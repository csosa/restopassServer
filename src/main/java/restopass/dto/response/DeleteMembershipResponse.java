package restopass.dto.response;

import java.time.LocalDateTime;

public class DeleteMembershipResponse {
    private LocalDateTime membershipFinalizeDate;

    public DeleteMembershipResponse(LocalDateTime membershipFinalizeDate) {
        this.membershipFinalizeDate = membershipFinalizeDate;
    }

    public LocalDateTime getMembershipFinalizeDate() {
        return membershipFinalizeDate;
    }

    public void setMembershipFinalizeDate(LocalDateTime membershipFinalizeDate) {
        this.membershipFinalizeDate = membershipFinalizeDate;
    }
}
