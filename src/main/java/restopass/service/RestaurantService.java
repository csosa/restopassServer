package restopass.service;

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
import restopass.dto.Dish;
import restopass.dto.MembershipType;
import restopass.dto.Restaurant;
import restopass.dto.request.RestaurantCreationRequest;
import restopass.mongo.RestaurantRepository;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {

    final RestaurantRepository restaurantRepository;
    protected final MongoTemplate mongoTemplate;

    private String RESTAURANT_ID = "restaurantId";
    private String DISHES_FIELD = "dishes";
    private String TOP_MEMBERSHIP_FIELD = "topMembership";
    private String LOCATION_FIELD = "location";
    private String RESTAURANTS_COLLECTION = "restaurants";
    private String TAGS_FIELD = "tags";
    private Double KM_RADIUS = 10D;


    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, MongoTemplate mongoTemplate) {
        this.restaurantRepository = restaurantRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void createRestaurant(RestaurantCreationRequest restaurantCreation) {
        Restaurant restaurant = new Restaurant();
        String restaurantId = UUID.randomUUID().toString();
        restaurant.setRestaurantId(restaurantId);

        restaurant.setAddress(restaurantCreation.getAddress());
        restaurant.setImg(restaurantCreation.getImg());
        GeoJsonPoint point = new GeoJsonPoint(restaurantCreation.getLongitude(), restaurantCreation.getLatitude());
        restaurant.setLocation(point);
        restaurant.setName(restaurantCreation.getName());
        restaurant.setTimeTable(restaurantCreation.getTimeTable());
        restaurant.setTags(restaurantCreation.getTags());
        restaurant.setDishes(restaurantCreation.getDishes());

        this.restaurantRepository.save(restaurant);
    }

    public void addDish(Dish dish, String restaurantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(RESTAURANT_ID).is(restaurantId));

        Update update = new Update();
        update.addToSet(DISHES_FIELD, dish);
        this.mongoTemplate.updateMulti(query, update, RESTAURANTS_COLLECTION);
    }

    public List<Restaurant> getInARadius(Double lat, Double lng) {
        Query query = new Query();

        Point geoPoint = new Point(lng, lat);
        Distance geoDistance = new Distance(KM_RADIUS, Metrics.KILOMETERS);
        Circle geoCircle = new Circle(geoPoint, geoDistance);
        query.addCriteria(Criteria.where(LOCATION_FIELD).withinSphere(geoCircle));

        return this.mongoTemplate.find(query, Restaurant.class);
    }

    public List<Restaurant> getByTags(List<String> tags, MembershipType topMembership) {
        Query query = new Query();
        query.addCriteria(Criteria.where(TAGS_FIELD).all(tags));
        return null;
    }

    public List<Restaurant> getRestaurantInAMemberships(MembershipType membership) {
        Query query = new Query();
        query.addCriteria(Criteria.where(DISHES_FIELD).elemMatch(Criteria.where(TOP_MEMBERSHIP_FIELD).lte(membership.ordinal())));

        return this.mongoTemplate.find(query, Restaurant.class);
    }


}
