package restopass.dto.response;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import restopass.dto.Dish;
import restopass.dto.Restaurant;
import restopass.dto.RestaurantHours;

import java.util.List;

public class RestaurantResponse {
    private String restaurantId;
    private String name;
    private String img;
    private String address;
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
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
        this.stars = restaurant.getStars();
        this.hoursToCancel = restaurant.getHoursToCancel();
    }

    public void setComments(List<RestaurantCommentResponse> comments) {
        this.comments = comments;
    }
}
