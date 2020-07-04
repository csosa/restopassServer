package restopass.utils;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import restopass.exception.InvalidUsernameOrPasswordException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleLoginUtils {

    @Value("${google.api.key}")
    private static String googleApiKey;


    public static void verifyGoogleToken(String token) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(UrlFetchTransport.getDefaultInstance(), new JacksonFactory())
                .setAudience(Collections.singletonList(googleApiKey))
                .build();

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(token);

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = payload.getEmailVerified();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");
        } else {
            throw new InvalidUsernameOrPasswordException();
        }

        } catch (GeneralSecurityException | IOException e) {
                throw new InvalidUsernameOrPasswordException();
            }


    }
}
