package restopass.dto.request;

import restopass.dto.MembershipType;

public class DishRequest {
    String name;
    String img;
    String description;
    MembershipType topMembership;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MembershipType getTopMembership() {
        return topMembership;
    }

    public void setTopMembership(MembershipType topMembership) {
        this.topMembership = topMembership;
    }
}
