package restopass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restopass.client.FirebaseClient;
import restopass.dto.firebase.SimpleTopicPush;

@Service
public class FirebaseService {
    @Autowired
    private FirebaseClient firebaseClient;

    public void sendNotification(SimpleTopicPush notification) {
        firebaseClient.sendNotification(notification);
    }
}
