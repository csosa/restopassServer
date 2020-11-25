package restopass.dto.response;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import restopass.dto.Dish;
import restopass.dto.Restaurant;
import restopass.dto.RestaurantHours;

import java.util.List;

public class RestaurantResponse {
    private String restaurantId;
    private String name;
    private String img;
    private String address;
    private GeoJsonPoint location;
    private List<RestaurantHours> timeTable;
    private List<String> tags;
    private List<Dish> dishes;
    private Float stars = 0f;
    private Integer hoursToCancel;
    private List<RestaurantCommentResponse> comments;

    public RestaurantResponse(Restaurant restaurant) {
        this.restaurantId = restaurant.getRestaurantId();
        this.name = restaurant.getName();
        this.img = restaurant.getImg();
        this.address = restaurant.getAddress();
        this.location = restaurant.getLocation();
        this.timeTable = restaurant.getTimeTable();
        this.tags = restaurant.getTags();
        this.dishes = restaurant.getDishes();
        this.stars = restaurant.getStars() / restaurant.getCountStars();
        this.hoursToCancel = restaurant.getHoursToCancel();
    }

    public void setComments(List<RestaurantCommentResponse> comments) {
        this.comments = comments;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public GeoJsonPoint getLocation() {
        return location;
    }

    public void setLocation(GeoJsonPoint location) {
        this.location = location;
    }

    public List<RestaurantHours> getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(List<RestaurantHours> timeTable) {
        this.timeTable = timeTable;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public Float getStars() {
        return stars;
    }

    public void setStars(Float stars) {
        this.stars = stars;
    }

    public Integer getHoursToCancel() {
        return hoursToCancel;
    }

    public void setHoursToCancel(Integer hoursToCancel) {
        this.hoursToCancel = hoursToCancel;
    }

    public List<RestaurantCommentResponse> getComments() {
        return comments;
    }
}
