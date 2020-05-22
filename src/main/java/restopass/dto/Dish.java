package restopass.dto;

public class Dish {

    private String name;
    private String description;
    private MembershipType topPlan;

    public Dish(){

    }

    public Dish(String name, String description, MembershipType topPlan) {
        this.name = name;
        this.description = description;
        this.topPlan = topPlan;
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

    public MembershipType getTopPlan() {
        return topPlan;
    }

    public void setTopPlan(MembershipType topPlan) {
        this.topPlan = topPlan;
    }
}
