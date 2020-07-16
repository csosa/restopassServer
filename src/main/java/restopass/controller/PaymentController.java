package restopass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import restopass.dto.CreditCard;
import restopass.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/users/payment")
public class PaymentController {

    @Autowired
    private UserService userService;

    private final String USER_ID_ATTR = "userId";

    @GetMapping(value = "")
    public CreditCard getUserPayment(HttpServletRequest request) {
        return this.userService.getPayment(request.getAttribute(USER_ID_ATTR).toString());
    }

    @PatchMapping(value = "")
    public void updateUserPayment(HttpServletRequest request, @RequestBody CreditCard creditcard) {
        String userId = request.getAttribute(USER_ID_ATTR).toString();
        this.userService.updatePayment(creditcard, userId);
    }

    @DeleteMapping("")
    public void deleteUserPayment(HttpServletRequest request) {
        String userId = request.getAttribute(USER_ID_ATTR).toString();
        this.userService.removePayment(userId);
    }
}
