package restopass.dto.firebase;

public class ReservationPushNotifData extends SimpleNotifData{
    private String type;
    private String reservationId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }
}
