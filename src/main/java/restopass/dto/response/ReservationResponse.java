package restopass.dto.response;

import restopass.dto.Membership;
import restopass.dto.ReservationState;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationResponse {

    private String reservationId;
    private String restaurantId;
    private LocalDateTime date;
    private ReservationState state = ReservationState.CONFIRMED;
    private UserReservation ownerUser;
    private String qrBase64;
    private List<UserReservation> confirmedUsers;
    private List<UserReservation> toConfirmUsers;
    private String restaurantName;
    private String restaurantAddress;
    private String img;
    private Integer dinners;
    private Boolean isInvitation = false;
    private Membership minMembershipRequired;

    public Integer getDinners() {
        return dinners;
    }

    public void setDinners(Integer dinners) {
        this.dinners = dinners;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Boolean getInvitation() {
        return isInvitation;
    }

    public void setInvitation(Boolean invitation) {
        isInvitation = invitation;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public ReservationState getState() {
        return state;
    }

    public void setState(ReservationState state) {
        this.state = state;
    }

    public UserReservation getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(UserReservation ownerUser) {
        this.ownerUser = ownerUser;
    }

    public String getQrBase64() {
        return qrBase64;
    }

    public void setQrBase64(String qrBase64) {
        this.qrBase64 = qrBase64;
    }

    public List<UserReservation> getConfirmedUsers() {
        return confirmedUsers;
    }

    public void setConfirmedUsers(List<UserReservation> confirmedUsers) {
        this.confirmedUsers = confirmedUsers;
    }

    public List<UserReservation> getToConfirmUsers() {
        return toConfirmUsers;
    }

    public void setToConfirmUsers(List<UserReservation> toConfirmUsers) {
        this.toConfirmUsers = toConfirmUsers;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public Membership getMinMembershipRequired() {
        return minMembershipRequired;
    }

    public void setMinMembershipRequired(Membership minMembershipRequired) {
        this.minMembershipRequired = minMembershipRequired;
    }
}
