package restopass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import restopass.dto.Membership;
import restopass.dto.MembershipsResponse;
import restopass.dto.request.UserCreationRequest;
import restopass.service.MembershipService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/memberships")
public class MembershipController {

    @Autowired
    MembershipService membershipService;

    private String USER_ID_ATTR = "userId";

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void createUser(@RequestBody Membership membership) {
        this.membershipService.createMembership(membership);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public MembershipsResponse getMemberships(HttpServletRequest request) {
        return this.membershipService.getMemberships(request.getAttribute(USER_ID_ATTR).toString());
    }

    
}
