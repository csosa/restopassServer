package restopass.dto;

public class Dish {

    private String name;
    private String description;
    private MembershipType topMembership;
    private Integer stars = 3;

    public Dish(){

    }

    public Dish(String name, String description, MembershipType topMembership) {
        this.name = name;
        this.description = description;
        this.topMembership = topMembership;
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

    public MembershipType getTopMembership() {
        return topMembership;
    }

    public void setTopMembership(MembershipType topMembership) {
        this.topMembership = topMembership;
    }
}
