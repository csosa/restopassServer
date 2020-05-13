package restopass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import restopass.mongo.RestaurantDTO;
import restopass.mongo.RestaurantRepository;

@RestController
public class BasicController {

    @Autowired
    RestaurantRepository restaurantRepository;

    @RequestMapping(value = "/holamundo", method = RequestMethod.GET)
    public void holaMundo() {
        restaurantRepository.save(new RestaurantDTO("1","La Nueva Casa Japonesa", "Humberto 1ero 2357"));
    }
}
