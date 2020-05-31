package restopass.dto;

import java.time.LocalDateTime;

public class DateTimeWithTables {

    private LocalDateTime dateTime;
    private Integer tablesAvailable;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getTablesAvailable() {
        return tablesAvailable;
    }

    public void setTablesAvailable(Integer tablesAvailable) {
        this.tablesAvailable = tablesAvailable;
    }
}
