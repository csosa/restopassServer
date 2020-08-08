package restopass.dto.response;

public class QRData {
    private String reservationId;
    private String userId;

    public QRData(String reservationId, String userId) {
        this.reservationId = reservationId;
        this.userId = userId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
