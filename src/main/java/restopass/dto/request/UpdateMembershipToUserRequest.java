package restopass.dto.request;

import restopass.dto.MembershipType;

public class UpdateMembershipToUserRequest {

    MembershipType membershipId;

    public MembershipType getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(MembershipType membershipId) {
        this.membershipId = membershipId;
    }
}
