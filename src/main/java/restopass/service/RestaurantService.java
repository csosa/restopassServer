package restopass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import restopass.dto.Dish;
import restopass.dto.Membership;
import restopass.dto.Restaurant;
import restopass.mongo.RestaurantsRepository;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {

    final RestaurantsRepository restaurantsRepository;
    protected final MongoTemplate mongoTemplate;

    private String RESTAURANT_ID = "restaurantId";
    private String DISHES_FIELD = "dishes";
    private String LOCATION_FIELD = "location";
    private String RESTAURANTS_COLLECTION = "restaurants";
    private Double KM_RADIUS = 10D;


    @Autowired
    public RestaurantService(RestaurantsRepository restaurantsRepository, MongoTemplate mongoTemplate) {
        this.restaurantsRepository = restaurantsRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void createRestaurant(Restaurant restaurant) {
        String restaurantId = UUID.randomUUID().toString();
        restaurant.setRestaurantId(restaurantId);
        this.restaurantsRepository.save(restaurant);
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

    public List<Restaurant> getByTags(List<String> tags, Membership topMembership) {
        return null;
    }


}
