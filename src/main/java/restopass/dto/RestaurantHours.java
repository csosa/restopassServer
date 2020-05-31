package restopass.dto;

import java.time.DayOfWeek;
import java.util.List;

public class RestaurantHours {

    private List<DayOfWeek> openingDays;
    private List<PairHour> pairHours;

    public List<DayOfWeek> getOpeningDays() {
        return openingDays;
    }

    public void setOpeningDays(List<DayOfWeek> openingDays) {
        this.openingDays = openingDays;
    }

    public List<PairHour> getPairHours() {
        return pairHours;
    }

    public void setPairHours(List<PairHour> pairHours) {
        this.pairHours = pairHours;
    }
}
