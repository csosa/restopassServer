package restopass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import restopass.dto.request.UserCreationRequest;
import restopass.dto.request.UserLoginRequest;
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
    public void userLogin(@RequestBody UserLoginRequest user, HttpServletResponse response) {
        userService.loginUser(user, response);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void userLogin(@RequestBody UserCreationRequest user) {
        this.userService.createUser(user);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public void userLogin(HttpServletRequest request, HttpServletResponse response) {
        this.userService.refreshToken(request, response);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void isAccessTokenValid() {
    }


}
