package restopass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import restopass.dto.Membership;
import restopass.dto.response.ChangeMembershipResponse;
import restopass.dto.response.MembershipResponse;
import restopass.dto.request.UpdateMembershipToUserRequest;
import restopass.dto.response.MembershipsResponse;
import restopass.dto.User;
import restopass.exception.UserNotFoundException;
import restopass.mongo.MembershipRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembershipService {

    private final String ID = "membershipId";

    private final MembershipRepository membershipRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    private RestaurantService restaurantService;

    private UserService userService;

    @Autowired
    public MembershipService(MembershipRepository membershipRepository, MongoTemplate mongoTemplate,
                             UserService userService) {
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
            MembershipResponse mr = new MembershipResponse();
            mr.setMembershipInfo(actualMembership);
            mr.setRestaurants(this.restaurantService.getRestaurantInAMemberships(actualMembership.getMembershipId()));
            membershipsResponse.setActualMembership(mr);
            memberships.remove(actualMembership);
        }

        membershipsResponse.setMemberships(memberships.stream().map(m -> {
            MembershipResponse mr = new MembershipResponse();
            mr.setMembershipInfo(m);
            mr.setRestaurants(this.restaurantService.getRestaurantInAMemberships(m.getMembershipId()));
            return mr;
        }).collect(Collectors.toList()));

        return membershipsResponse;
    }

    public ChangeMembershipResponse updateMembershipToUser(String userId, UpdateMembershipToUserRequest request){
        User user = this.userService.findById(userId);

        if(user == null) {
            throw new UserNotFoundException();
        }

        Query query = new Query();

        query.addCriteria(Criteria.where(ID).is(request.getMembershipId()));
        Membership membership = this.mongoTemplate.findOne(query, Membership.class);

        return new ChangeMembershipResponse(this.userService.updateMembership(userId, membership));
    }

    public ChangeMembershipResponse removeMembershipToUser(String userId){
        User user = this.userService.findById(userId);

        if(user == null) {
            throw new UserNotFoundException();
        }

       return new ChangeMembershipResponse(this.userService.removeMembership(userId));
    }

    public List<Membership> findAll() {
        return this.membershipRepository.findAll();
    }
}
