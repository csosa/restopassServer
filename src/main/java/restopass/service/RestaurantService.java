package restopass.service;

import com.google.common.collect.Lists;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import restopass.dto.*;
import restopass.dto.request.DishRequest;
import restopass.dto.request.RestaurantCreationRequest;
import restopass.dto.request.RestaurantCommentRequest;
import restopass.dto.response.ReservationResponse;
import restopass.dto.response.RestaurantCommentResponse;
import restopass.dto.response.RestaurantResponse;
import restopass.dto.response.RestaurantTagsResponse;
import restopass.exception.LastTableAlreadyBookedException;
import restopass.mongo.FiltersMapRepository;
import restopass.mongo.RestaurantConfigRepository;
import restopass.mongo.RestaurantRepository;

import java.lang.reflect.Member;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class RestaurantService {

    protected final MongoTemplate mongoTemplate;
    final RestaurantRepository restaurantRepository;
    final FiltersMapRepository filtersMapRepository;
    final RestaurantConfigRepository restaurantConfigRepository;
    private String RESTAURANT_ID = "restaurantId";
    private String DISHES_FIELD = "dishes";
    private String BASE_MEMBERSHIP_FIELD = "baseMembership";
    private String LOCATION_FIELD = "location";
    private String RESTAURANT_NAME = "name";
    private String RESTAURANTS_COLLECTION = "restaurants";
    private String STARS_FIELD = "stars";
    private String COUNT_STARS_FIELD = "countStars";
    private String TAGS_FIELD = "tags";
    private String COMMENTS_FIELD = "comments";
    private Double DEFAULT_KM_RADIUS = 30D;
    private Integer SIZE_CALENDAR = 45;
    private String RESTAURANT_CONFIG_COLLECTION = "restaurant_configs";
    private String SLOTS_FIELD = "slots";

    @Autowired
    private MembershipService membershipService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private FirebaseService firebaseService;


    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository,
                             FiltersMapRepository filtersMapRepository,
                             MongoTemplate mongoTemplate, RestaurantConfigRepository restaurantConfigRepository) {
        this.restaurantRepository = restaurantRepository;
        this.filtersMapRepository = filtersMapRepository;
        this.restaurantConfigRepository = restaurantConfigRepository;
        this.mongoTemplate = mongoTemplate;

    }

    public void createRestaurant(RestaurantCreationRequest restaurantCreation) {
        Restaurant restaurant = new Restaurant();
        String restaurantId = UUID.randomUUID().toString();
        restaurant.setRestaurantId(restaurantId);

        restaurant.setAddress(restaurantCreation.getAddress());
        restaurant.setImg(this.firebaseService.createImageFromURL(restaurantCreation.getImg(), restaurantId, restaurantId));
        GeoJsonPoint point = new GeoJsonPoint(restaurantCreation.getLongitude(), restaurantCreation.getLatitude());
        restaurant.setLocation(point);
        restaurant.setName(restaurantCreation.getName());
        restaurant.setTimeTable(restaurantCreation.getTimeTable());
        restaurant.setTags(restaurantCreation.getTags());
        List<DishRequest> dishes = restaurantCreation.getDishes();
        List<Dish> dishesToSave;
        if (dishes != null) {
            dishesToSave = dishes.stream().map(dr -> {
                String dishId = UUID.randomUUID().toString();
                return new Dish(dishId, dr.getName(), this.firebaseService.createImageFromURL(dr.getImg(), dishId, restaurantId),
                        dr.getDescription(), dr.getBaseMembership(), dr.getTags());
            }).collect(Collectors.toList());
            restaurant.setDishes(dishesToSave);
        }

        this.restaurantRepository.save(restaurant);
    }

    public void createRestaurantConfig(RestaurantConfig restaurantConfig) {
        RestaurantConfig restaurantConfigBD = this.findConfigurationByRestaurantId(restaurantConfig.getRestaurantId());

        if (restaurantConfigBD == null) {
            this.restaurantConfigRepository.save(restaurantConfig);
        }

        this.generateSlotsByRestaurantConfig(restaurantConfig.getRestaurantId());
    }

    public RestaurantConfig generateSlotsByRestaurantConfig(String restaurantId) {
        RestaurantConfig restaurantConfig = this.findConfigurationByRestaurantId(restaurantId);
        Restaurant restaurant = this.findById(restaurantId);
        List<RestaurantHours> restaurantHours = restaurant.getTimeTable();
        LocalDateTime today = LocalDateTime.now();

        List<RestaurantSlot> slots = new ArrayList<>();

        //Genero el calendario de 45 dias
        IntStream.rangeClosed(0, SIZE_CALENDAR).forEach(i -> {
                    LocalDateTime date = today.plusDays(i);

                    //Busco la configuracion de horas que incluye el dia que estoy queriendo generar.
                    //Si esta cerrado el local, no va a existir esa configuracion por eso Optional
                    Optional<RestaurantHours> optionalDay = restaurantHours.stream().filter(rh -> rh.getOpeningDays().stream().anyMatch(op -> op.equals(date.getDayOfWeek()))).findFirst();

                    if (optionalDay.isPresent()) {
                        RestaurantHours day = optionalDay.get();
                        RestaurantSlot slot = new RestaurantSlot();
                        List<List<DateTimeWithTables>> allDateTimeWithTables = new ArrayList<>();

                        List<PairHour> pairHours = day.getPairHours();

                        //Por cada par hora inicio hora fin genero los distintos horarios con sus mesas
                        pairHours.forEach(pair -> {
                            LocalDateTime startHour = date.withHour(pair.getOpeningHour()).withMinute(pair.getOpeningMinute()).truncatedTo(ChronoUnit.MINUTES);
                            LocalDateTime endHour = date.withHour(pair.getClosingHour()).withMinute(pair.getClosingMinute()).truncatedTo(ChronoUnit.MINUTES);
                            List<DateTimeWithTables> dateTimeWithTables = new ArrayList<>();

                            Integer minutes = restaurantConfig.getMinutesGap();
                            int minutesCount = 0;
                            long minutesUntilEndHour = startHour.until(endHour, ChronoUnit.MINUTES);

                            //Empiezo a generar las distintas horas con sus mesas
                            while (minutesCount < minutesUntilEndHour) {
                                DateTimeWithTables dt = new DateTimeWithTables();
                                dt.setDateTime(startHour.plusMinutes(minutesCount));
                                dt.setTablesAvailable(restaurantConfig.getTablesPerShift());
                                dateTimeWithTables.add(dt);
                                minutesCount = minutesCount + minutes;
                            }
                            //Cuando termino de generar todos los horarios para un par inicio fin lo agrego al conjunto de todos los pares inicio fin
                            allDateTimeWithTables.add(dateTimeWithTables);
                        });
                        //Cuando tengo todos los pares inicio fin los agrego al dia
                        slot.setDateTime(allDateTimeWithTables);
                        slots.add(slot);
                    }
                }
        );

        this.updateSlotsInDB(restaurantId, slots);
        restaurantConfig.setSlots(slots);
        return restaurantConfig;
    }

    public RestaurantConfig buildRestaurantConfig(String restaurantId) {
        RestaurantConfig restaurantConfig = this.findConfigurationByRestaurantId(restaurantId);

        if (restaurantConfig != null) {
            restaurantConfig.getSlots().forEach(slot -> {
                slot.getDateTime().forEach(dates -> {
                    if (dates.stream().allMatch(date -> date.getTablesAvailable() <= 0)) slot.setDayFull(true);
                });
            });
        }

        return restaurantConfig;
    }

    public RestaurantConfig findConfigurationByRestaurantId(String restaurantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(RESTAURANT_ID).is(restaurantId));

        return this.mongoTemplate.findOne(query, RestaurantConfig.class);
    }

    public void updateSlotsInDB(String restaurantId, List<RestaurantSlot> slots) {
        Query query = new Query();
        query.addCriteria(Criteria.where(RESTAURANT_ID).is(restaurantId));

        Update update = new Update();
        update.set(SLOTS_FIELD, slots);

        this.mongoTemplate.updateMulti(query, update, RESTAURANT_CONFIG_COLLECTION);
    }

    public List<RestaurantSlot> decrementTableInSlot(RestaurantConfig restaurantConfig, LocalDateTime dateTime) {
        restaurantConfig.getSlots().forEach(
                slot ->
                        slot.getDateTime().forEach(dt ->
                                dt.forEach(date -> {
                                            if (date.getDateTime().equals(dateTime)) {
                                                if (date.getTablesAvailable() <= 0) throw new LastTableAlreadyBookedException();
                                                date.setTablesAvailable(date.getTablesAvailable() - 1);
                                            }
                                        }
                                )
                        )
        );

        return restaurantConfig.getSlots();
    }

    public void addDish(DishRequest dishRequest, String restaurantId) {
        String dishId = UUID.randomUUID().toString();
        Dish dish = new Dish(dishId, dishRequest.getName(), this.firebaseService.createImageFromURL(dishRequest.getImg(), dishId, restaurantId),
                dishRequest.getDescription(), dishRequest.getBaseMembership(), dishRequest.getTags());
        Query query = new Query();
        query.addCriteria(Criteria.where(RESTAURANT_ID).is(restaurantId));

        Update update = new Update();
        update.addToSet(DISHES_FIELD, dish);
        this.mongoTemplate.updateMulti(query, update, RESTAURANTS_COLLECTION);
    }

    public List<RestaurantResponse> getByTags(Double lat, Double lng, Double radius, List<String> tags, Integer topMembership, String freeText) {
        List<String> freeTextList = Arrays.asList(Strings.delimitedListToStringArray(freeText, " "));

        Query query = new Query();

        Point geoPoint = new Point(lng, lat);
        Distance geoDistance = new Distance(radius != null ? radius : DEFAULT_KM_RADIUS, Metrics.KILOMETERS);
        Circle geoCircle = new Circle(geoPoint, geoDistance);
        query.addCriteria(Criteria.where(LOCATION_FIELD).withinSphere(geoCircle));

        if (tags == null) {
            tags = new ArrayList<>();
        }

        tags.addAll(freeTextList);

        Criteria orTagFreeTextCriteria = new Criteria();
        List<Criteria> criterias = new ArrayList<>();

        if (!tags.isEmpty()) {
            criterias.add(Criteria.where(TAGS_FIELD).all(tags));
        }

        if (freeText != null) {
            String freeTextRegex = freeTextList.stream().map(s -> ".*" + Strings.capitalize(s) + ".*").collect(Collectors.joining("|"));
            criterias.add(Criteria.where(RESTAURANT_NAME).regex(freeTextRegex));
        }

        if (!criterias.isEmpty()) {
            orTagFreeTextCriteria.orOperator(criterias.toArray(new Criteria[criterias.size()]));
            query.addCriteria(orTagFreeTextCriteria);
        }

        if (topMembership != null) {
            query.addCriteria(Criteria.where(DISHES_FIELD).elemMatch(Criteria.where(BASE_MEMBERSHIP_FIELD).lte(topMembership)));
        }

        List<Restaurant> restaurants = this.mongoTemplate.find(query, Restaurant.class);
        return restaurants.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<Restaurant> getRestaurantInAMemberships(Integer membership) {
        Query query = new Query();
        query.addCriteria(Criteria.where(DISHES_FIELD).elemMatch(Criteria.where(BASE_MEMBERSHIP_FIELD).lte(membership)));

        return this.mongoTemplate.find(query, Restaurant.class);
    }

    public RestaurantTagsResponse getRestaurantsTags() {
        RestaurantTagsResponse response = new RestaurantTagsResponse();

        List<FilterMap> filtersMap = this.filtersMapRepository.findAll();
        List<Membership> memberships = membershipService.findAll();

        HashMap<String, List<String>> tags = new HashMap<>();

        filtersMap.forEach(filter -> tags.put(filter.getName(), filter.getElements()));

        response.setMemberships(memberships);
        response.setTags(tags);

        return response;
    }

    public Restaurant findById(String restaurantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(RESTAURANT_ID).is(restaurantId));

        return this.mongoTemplate.findOne(query, Restaurant.class);
    }

    public RestaurantResponse getRestaurantById(String restaurantId) {
        Restaurant r = this.findById(restaurantId);
        r.getDishes().sort(Comparator.comparing(Dish::getBaseMembershipName));
        r.getDishes().forEach(Dish::setAverageStars);
        return toResponse(r);
    }

    public void fillRestaurantData(ReservationResponse reservation) {
        Restaurant restaurant = this.findById(reservation.getRestaurantId());
        reservation.setImg(restaurant.getImg());
        reservation.setRestaurantAddress(restaurant.getAddress());
        reservation.setRestaurantName(restaurant.getName());

        reservation.setMinMembershipRequired(getMinMembershipRequired(restaurant));
    }

    public Set<RestaurantResponse> findAllFavoritesByUser(String userId) {
        Set<String> restaurantsIds = this.userService.findById(userId).getFavoriteRestaurants();

        return restaurantsIds.stream().map(this::findById)
                .map(this::toResponse).collect(Collectors.toSet());
    }

    public void scoreRestaurantAndDish(RestaurantCommentRequest commentReq, String userId) {
        Restaurant restaurant = this.findById(commentReq.getRestaurantId());
        restaurant.setCountStars(restaurant.getCountStars() + 1);
        restaurant.setStars(restaurant.getStars() + commentReq.getRestaurantStars());

        restaurant.getDishes().forEach(d -> {
            if (d.getDishId().equalsIgnoreCase(commentReq.getDishId())) {
                d.setCountStars(d.getCountStars() + 1);
                d.setStars(d.getStars() + commentReq.getDishStars());
            }
        });

        RestaurantComment comment = new RestaurantComment(UUID.randomUUID().toString(),
                userId,
                commentReq.getDishId(),
                commentReq.getDishStars(),
                commentReq.getRestaurantStars(),
                commentReq.getDescription());

        Query query = new Query();
        query.addCriteria(Criteria.where(RESTAURANT_ID).is(commentReq.getRestaurantId()));

        Update update = new Update();
        update.set(STARS_FIELD, restaurant.getStars());
        update.set(COUNT_STARS_FIELD, restaurant.getCountStars());
        update.set(DISHES_FIELD, restaurant.getDishes());
        update.push(COMMENTS_FIELD, comment);

        this.mongoTemplate.updateMulti(query, update, RESTAURANTS_COLLECTION);

        this.reservationService.updateAlreadyScoreUser(commentReq.getReservationId(), userId);

    }

    public void updateTimeTable(String restaurantId, Restaurant restaurant) {
        Query query = new Query();
        query.addCriteria(Criteria.where(RESTAURANT_ID).is(restaurantId));

        Update update = new Update();
        update.set("timeTable", restaurant.getTimeTable());

        this.mongoTemplate.updateMulti(query, update, RESTAURANTS_COLLECTION);

    }

    public RestaurantResponse toResponse(Restaurant restaurant) {
        restaurant.setAverageStars();
        RestaurantResponse response = new RestaurantResponse(restaurant);

        if (restaurant.getComments() != null) {
            List<RestaurantCommentResponse> comments = restaurant.getComments().stream().map(comment ->
                    this.fromCommentToResponse(restaurant, comment)).collect(Collectors.toList());
            response.setComments(comments);
        }

        return response;
    }

    private RestaurantCommentResponse fromCommentToResponse(Restaurant restaurant, RestaurantComment comment) {
        User user = this.userService.findById(comment.getUserId());
        Dish dish = restaurant.getDishes().stream().filter(d -> d.getDishId().equals(comment.getDishId())).findAny().get();

        RestaurantCommentResponse response = new RestaurantCommentResponse();
        response.setCommentId(comment.getCommentId());
        response.setDate(comment.getDate());
        response.setDescription(comment.getDescription());
        response.setDishStars(comment.getDishStars());
        response.setRestaurantStars(comment.getRestaurantStars());
        response.setDish(dish);
        response.setUser(user);

        return response;
    }

    public Boolean isEnabledToBook(String userId, String restaurantId) {
        User user = this.userService.findById(userId);

        if (user.getActualMembership() == null) {
            return false;
        }

        Integer minMembershipRequired = getMinMembershipRequired(restaurantId).getMembershipId();

        return minMembershipRequired != null && user.getActualMembership() >= minMembershipRequired;
    }

    public Membership getMinMembershipRequired(String restaurantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(RESTAURANT_ID).is(restaurantId));

        Restaurant restaurant = this.mongoTemplate.findOne(query, Restaurant.class);

        return getMinMembershipRequired(restaurant);
    }

    private Membership getMinMembershipRequired(Restaurant restaurant) {
        if (restaurant != null && restaurant.getDishes() != null) {
            Integer membershipId = restaurant.getDishes().stream().map(Dish::getBaseMembership).min(Integer::compare).get();
            return membershipService.getMembershipById(membershipId);
        }

        return null;
    }

    public void deleteUserComments(String userId) {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        List<Restaurant> commentedRestaurants = restaurants.stream()
                .filter(restaurant -> restaurant.getComments().stream().anyMatch(comment -> comment.getUserId().equals(userId)))
        .collect(Collectors.toList());

        commentedRestaurants.forEach(restaurant -> {
            List<RestaurantComment> comments = restaurant.getComments().stream()
                    .filter(comment -> !comment.getUserId().equals(userId)).collect(Collectors.toList());
            Query query = new Query();
            query.addCriteria(Criteria.where(RESTAURANT_ID).is(restaurant.getRestaurantId()));
            Update update = new Update();
            update.set(COMMENTS_FIELD, comments);

            this.mongoTemplate.updateMulti(query, update, RESTAURANTS_COLLECTION);
        });
    }

}
