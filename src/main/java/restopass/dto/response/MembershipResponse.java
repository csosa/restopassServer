package restopass.dto.response;

import restopass.dto.Membership;
import restopass.dto.Restaurant;

import java.util.List;

public class MembershipResponse {

    private Membership membershipInfo;
    private List<Restaurant> restaurants;

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public Membership getMembershipInfo() {
        return membershipInfo;
    }

    public void setMembershipInfo(Membership membershipInfo) {
        this.membershipInfo = membershipInfo;
    }
}
