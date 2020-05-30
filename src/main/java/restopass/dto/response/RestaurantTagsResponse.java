package restopass.dto.response;

import restopass.dto.MembershipType;

import java.util.HashMap;
import java.util.List;

public class RestaurantTagsResponse {

    List<MembershipType> memberships;
    HashMap<String, List<String>> tags;

    public List<MembershipType> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<MembershipType> memberships) {
        this.memberships = memberships;
    }

    public HashMap<String, List<String>> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, List<String>> tags) {
        this.tags = tags;
    }
}
