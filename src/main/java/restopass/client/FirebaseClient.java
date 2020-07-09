package restopass.client;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import restopass.dto.firebase.ReservationPushNotifData;
import restopass.dto.firebase.ScorePushNotifData;
import restopass.dto.firebase.SimpleTopicPush;
import restopass.exception.ErrorUploadingImgFirebaseException;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseClient extends DefaultRestConnector {

    private final String FCM_SEND_NOTIFICATION = "https://fcm.googleapis.com/fcm/send";
    private final String IMAGE_BUCKET_URL = "https://firebasestorage.googleapis.com/v0/b/restopass-d70b2.appspot.com/o/{}?alt=media";
    @Value("${firebase.api.key}")
    private String FIREBASE_API_KEY;
    @Value("${firebase.bucket.name}")
    private String FIREBASE_BUCKET;
    @Value("${google.bucket.credentials.path}")
    private String GOOGLE_CREDENTIALS_PATH;
    private Bucket bucketStorage;

    @PostConstruct
    public void init() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(GOOGLE_CREDENTIALS_PATH);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(FIREBASE_BUCKET)
                .build();

        FirebaseApp.initializeApp(options);

        bucketStorage = StorageClient.getInstance().bucket();
    }

    public void sendReservationNotification(SimpleTopicPush<ReservationPushNotifData> notification) {
        this.doPost(FCM_SEND_NOTIFICATION, this.buildHeaders(), notification, new TypeToken<SimpleTopicPush<ReservationPushNotifData>>() {
        }.getType());
    }

    public void sendScoreNotification(SimpleTopicPush<ScorePushNotifData> notification) {
        this.doPost(FCM_SEND_NOTIFICATION, this.buildHeaders(), notification, new TypeToken<SimpleTopicPush<ScorePushNotifData>>() {
        }.getType());
    }

    public String createImageFromURL(String url, String fileName, String folderName) {
        try {
            URLConnection connection = new URL(url).openConnection();
            InputStream input = connection.getInputStream();
            String contentType = connection.getContentType();
            String name = fileName + "." + contentType.split("/")[1];
            String path = folderName + "/" + name;
            bucketStorage.create(path, input, contentType, Bucket.BlobWriteOption.userProject("restopass-d70b2"));
            return this.buildImgURL(path);
        } catch (IOException e) {
            throw new ErrorUploadingImgFirebaseException(fileName);
        }
    }

    private String buildImgURL(String path) {
        path = path.replace("/", "%2F");
        return IMAGE_BUCKET_URL.replace("{}", path);
    }

    private Map<String, String> buildHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", FIREBASE_API_KEY);
        return headers;
    }
}
