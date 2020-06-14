package restopass.dto.firebase;

public class SimpleTopicPush {
    private String to;
    private SimplePushData data;
    private SimplePushNotif notification;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = "/topics/" + to;
    }

    public void setData(SimplePushData data) {
        this.data = data;
    }

    public SimplePushData getData() {
        return data;
    }

    public SimplePushNotif getNotification() {
        return notification;
    }

    public void setNotification(SimplePushNotif notification) {
        this.notification = notification;
    }
}
