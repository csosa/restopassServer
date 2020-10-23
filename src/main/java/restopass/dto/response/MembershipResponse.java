package restopass.dto.response;

import restopass.dto.Membership;

import java.util.List;

public class MembershipResponse {

    private Membership membershipInfo;
    private List<RestaurantResponse> restaurants;

    public List<RestaurantResponse> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<RestaurantResponse> restaurants) {
        this.restaurants = restaurants;
    }

    public Membership getMembershipInfo() {
        return membershipInfo;
    }

    public void setMembershipInfo(Membership membershipInfo) {
        this.membershipInfo = membershipInfo;
    }
}
