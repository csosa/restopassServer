package restopass.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import restopass.dto.User;
import restopass.dto.request.UserCreationRequest;
import restopass.dto.request.UserLoginRequest;
import restopass.dto.response.UserLoginResponse;
import restopass.exception.InvalidAccessOrRefreshTokenException;
import restopass.exception.InvalidUsernameOrPasswordException;
import restopass.exception.UserAlreadyExistsException;
import restopass.mongo.UserRepository;
import restopass.utils.JWTHelper;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {

    private static String EMAIL_FIELD = "email";
    private static String PASSWORD_FIELD = "password";
    private static String ACCESS_TOKEN_HEADER = "X-Auth-Token";
    private static String REFRESH_TOKEN_HEADER = "X-Refresh-Token";

    MongoTemplate mongoTemplate;
    UserRepository userRepository;

    @Autowired
    public UserService(MongoTemplate mongoTemplate, UserRepository userRepository) {
        this.mongoTemplate = mongoTemplate;
        this.userRepository = userRepository;
    }

    public UserLoginResponse loginUser(UserLoginRequest user) {
        Query query = new Query();
        query.addCriteria(Criteria.where(EMAIL_FIELD).is(user.getEmail()));
        query.addCriteria(Criteria.where(PASSWORD_FIELD).is(user.getPassword()));

        User userDTO = this.mongoTemplate.findOne(query, User.class);

        if(userDTO == null) {
            throw new InvalidUsernameOrPasswordException();
        }

        return this.buildAuthAndRefreshToken(userDTO);
    }

    public UserLoginResponse createUser(UserCreationRequest user) {
        User userDTO = new User(user.getEmail(), user.getPassword(), user.getName(), user.getLastName());
        try {
            userRepository.save(userDTO);
            return this.buildAuthAndRefreshToken(userDTO);
        } catch(DuplicateKeyException e) {
            throw new UserAlreadyExistsException();
        }
    }

    public UserLoginResponse refreshToken(HttpServletRequest req) {

        String oldAccessToken = req.getHeader(ACCESS_TOKEN_HEADER);
        String refreshAccessToken = req.getHeader(REFRESH_TOKEN_HEADER);

        String emailRefresh = JWTHelper.decodeJWT(refreshAccessToken).getId();
        User userDTO = this.findById(emailRefresh);

        try {
            Claims claims = JWTHelper.decodeJWT(oldAccessToken);
            if(claims.getId().equalsIgnoreCase(emailRefresh)) {
                return this.buildAuthAndRefreshToken(userDTO);
            } else {
                throw new InvalidAccessOrRefreshTokenException();
            }
        } catch (ExpiredJwtException e) {
            if(e.getClaims().getId().equalsIgnoreCase(emailRefresh)) {
                return this.buildAuthAndRefreshToken(userDTO);
            } else {
                throw new InvalidAccessOrRefreshTokenException();
            }
        } catch (Exception e) {
            throw new InvalidAccessOrRefreshTokenException();
        }
    }

    public User findById(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(EMAIL_FIELD).is(userId));

        return this.mongoTemplate.findOne(query, User.class);
    }

    private UserLoginResponse buildAuthAndRefreshToken(User user) {
        UserLoginResponse userResponse = new UserLoginResponse();
        userResponse.setxAuthToken(JWTHelper.createAccessToken(user.getEmail()));
        userResponse.setxRefreshToken(JWTHelper.createRefreshToken(user.getEmail()));
        userResponse.setUser(user);

        return userResponse;
    }

}
