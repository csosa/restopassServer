package restopass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import restopass.dto.Dish;
import restopass.dto.Restaurant;
import restopass.dto.RestaurantConfig;
import restopass.dto.firebase.SimpleTopicPush;
import restopass.dto.firebase.SimplePushData;
import restopass.dto.firebase.SimplePushNotif;
import restopass.dto.request.RestaurantCreationRequest;
import restopass.dto.request.RestaurantTagsRequest;
import restopass.dto.response.RestaurantTagsResponse;
import restopass.service.FirebaseService;
import restopass.service.ReservationService;
import restopass.service.RestaurantService;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantsController {

    @Autowired
    RestaurantService restaurantService;
    @Autowired
    ReservationService reservationService;
    @Autowired
    FirebaseService firebaseService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void createRestaurant(@RequestBody RestaurantCreationRequest restaurant) {
        this.restaurantService.createRestaurant(restaurant);
    }

    @RequestMapping(value = "config", method = RequestMethod.POST)
    public void createRestaurantConfig(@RequestBody RestaurantConfig restaurant) {
        this.restaurantService.createRestaurantConfig(restaurant);
    }

    @RequestMapping(value = "/dishes/{restaurantId}", method = RequestMethod.PATCH)
    public void addPlate(@RequestBody Dish dish, @PathVariable String restaurantId) {
        this.restaurantService.addDish(dish, restaurantId);
    }

    @RequestMapping(value = "/{lat}/{lng}", method = RequestMethod.GET)
    public List<Restaurant> getRestaurantInARadius(@PathVariable Double lat, @PathVariable Double lng) {
        return this.restaurantService.getInARadius(lat,lng);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public List<Restaurant> getRestaurantByTags(@RequestBody RestaurantTagsRequest request) {
        return this.restaurantService.getByTags(request.getTags(), request.getTopMembership(), request.getFreeText());
    }

    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public RestaurantTagsResponse getRestaurantsTags() {
        return this.restaurantService.getRestaurantsTags();
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test() {
        SimpleTopicPush simpleTopicPush = new SimpleTopicPush();
        simpleTopicPush.setTo("pruebaprueba.com");
        SimplePushNotif simplePushNotif = new SimplePushNotif();
        simplePushNotif.setBody("El body");
        simplePushNotif.setTitle("El title");
        simpleTopicPush.setNotification(simplePushNotif);
        SimplePushData simplePushData = new SimplePushData();
        simplePushData.setReservationId("unaReserva");
        simplePushData.setType("RESERVATION");
        simpleTopicPush.setData(simplePushData);

        firebaseService.sendNotification(simpleTopicPush);
    }


}
