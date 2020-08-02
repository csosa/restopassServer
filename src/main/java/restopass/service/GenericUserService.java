package restopass.service;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import restopass.dto.User;
import restopass.exception.InvalidUsernameOrPasswordException;
import restopass.dto.request.UserLoginRequest;
import restopass.dto.response.UserLoginResponse;
import restopass.utils.JWTHelper;

import javax.servlet.http.HttpServletRequest;


@Service
public abstract class GenericUserService {

    private static String EMAIL_FIELD = "email";
    private static String PASSWORD_FIELD = "password";
    private static String ACCESS_TOKEN_HEADER = "X-Auth-Token";
    private static String REFRESH_TOKEN_HEADER = "X-Refresh-Token";

    public <T> UserLoginResponse<T> loginUser(UserLoginRequest user) {
        Query query = new Query();
        query.addCriteria(Criteria.where(EMAIL_FIELD).is(user.getEmail()));
        query.addCriteria(Criteria.where(PASSWORD_FIELD).is(user.getPassword()));

        if (this.findByUserAndPass(query) == null) {
            throw new InvalidUsernameOrPasswordException();
        }

        return JWTHelper.buildUserLoginResponse(this.findByUserAndPass(query), user.getEmail(),false);
    }

    public <T> UserLoginResponse<T> refreshToken(HttpServletRequest req) {
        String refreshAccessToken = req.getHeader(REFRESH_TOKEN_HEADER);
        String oldAccessToken = req.getHeader(ACCESS_TOKEN_HEADER);

        String emailRefresh = JWTHelper.decodeJWT(refreshAccessToken).getId();

        return JWTHelper.refreshToken(oldAccessToken, emailRefresh, this.findById(emailRefresh));
    }

    public abstract <T> T findByUserAndPass(Query query);
    public abstract <T> T findById(String id);
}
