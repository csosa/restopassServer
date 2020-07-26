package restopass.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import restopass.dto.User;
import restopass.exception.NotValidGoogleLoginException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleService {

    @Value("${google.api.key}")
    private String googleApiKey;
    private GoogleIdTokenVerifier verifier;

    @PostConstruct
    private void init() {
        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(googleApiKey))
                .setIssuer("https://accounts.google.com")
                .build();
    }

    public User verifyGoogleToken(String token) {
        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(token);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");

                User user = new User();
                user.setName(givenName);
                user.setLastName(familyName);
                user.setEmail(email);

                return user;

            } else {
                throw new NotValidGoogleLoginException();
            }

        } catch (GeneralSecurityException | IOException e) {
            throw new NotValidGoogleLoginException();
        }


    }
}
