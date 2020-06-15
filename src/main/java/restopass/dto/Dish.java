package restopass.dto;

import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

public class Dish {

    private String name;
    private String description;
    private String img;
    private Integer topMembership;
    private String topMembershipName;
    private Integer stars = 3;


    public Dish(){

    }

    public Dish(String name, String img, String description, MembershipType topMembership) {
        this.name = name;
        this.img = img;
        this.description = description;
        this.topMembership = topMembership.ordinal();
        this.topMembershipName = topMembership.getName();
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTopMembership() {
        return topMembership;
    }

    public void setTopMembership(Integer topMembership) {
        this.topMembership = topMembership;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTopMembershipName() {
        return topMembershipName;
    }

    public void setTopMembershipName(String topMembershipName) {
        this.topMembershipName = topMembershipName;
    }
}
