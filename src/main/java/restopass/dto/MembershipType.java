package restopass.dto;

public enum MembershipType {

    BASIC("Básica"),GOLD("Oro"),PLATINUM("Platinum");

    private String name;

    MembershipType(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
