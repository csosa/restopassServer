package restopass.dto;

import java.time.LocalDateTime;

public class RestaurantComment {
    private String commentId;
    private String userId;
    private String dishId;
    private Integer dishStars;
    private Integer restaurantStars;
    private String description;
    private LocalDateTime date;

    public RestaurantComment(String commentId, String userId, String dishId, Integer dishStars, Integer restaurantStars, String description) {
        this.commentId = commentId;
        this.userId = userId;
        this.dishId = dishId;
        this.dishStars = dishStars;
        this.restaurantStars = restaurantStars;
        this.description = description;
        this.date = LocalDateTime.now();
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Integer getRestaurantStars() {
        return restaurantStars;
    }

    public void setRestaurantStars(Integer restaurantStars) {
        this.restaurantStars = restaurantStars;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
