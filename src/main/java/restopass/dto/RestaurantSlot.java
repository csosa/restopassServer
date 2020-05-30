package restopass.dto;

import java.time.LocalDateTime;
import java.util.List;

public class RestaurantSlot {

    private List<LocalDateTime> dateTime;
    private Integer tablesAvailable;
    private Boolean isDayFull = false;

    public List<LocalDateTime> getDateTime() {
        return dateTime;
    }

    public void setDateTime(List<LocalDateTime> dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getTablesAvailable() {
        return tablesAvailable;
    }

    public void setTablesAvailable(Integer tablesAvailable) {
        this.tablesAvailable = tablesAvailable;
    }

    public Boolean getDayFull() {
        return isDayFull;
    }

    public void setDayFull(Boolean dayFull) {
        isDayFull = dayFull;
    }

    public RestaurantSlot() {
    }


}
