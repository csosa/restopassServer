package restopass.utils;


import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

import io.jsonwebtoken.*;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import restopass.dto.UserDTO;

@Service
public class JWTHelper {

    private static String SECRET_KEY = "PRUEBA";
    private static long TTL_MILLIS = 60000;

    public static String createAccessToken(String userEmail) {
        return createJWT(userEmail, true);
    }

    public static String createRefreshToken(String userEmail) {
        return createJWT(userEmail, false);
    }

    private static String createJWT(String userEmail, boolean isAccessToken) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(userEmail)
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (TTL_MILLIS > 0 && isAccessToken) {
            long expMillis = nowMillis + TTL_MILLIS;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
}
