package restopass.service;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import restopass.dto.UserDTO;
import restopass.dto.request.UserCreationRequest;
import restopass.dto.request.UserLoginRequest;
import restopass.exception.InvalidAccessOrRefreshTokenException;
import restopass.exception.InvalidUsernameOrPasswordException;
import restopass.exception.UserAlreadyExistsException;
import restopass.mongo.UsersRepository;
import restopass.utils.JWTHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

    private static String EMAIL_FIELD = "email";
    private static String PASSWORD_FIELD = "password";
    private static String ACCESS_TOKEN_HEADER = "access-token";
    private static String REFRESH_TOKEN_HEADER = "refresh-token";

    MongoTemplate mongoTemplate;
    UsersRepository usersRepository;

    @Autowired
    public UserService(MongoTemplate mongoTemplate, UsersRepository usersRepository) {
        this.mongoTemplate = mongoTemplate;
        this.usersRepository = usersRepository;
    }

    public void loginUser(UserLoginRequest user, HttpServletResponse response) {
        Query query = new Query();
        query.addCriteria(Criteria.where(EMAIL_FIELD).is(user.getEmail()));
        query.addCriteria(Criteria.where(PASSWORD_FIELD).is(user.getPassword()));

        UserDTO userDTO = this.mongoTemplate.findOne(query, UserDTO.class);

        if(userDTO == null) {
            throw new InvalidUsernameOrPasswordException();
        }

        response.setHeader(ACCESS_TOKEN_HEADER, JWTHelper.createAccessToken(userDTO.getEmail()));
        response.setHeader(REFRESH_TOKEN_HEADER, JWTHelper.createRefreshToken(userDTO.getEmail()));

    }

    public void createUser(UserCreationRequest user) {
        UserDTO userDTO = new UserDTO(user.getEmail(), user.getPassword(), user.getName(), user.getLastName());
        try {
            usersRepository.save(userDTO);
        } catch(DuplicateKeyException e) {
            throw new UserAlreadyExistsException();
        }
    }

    public void refreshToken(HttpServletRequest req, HttpServletResponse res) {

        String oldAccessToken = req.getHeader(ACCESS_TOKEN_HEADER);
        String refreshAccessToken = req.getHeader(REFRESH_TOKEN_HEADER);

        try {
            JWTHelper.decodeJWT(oldAccessToken);
        } catch (ExpiredJwtException e) {
            String emailRefresh = JWTHelper.decodeJWT(refreshAccessToken).getId();

            if(e.getClaims().getId().equalsIgnoreCase(emailRefresh)) {
                res.setHeader(ACCESS_TOKEN_HEADER, JWTHelper.createAccessToken(emailRefresh));
                res.setHeader(REFRESH_TOKEN_HEADER, JWTHelper.createRefreshToken(emailRefresh));
            }
        } catch (Exception e) {
            throw new InvalidAccessOrRefreshTokenException();
        }
    }


}
