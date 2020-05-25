package restopass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import restopass.dto.Membership;
import restopass.dto.request.UpdateMembershipToUserRequest;
import restopass.dto.response.MembershipsResponse;
import restopass.dto.User;
import restopass.exception.InvalidUsernameOrPasswordException;
import restopass.mongo.MembershipRepository;

import java.util.List;

@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final MongoTemplate mongoTemplate;

    private UserService userService;

    @Autowired
    public MembershipService(MembershipRepository membershipRepository, MongoTemplate mongoTemplate, UserService userService) {
        this.membershipRepository = membershipRepository;
        this.mongoTemplate = mongoTemplate;
        this.userService = userService;
    }

    public void createMembership(Membership membership) {
        this.membershipRepository.save(membership);
    }

    public MembershipsResponse getMemberships(String userId) {
        MembershipsResponse membershipsResponse = new MembershipsResponse();

        User user = this.userService.findById(userId);
        List<Membership> memberships = this.membershipRepository.findAll();

        if(user != null && user.getActualMembership() != null) {
            Membership actualMembership = memberships.stream().filter(m -> m.getMembershipId().equals(user.getActualMembership())).findAny().get();
            membershipsResponse.setActualMembership(actualMembership);
            memberships.remove(actualMembership);
        }

        membershipsResponse.setMemberships(memberships);

        return membershipsResponse;
    }

    public void updateMembershipToUser(String userId, UpdateMembershipToUserRequest request){
        User user = this.userService.findById(userId);

        if(user == null) {
            throw new InvalidUsernameOrPasswordException();
        }

        this.userService.updateMembership(userId, request.getMembershipId().name());
    }
}
