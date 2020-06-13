package restopass.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restopass.client.FirebaseClient;
import restopass.dto.firebase.SimplePush;

import java.lang.reflect.Type;

@Service
public class FirebaseService {
    @Autowired
    private FirebaseClient firebaseClient;

    public void sendNotification(SimplePush notification) {
        firebaseClient.sendNotification(notification);
    }
}
