package restopass.dto.response;

import java.time.LocalDateTime;

public class ChangeMembershipResponse {
    private LocalDateTime changedDate;

    public ChangeMembershipResponse(LocalDateTime changedDate) {
        this.changedDate = changedDate;
    }

    public LocalDateTime getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(LocalDateTime changedDate) {
        this.changedDate = changedDate;
    }
}
