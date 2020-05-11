package restopass.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import restopass.mongo.RestaurantDTO;
import restopass.mongo.RestaurantRepository;

@RestController
public class BasicController {

    RestaurantRepository restaurantRepository;

    @RequestMapping(value = "/holamundo", method = RequestMethod.GET)
    public void holaMundo() {
        restaurantRepository.save(new RestaurantDTO("La Nueva Casa Japonesa", "Humberto 1ero 2357"));
    }
}
