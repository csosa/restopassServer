package restopass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import restopass.dto.B2CUserEmployer;
import restopass.service.B2CUserService;

@RestController
@RequestMapping("/users/b2c")
public class B2CUserController {

    @Autowired
    private B2CUserService b2CUserService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void createB2CUserEmployer(@RequestBody B2CUserEmployer user) {
        this.b2CUserService.createUser(user);
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void updateDiscounts(@RequestBody B2CUserEmployer user) {
        this.b2CUserService.updateDiscounts(user);
    }

}
