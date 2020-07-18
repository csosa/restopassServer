package restopass.dto;

import java.util.HashMap;

public class B2CUserEmployee {

    private HashMap<Integer, Float> percentageDiscountPerMembership;

    public B2CUserEmployee(HashMap<Integer, Float> percentageDiscountPerMembership) {
        this.percentageDiscountPerMembership = percentageDiscountPerMembership;
    }

    public HashMap<Integer, Float> getPercentageDiscountPerMembership() {
        return percentageDiscountPerMembership;
    }

    public void setPercentageDiscountPerMembership(HashMap<Integer, Float> percentageDiscountPerMembership) {
        this.percentageDiscountPerMembership = percentageDiscountPerMembership;
    }
}
