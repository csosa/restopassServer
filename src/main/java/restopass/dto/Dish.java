package restopass.dto;

public class Dish {

    private String name;
    private String description;
    private String img;
    private Integer baseMembership;
    private String baseMembershipName;
    private Integer stars = 3;


    public Dish(){

    }

    public Dish(String name, String img, String description, MembershipType baseMembership) {
        this.name = name;
        this.img = img;
        this.description = description;
        this.baseMembership = baseMembership.ordinal();
        this.baseMembershipName = baseMembership.getName();
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

    public Integer getBaseMembership() {
        return baseMembership;
    }

    public void setBaseMembership(Integer baseMembership) {
        this.baseMembership = baseMembership;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBaseMembershipName() {
        return baseMembershipName;
    }

    public void setBaseMembershipName(String baseMembershipName) {
        this.baseMembershipName = baseMembershipName;
    }
}
