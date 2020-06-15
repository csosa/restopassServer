package restopass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import restopass.dto.User;
import restopass.dto.request.UserCreationRequest;
import restopass.dto.request.UserLoginRequest;
import restopass.dto.response.UserLoginResponse;
import restopass.service.UserService;
import restopass.utils.JWTHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private String USER_ID_ATTR = "userId";

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public UserLoginResponse userLogin(@RequestBody UserLoginRequest user) {
        return userService.loginUser(user);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public UserLoginResponse createUser(@RequestBody UserCreationRequest user) {
        return this.userService.createUser(user);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public UserLoginResponse refreshToken(HttpServletRequest request) {
        return this.userService.refreshToken(request);
    }

    @RequestMapping(value = "/favorite/{restaurantId}", method = RequestMethod.POST)
    public void addRestaurantToFavorites(HttpServletRequest request, @PathVariable String restaurantId) {
        String userId = request.getAttribute(USER_ID_ATTR).toString();
        this.userService.addNewRestaurantFavorite(restaurantId, userId);
    }

    @RequestMapping(value = "/unfavorite/{restaurantId}", method = RequestMethod.POST)
    public void removeRestaurantFromFavorites(HttpServletRequest request, @PathVariable String restaurantId) {
        String userId = request.getAttribute(USER_ID_ATTR).toString();
        this.userService.removeRestaurantFavorite(restaurantId, userId);
    }
}
