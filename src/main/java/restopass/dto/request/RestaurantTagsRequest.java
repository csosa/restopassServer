package restopass.dto.request;

import restopass.dto.Membership;

import java.util.List;

public class RestaurantTagsRequest {

    private List<String> tags;
    private Membership topMembership;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Membership getTopMembership() {
        return topMembership;
    }

    public void setTopMembership(Membership topMembership) {
        this.topMembership = topMembership;
    }
}
