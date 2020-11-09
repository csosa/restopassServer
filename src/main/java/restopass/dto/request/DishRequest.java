package restopass.dto.request;

import restopass.dto.MembershipType;

import java.util.List;

public class DishRequest {
    String name;
    String img;
    String description;
    MembershipType baseMembership;
    List<String> tags;

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

    public MembershipType getBaseMembership() {
        return baseMembership;
    }

    public void setBaseMembership(MembershipType baseMembership) {
        this.baseMembership = baseMembership;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
