package restopass.dto;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "memberships")
public class Membership {

    private MembershipType membershipId;
    private String description;
    private Double price;

    public MembershipType getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(MembershipType membershipId) {
        this.membershipId = membershipId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
