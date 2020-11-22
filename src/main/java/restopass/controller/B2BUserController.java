package restopass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import restopass.dto.B2BUserEmployer;
import restopass.service.B2BUserService;

@RestController
@RequestMapping("/users/b2b")
public class B2BUserController {

    @Autowired
    private B2BUserService b2BUserService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void createB2BUserEmployer(@RequestBody B2BUserEmployer user) {
        this.b2BUserService.createUser(user);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.POST)
    public void addExistingUserToCompany(@RequestBody B2BUserEmployer company, @PathVariable(name = "userId") String userId) {
        this.b2BUserService.addExistingUserToCompany(company, userId);
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void updateDiscounts(@RequestBody B2BUserEmployer user) {
        this.b2BUserService.updateDiscounts(user);
    }

}
