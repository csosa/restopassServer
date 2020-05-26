package restopass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import restopass.dto.Dish;
import restopass.dto.Restaurant;
import restopass.dto.request.RestaurantCreationRequest;
import restopass.dto.request.RestaurantTagsRequest;
import restopass.service.RestaurantService;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantsController {

    @Autowired
    RestaurantService restaurantService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void createRestaurante(@RequestBody RestaurantCreationRequest restaurant) {
        this.restaurantService.createRestaurant(restaurant);
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

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Restaurant test() {
        Restaurant restaurant =  new Restaurant();
        restaurant.setRestaurantId("Holis");
        return restaurant;
    }


}
