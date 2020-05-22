package restopass.dto.response;

import restopass.dto.Membership;

import java.util.List;

public class MembershipsResponse {

    private Membership actualMembership;
    private List<Membership> memberships;

    public Membership getActualMembership() {
        return actualMembership;
    }

    public void setActualMembership(Membership actualMembership) {
        this.actualMembership = actualMembership;
    }

    public List<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<Membership> memberships) {
        this.memberships = memberships;
    }
}
