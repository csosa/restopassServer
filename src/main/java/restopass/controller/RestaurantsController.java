package restopass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import restopass.dto.Dish;
import restopass.dto.EmailModel;
import restopass.dto.Restaurant;
import restopass.dto.RestaurantConfig;
import restopass.dto.request.RestaurantCreationRequest;
import restopass.dto.request.RestaurantTagsRequest;
import restopass.dto.response.RestaurantTagsResponse;
import restopass.service.ReservationService;
import restopass.service.RestaurantService;
import restopass.utils.EmailSender;
import restopass.utils.QRHelper;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantsController {

    @Autowired
    RestaurantService restaurantService;
    @Autowired
    ReservationService reservationService;

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
        HashMap<String, Object> modelEmail = new HashMap<>();
        modelEmail.put("ownerUser", "Yamila Casarini");
        modelEmail.put("restaurantName", "La Causa Nikkei");
        modelEmail.put("totalDiners", "4");
        modelEmail.put("dayName", "Sabado");
        modelEmail.put("day", "5");
        modelEmail.put("monthName", "Mayo");
        modelEmail.put("year", "2020");
        modelEmail.put("hour", "20:00");
        modelEmail.put("restaurantAddress", "Av Callao 1231, CABA");

        EmailModel emailModel = new EmailModel();
        emailModel.setEmailTo("restopassprueba@yopmail.com");
        emailModel.setMailTempate("new_booking.html");
        emailModel.setSubject("Parece que tienes una nueva reserva");
        emailModel.setModel(modelEmail);

        EmailSender.sendEmail(emailModel);
    }


}
