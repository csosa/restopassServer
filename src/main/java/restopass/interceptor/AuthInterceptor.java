package restopass.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import restopass.exception.ExpiredAccessTokenException;
import restopass.exception.InvalidAccessOrRefreshTokenException;
import restopass.utils.JWTHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static String ACCESS_TOKEN_HEADER = "X-Auth-Token";
    private static String USER_ID_ATTR = "userId";

    @Override
    public boolean preHandle
            (HttpServletRequest request, HttpServletResponse response, Object handler) {

        String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);

        if(Strings.isBlank(accessToken)) {
            //throw new AccessTokenRequiredException();
            request.setAttribute(USER_ID_ATTR, request.getHeader(USER_ID_ATTR));
            return true;
        }

        try {
            Claims tokenDecoded = JWTHelper.decodeJWT(accessToken);
            request.setAttribute(USER_ID_ATTR, tokenDecoded.getId());
        }catch (ExpiredJwtException e) {
            throw new ExpiredAccessTokenException();
        }catch (Exception e) {
            throw new InvalidAccessOrRefreshTokenException();
        }

        return true;
    }


}