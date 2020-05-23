package restopass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import restopass.dto.Membership;
import restopass.dto.response.MembershipsResponse;
import restopass.service.MembershipService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/memberships")
public class MembershipController {

    @Autowired
    MembershipService membershipService;

    private String USER_ID_ATTR = "userId";

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void createMembership(@RequestBody Membership membership) {
        this.membershipService.createMembership(membership);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public MembershipsResponse getMemberships(HttpServletRequest request) {
        return this.membershipService.getMemberships(request.getAttribute(USER_ID_ATTR).toString());
    }


}
