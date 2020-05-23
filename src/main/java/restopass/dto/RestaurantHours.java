package restopass.dto;

import java.time.DayOfWeek;
import java.util.List;

public class RestaurantHours {

    private List<DayOfWeek> openingDays;
    private String openingHour;
    private String closingHour;

    public RestaurantHours(List<DayOfWeek> openingDays, String openingHour, String closingHour) {
        this.openingDays = openingDays;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
    }

    public List<DayOfWeek> getOpeningDays() {
        return openingDays;
    }

    public void setOpeningDays(List<DayOfWeek> openingDays) {
        this.openingDays = openingDays;
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(String openingHour) {
        this.openingHour = openingHour;
    }

    public String getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(String closingHour) {
        this.closingHour = closingHour;
    }
}
