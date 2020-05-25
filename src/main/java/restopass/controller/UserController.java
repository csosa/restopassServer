package restopass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public UserLoginResponse userLogin(@RequestBody UserLoginRequest user) {
        return userService.loginUser(user);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public User createUser(@RequestBody UserCreationRequest user) {
        return this.userService.createUser(user);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public UserLoginResponse refreshToken(HttpServletRequest request) {
        return this.userService.refreshToken(request);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void isAccessTokenValid() {
    }


}
