package restopass.dto.request;

import java.util.List;

public class CreateReservationRequest {
    private String restaurantId;
    private String date;
    private List<String> toConfirmUsers;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getToConfirmUsers() {
        return toConfirmUsers;
    }

    public void setToConfirmUsers(List<String> toConfirmUsers) {
        this.toConfirmUsers = toConfirmUsers;
    }
}
