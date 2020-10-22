package restopass.dto.request;

public class RestaurantCommentRequest {
    private String restaurantId;
    private Integer restaurantStars;
    private String dishId;
    private Integer dishStars;
    private String description;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getRestaurantStars() {
        return restaurantStars;
    }

    public void setRestaurantStars(Integer restaurantStars) {
        this.restaurantStars = restaurantStars;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public Integer getDishStars() {
        return dishStars;
    }

    public void setDishStars(Integer dishStars) {
        this.dishStars = dishStars;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
