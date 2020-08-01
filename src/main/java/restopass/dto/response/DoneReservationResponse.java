package restopass.dto.response;

import restopass.dto.Dish;

import java.util.List;
import java.util.Map;

public class DoneReservationResponse {
    private String reservationId;
    private String ownerUserName;
    private Integer dinners;
    private String date;
    Map<String, List<Dish>> dishesPerMembership;
    Map<String, Long> dinnersPerMembership;

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    public Integer getDinners() {
        return dinners;
    }

    public void setDinners(Integer dinners) {
        this.dinners = dinners;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, List<Dish>> getDishesPerMembership() {
        return dishesPerMembership;
    }

    public void setDishesPerMembership(Map<String, List<Dish>> dishesPerMembership) {
        this.dishesPerMembership = dishesPerMembership;
    }

    public Map<String, Long> getDinnersPerMembership() {
        return dinnersPerMembership;
    }

    public void setDinnersPerMembership(Map<String, Long> dinnersPerMembership) {
        this.dinnersPerMembership = dinnersPerMembership;
    }
}
