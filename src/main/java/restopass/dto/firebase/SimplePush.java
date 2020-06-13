package restopass.dto.firebase;

public class SimplePush {
    private String topic;
    private SimplePushData data;
    private SimplePushNotif notification;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public SimplePushData getData() {
        return data;
    }

    public void setData(SimplePushData data) {
        this.data = data;
    }

    public SimplePushNotif getNotification() {
        return notification;
    }

    public void setNotification(SimplePushNotif notification) {
        this.notification = notification;
    }
}
