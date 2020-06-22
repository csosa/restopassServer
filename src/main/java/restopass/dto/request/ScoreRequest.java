package restopass.dto.request;

public class ScoreRequest {
    private String restaurantId;
    private String dishId;
    private Integer starsRestaurant;
    private Integer starsDish;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public Integer getStarsRestaurant() {
        return starsRestaurant;
    }

    public void setStarsRestaurant(Integer starsRestaurant) {
        this.starsRestaurant = starsRestaurant;
    }

    public Integer getStarsDish() {
        return starsDish;
    }

    public void setStarsDish(Integer starsDish) {
        this.starsDish = starsDish;
    }
}
