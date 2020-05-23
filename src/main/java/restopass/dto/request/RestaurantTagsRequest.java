package restopass.dto.request;

import restopass.dto.Membership;
import restopass.dto.MembershipType;

import java.util.List;

public class RestaurantTagsRequest {

    private List<String> tags;
    private MembershipType topMembership;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public MembershipType getTopMembership() {
        return topMembership;
    }

    public void setTopMembership(MembershipType topMembership) {
        this.topMembership = topMembership;
    }
}
