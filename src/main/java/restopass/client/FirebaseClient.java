package restopass.client;

import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import restopass.dto.firebase.ReservationPushNotifData;
import restopass.dto.firebase.ScorePushNotifData;
import restopass.dto.firebase.SimpleTopicPush;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseClient extends DefaultRestConnector {

    private final String FCM_SEND_NOTIFICATION = "https://fcm.googleapis.com/fcm/send";

    @Value("${firebase.api.key}")
    private String FIREBASE_API_KEY;

    public void sendReservationNotification(SimpleTopicPush<ReservationPushNotifData> notification) {
        this.doPost(FCM_SEND_NOTIFICATION, this.buildHeaders(), notification, new TypeToken<SimpleTopicPush<ReservationPushNotifData>>() {}.getType());
    }

    public void sendScoreNotification(SimpleTopicPush<ScorePushNotifData> notification) {
        this.doPost(FCM_SEND_NOTIFICATION, this.buildHeaders(), notification, new TypeToken<SimpleTopicPush<ScorePushNotifData>>() {}.getType());
    }

    public Map<String, String> buildHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", FIREBASE_API_KEY);
        return headers;
    }
}
