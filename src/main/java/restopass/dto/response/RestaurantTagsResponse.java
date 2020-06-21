package restopass.dto.response;

import restopass.dto.Membership;

import java.util.HashMap;
import java.util.List;

public class RestaurantTagsResponse {

    List<Membership> memberships;
    HashMap<String, List<String>> tags;

    public List<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<Membership> memberships) {
        this.memberships = memberships;
    }

    public HashMap<String, List<String>> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, List<String>> tags) {
        this.tags = tags;
    }
}
