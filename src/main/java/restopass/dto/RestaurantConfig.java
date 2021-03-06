package restopass.dto;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "restaurant_configs")
public class RestaurantConfig {

    private String restaurantId;
    private Integer tablesPerShift;
    private Integer minutesGap;
    private Integer maxDiners;
    private List<RestaurantSlot> slots;


    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getTablesPerShift() {
        return tablesPerShift;
    }

    public void setTablesPerShift(Integer tablesPerShift) {
        this.tablesPerShift = tablesPerShift;
    }

    public Integer getMinutesGap() {
        return minutesGap;
    }

    public void setMinutesGap(Integer minutesGap) {
        this.minutesGap = minutesGap;
    }

    public Integer getMaxDiners() {
        return maxDiners;
    }

    public void setMaxDiners(Integer maxDiners) {
        this.maxDiners = maxDiners;
    }

    public List<RestaurantSlot> getSlots() {
        return slots;
    }

    public void setSlots(List<RestaurantSlot> slots) {
        this.slots = slots;
    }
}
