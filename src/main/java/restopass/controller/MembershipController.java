package restopass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import restopass.dto.Membership;
import restopass.dto.request.UpdateMembershipToUserRequest;
import restopass.dto.response.ChangeMembershipResponse;
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

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public ChangeMembershipResponse updateMembershipToUser(HttpServletRequest request, @RequestBody UpdateMembershipToUserRequest updateMembershipToUserRequest) {
        String userId = request.getAttribute(USER_ID_ATTR).toString();
        return this.membershipService.updateMembershipToUser(userId, updateMembershipToUserRequest);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ChangeMembershipResponse removeMembershipToUser(HttpServletRequest request) {
        String userId = request.getAttribute(USER_ID_ATTR).toString();
        return this.membershipService.removeMembershipToUser(userId);
    }

}
