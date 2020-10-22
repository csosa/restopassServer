package restopass.dto.response;

import restopass.dto.Dish;
import restopass.dto.User;

import java.time.LocalDateTime;

public class RestaurantCommentResponse {
    private String commentId;
    private User user;
    private Dish dish;
    private Integer dishStars;
    private Integer restaurantStars;
    private String description;
    private LocalDateTime date;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
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
