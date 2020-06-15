package restopass.dto;

public enum MembershipType {
    BASIC("Básica"),GOLD("Oro"),PLATINUM("Platinum");

    MembershipType(String name) {
    }

    public String getName(){
        return this.name();
    }
}
