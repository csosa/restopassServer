package restopass.service;

import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;
import restopass.dto.*;
import restopass.dto.response.ReservationResponse;
import restopass.dto.response.UserReservation;
import restopass.exception.NoMoreVisitsException;
import restopass.exception.ReservationAlreadyConfirmedException;
import restopass.exception.ReservationCanceledException;
import restopass.mongo.ReservationRepository;
import restopass.utils.EmailSender;
import restopass.utils.QRHelper;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ReservationService {

    private MongoTemplate mongoTemplate;
    private ReservationRepository reservationRepository;
    private UserService userService;
    private FirebaseService firebaseService;
    private String RESTAURANT_ID = "restaurantId";
    private String OWNER_USER_ID = "ownerUser";
    private String RESERVATION_ID = "reservationId";
    private String RESERVATION_STATE = "state";
    private String CONFIRMED_USERS = "confirmedUsers";
    private String TO_CONFIRM_USERS = "toConfirmUsers";
    private String SLOTS_FIELD = "slots";
    private String RESERVATION_COLLECTION = "reservations";
    private String RESTAURANT_CONFIG_COLLECTION = "restaurant_configs";
    private Integer SIZE_CALENDAR = 45;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    public ReservationService(MongoTemplate mongoTemplate, ReservationRepository reservationRepository, UserService userService, FirebaseService firebaseService) {
        this.mongoTemplate = mongoTemplate;
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.firebaseService = firebaseService;
    }

    public void createReservation(Reservation reservation, String userId) {
        String reservationId = UUID.randomUUID().toString();
        reservation.setReservationId(reservationId);

        RestaurantConfig restaurantConfig = this.findConfigurationByRestaurantId(reservation.getRestaurantId());
        List<RestaurantSlot> slots = this.restaurantService.decrementTableInSlot(restaurantConfig, reservation.getDate());
        this.restaurantService.fillRestaurantData(reservation);
        this.updateSlotsInDB(reservation.getRestaurantId(), slots);

        this.userService.decrementUserVisits(userId);

        reservation.setQrBase64(QRHelper.createQRBase64(reservationId, reservation.getRestaurantId(), userId));

        this.sendConfirmBookingEmail(reservation);
        if(!CollectionUtils.isEmpty(reservation.getToConfirmUsers())) {
            this.sendNewBookingEmail(reservation);
            this.sendNewBookingNotif(reservation);
        }

        this.reservationRepository.save(reservation);
    }

    private void sendNewBookingNotif(Reservation reservation) {
        this.firebaseService.sendNewInvitationNotification(reservation.getToConfirmUsers(),
                reservation.getReservationId(), reservation.getOwnerUser(), reservation.getRestaurantName(),
                this.generateHumanDate(reservation.getDate()));
    }

    private void sendConfirmBookingEmail(Reservation reservation) {
        User user = this.userService.findById(reservation.getOwnerUser());
        Restaurant restaurant = this.restaurantService.findById(reservation.getRestaurantId());

        HashMap<String, Object> modelEmail = new HashMap<>();
        modelEmail.put("userName", user.getName());
        modelEmail.put("restaurantName", restaurant.getName());
        modelEmail.put("totalDiners", reservation.getToConfirmUsers().size() + 1);
        modelEmail.put("date", this.generateHumanDate(reservation.getDate()));
        modelEmail.put("restaurantAddress", restaurant.getAddress());
        modelEmail.put("qrCode", reservation.getQrBase64());
        modelEmail.put("cancelDate", reservation.getDate().minusHours(restaurant.getHoursToCancel()));


        EmailModel emailModel = new EmailModel();
        emailModel.setEmailTo(reservation.getOwnerUser());
        emailModel.setMailTempate("confirm_booking.html");
        emailModel.setSubject("Tu reserva ha sido confirmada");
        emailModel.setModel(modelEmail);

        EmailSender.sendEmail(emailModel);
    }

    private void sendNewBookingEmail(Reservation reservation) {
        User ownerUser = this.userService.findById(reservation.getOwnerUser());
        Restaurant restaurant = this.restaurantService.findById(reservation.getRestaurantId());

        HashMap<String, Object> modelEmail = new HashMap<>();
        modelEmail.put("ownerUser", ownerUser.getName() + " " + ownerUser.getLastName());
        modelEmail.put("restaurantName", restaurant.getName());
        modelEmail.put("totalDiners", reservation.getToConfirmUsers().size() + 1);
        modelEmail.put("date", this.generateHumanDate(reservation.getDate()));
        modelEmail.put("restaurantAddress", restaurant.getAddress());

        EmailModel emailModel = new EmailModel();
        emailModel.setMailTempate("new_booking.html");
        emailModel.setSubject("Parece que tienes una nueva reserva");
        emailModel.setModel(modelEmail);

        reservation.getToConfirmUsers().forEach(user -> {
            modelEmail.put("joinUrl", this.buildJoinUrl(reservation.getReservationId(), user));
            emailModel.setEmailTo(user);
            EmailSender.sendEmail(emailModel);
        });
    }

    public List<ReservationResponse> getReservationsForUser(String userId) {
        Query query = new Query();

        Criteria orCriteria = new Criteria();
        orCriteria.orOperator(
                Criteria.where(OWNER_USER_ID).is(userId),
                Criteria.where(CONFIRMED_USERS).in(userId));

        query.addCriteria(orCriteria);

        List<Reservation> reservations = this.mongoTemplate.find(query, Reservation.class);
        reservations.sort(Comparator.comparing(Reservation::getDate,
                Comparator.nullsLast(Comparator.reverseOrder())));

        return reservations.stream().map(r -> this.mapReservationToResponse(r, userId)).collect(Collectors.toList());
    }

    public List<ReservationResponse> cancelReservation(String reservationId, String userId) {
        this.updateReservationState(reservationId, ReservationState.CANCELED);
        List<ReservationResponse> reservations = this.getReservationsForUser(userId);

        ReservationResponse reservation = reservations.stream().filter(r -> r.getReservationId().equalsIgnoreCase(reservationId)).findFirst().get();
        this.firebaseService.sendCancelReservationNotification(
                reservation.getConfirmedUsers().stream().map(UserReservation::getUserId).collect(Collectors.toList()),
                reservationId, reservation.getOwnerUser().getName() + " " + reservation.getOwnerUser().getLastName(),
                reservation.getRestaurantName(), generateHumanDate(reservation.getDate()));

        return reservations;
    }

    public void confirmReservation(String reservationId, String userId) {
        User user = this.userService.findById(userId);
        Reservation reservation = this.findById(reservationId);

        if(reservation.getState().equals(ReservationState.CANCELED)) {
            throw new ReservationCanceledException();
        }
        if (user.getVisits() == null || (user.getVisits() != null && user.getVisits() == 0)) {
            throw new NoMoreVisitsException();
        }

        if(reservation.getConfirmedUsers() != null && reservation.getConfirmedUsers().stream().anyMatch(u -> u.equalsIgnoreCase(userId))) {
            throw new ReservationAlreadyConfirmedException();
        }

        Query query = new Query();
        query.addCriteria(Criteria.where(RESERVATION_ID).is(reservationId));

        Update update = new Update();
        update.pull(TO_CONFIRM_USERS, userId);
        update.push(CONFIRMED_USERS, userId);

        this.mongoTemplate.updateMulti(query, update, RESERVATION_COLLECTION);

        this.firebaseService.sendConfirmedInvitationNotification(reservation.getOwnerUser(), reservationId,
                user.getName() + " " + user.getLastName(), reservation.getRestaurantName(),
                this.generateHumanDate(reservation.getDate()));
        this.userService.decrementUserVisits(userId);

    }

    public ModelAndView doneReservation(String reservationId, String restaurantId, String userId) {

        Reservation reservation = this.findById(reservationId);
        User user = this.userService.findById(userId);
        Restaurant restaurant = this.restaurantService.findById(restaurantId);

        List<Dish> dishes = restaurant.getDishes();
        //TODO agregar validaciones futuras

        Map<Integer, Dish> dishesMap = new HashMap<>();
        Map<Integer, Integer> countUsersMemberships = new HashMap<>();
        countUsersMemberships.put(user.getActualMembership(), 1);

        List<String> userOwnerAndConfirmed = Arrays.asList(reservation.getOwnerUser());

        if (reservation.getConfirmedUsers() != null) {
            userOwnerAndConfirmed.addAll(reservation.getConfirmedUsers());
            reservation.getConfirmedUsers().forEach(s -> {
                User invitedUser = this.userService.findById(s);

                if(countUsersMemberships.containsKey(invitedUser.getActualMembership())){
                    Integer count = countUsersMemberships.get(invitedUser.getActualMembership());
                    countUsersMemberships.replace(invitedUser.getActualMembership(), count +1);
                }else{
                    countUsersMemberships.put(invitedUser.getActualMembership(), 1);
                }
            });
        }

        this.updateReservationState(reservationId, ReservationState.DONE);
        this.firebaseService.sendScoreNotification(userOwnerAndConfirmed, reservation.getRestaurantId(), reservation.getRestaurantName());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("restaurant_name", restaurant.getName());
        modelAndView.addObject("name", user.getName());
        modelAndView.addObject("lastname", user.getLastName());
        modelAndView.addObject("dishes", dishes);
        modelAndView.setViewName("/reservation/done-reservation");

        return modelAndView;
    }

    private void updateReservationState(String reservationId, ReservationState state) {
        Query query = new Query();
        query.addCriteria(Criteria.where(RESERVATION_ID).is(reservationId));

        Update update = new Update();
        update.set(RESERVATION_STATE, state);

        this.mongoTemplate.updateMulti(query, update, RESERVATION_COLLECTION);
    }

    public RestaurantConfig generateSlotsByRestaurantConfig(String restaurantId) {
        RestaurantConfig restaurantConfig = this.findConfigurationByRestaurantId(restaurantId);
        List<RestaurantHours> restaurantHours = restaurantConfig.getDateTimeAvailable();
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
                            LocalDateTime startHour = date.withHour(pair.getOpeningHour()).withMinute(pair.getOpeningMinute()).truncatedTo(ChronoUnit.MINUTES);;
                            LocalDateTime endHour = date.withHour(pair.getClosingHour()).withMinute(pair.getClosingMinute()).truncatedTo(ChronoUnit.MINUTES);;
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

    public RestaurantConfig findConfigurationByRestaurantId(String restaurantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(RESTAURANT_ID).is(restaurantId));

        return this.mongoTemplate.findOne(query, RestaurantConfig.class);
    }

    private void updateSlotsInDB(String restaurantId, List<RestaurantSlot> slots) {
        Query query = new Query();
        query.addCriteria(Criteria.where(RESTAURANT_ID).is(restaurantId));

        Update update = new Update();
        update.set(SLOTS_FIELD, slots);

        this.mongoTemplate.updateMulti(query, update, RESTAURANT_CONFIG_COLLECTION);
    }

    private String generateHumanDate(LocalDateTime dt) {
        String dayName = dt.getDayOfWeek().getDisplayName(TextStyle.FULL,
                new Locale("es"));

        String monthName = dt.getMonth().getDisplayName(TextStyle.FULL, new Locale("es"));

        String hour;
        if(dt.getMinute() == 0) {
            hour = dt.getHour() + ":00";
        } else {
           hour = dt.getHour() + ":" + dt.getMinute();
        }

        return Strings.capitalize(dayName) + " " + dt.getDayOfMonth() + " de " + Strings.capitalize(monthName) + " de " + dt.getYear() + " a las " + hour + "hs";
    }

    private String buildJoinUrl(String reservationId, String userId) {
        return "https://restopass.herokuapp.com/reservations/confirm/" + reservationId + "/" + userId;
    }

    private ReservationResponse mapReservationToResponse(Reservation reservation, String userId) {
        ReservationResponse response = new ReservationResponse();

        response.setReservationId(reservation.getReservationId());
        response.setRestaurantId(reservation.getRestaurantId());
        response.setDate(reservation.getDate());
        response.setQrBase64(reservation.getQrBase64());
        response.setRestaurantAddress(reservation.getRestaurantAddress());
        response.setRestaurantName(reservation.getRestaurantName());
        response.setState(reservation.getState());
        if (reservation.getConfirmedUsers() != null) response.setConfirmedUsers(reservation.getConfirmedUsers().stream().map(this::mapEmailToUserReservation).collect(Collectors.toList()));
        if (reservation.getToConfirmUsers() != null) response.setToConfirmUsers(reservation.getToConfirmUsers().stream().map(this::mapEmailToUserReservation).collect(Collectors.toList()));
        response.setOwnerUser(mapEmailToUserReservation(reservation.getOwnerUser()));
        if(!reservation.getOwnerUser().equalsIgnoreCase(userId))response.setIsInvitation(true);

        return response;
    }

    private UserReservation mapEmailToUserReservation(String userId) {
        User user = this.userService.findById(userId);
        UserReservation response = new UserReservation();

        response.setUserId(userId);
        response.setName(user.getName());
        response.setLastName(user.getLastName());

        return response;
    }

    public Reservation findById(String reservationId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(RESERVATION_ID).is(reservationId));

        return this.mongoTemplate.findOne(query, Reservation.class);
    }

}
