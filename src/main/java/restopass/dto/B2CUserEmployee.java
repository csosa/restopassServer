package restopass.dto;

import java.util.HashMap;
import java.util.List;

public class B2CUserEmployee {

    private List<Float> percentageDiscountPerMembership;

    public B2CUserEmployee(List<Float> percentageDiscountPerMembership) {
        this.percentageDiscountPerMembership = percentageDiscountPerMembership;
    }

    public List<Float> getPercentageDiscountPerMembership() {
        return percentageDiscountPerMembership;
    }

    public void setPercentageDiscountPerMembership(List<Float> percentageDiscountPerMembership) {
        this.percentageDiscountPerMembership = percentageDiscountPerMembership;
    }
}
