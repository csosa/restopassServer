package restopass.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import restopass.dto.B2BUserEmployer;
import restopass.dto.EmailModel;
import restopass.dto.User;
import restopass.dto.request.UserCreationRequest;
import restopass.dto.request.UserLoginGoogleRequest;
import restopass.dto.request.UserLoginRequest;
import restopass.dto.request.UserUpdateRequest;
import restopass.dto.response.UserLoginResponse;
import restopass.exception.*;
import restopass.mongo.UserRepository;
import restopass.utils.EmailSender;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    @MockBean
    private B2BUserService b2BUserService;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GoogleService googleService;

    @MockBean
    private ReservationService reservationService;

    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private UserService userService;

    public static final String USER_ID = "prueba@prueba.com";

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loginUserOK(){

        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(getUser());

        UserLoginRequest request = new UserLoginRequest();
        request.setPassword("password");
        request.setEmail(USER_ID);

        UserLoginResponse<User> loginResponse = userService.loginUser(request);

        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getxAuthToken());
        assertNotNull(loginResponse.getxRefreshToken());
        assertFalse(loginResponse.isCreation());
    }

    @Test
    public void loginUser_throwInvalidUsernameOrPasswordException(){

        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(null);

        UserLoginRequest request = new UserLoginRequest();
        request.setPassword("password");
        request.setEmail("mario@prueba.com");

        assertThatThrownBy(() -> userService.loginUser(request)).isInstanceOf(InvalidUsernameOrPasswordException.class);
    }

    @Test
    public void loginNewGoogleUserOK(){

        when(googleService.verifyGoogleToken(anyString())).thenReturn(getUser());
        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(null);

        UserLoginGoogleRequest request = new UserLoginGoogleRequest();
        request.setGoogleToken("token");

        UserLoginResponse<User> loginResponse = userService.loginGoogleUser(request);

        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getxAuthToken());
        assertNotNull(loginResponse.getxRefreshToken());
        assertTrue(loginResponse.isCreation());
    }

    @Test
    public void loginExistentGoogleUserOK(){

        when(googleService.verifyGoogleToken(anyString())).thenReturn(getUser());
        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(getUser());

        UserLoginGoogleRequest request = new UserLoginGoogleRequest();
        request.setGoogleToken("token");

        UserLoginResponse<User> loginResponse = userService.loginGoogleUser(request);

        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getxAuthToken());
        assertNotNull(loginResponse.getxRefreshToken());
        assertFalse(loginResponse.isCreation());
    }

    @Test
    public void createUserOK(){

        when(b2BUserService.checkIfB2BUser(anyString())).thenReturn(getB2BUser());

        UserCreationRequest request = new UserCreationRequest();
        request.setEmail(USER_ID);
        request.setPassword("password");
        request.setName("Jose");
        request.setLastName("Perez");

        UserLoginResponse<User> loginResponse = userService.createUser(request);

        assertNotNull(loginResponse);
        assertEquals("Jose", loginResponse.getUser().getName());
    }

    @Test
    public void createUser_ThrowUserAlreadyExistsException(){

        when(b2BUserService.checkIfB2BUser(anyString())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenThrow(DuplicateKeyException.class);

        UserCreationRequest request = new UserCreationRequest();
        request.setEmail(USER_ID);
        request.setPassword("password");
        request.setName("Jose");
        request.setLastName("Perez");

        assertThatThrownBy(() -> userService.createUser(request)).isInstanceOf(UserAlreadyExistsException.class);
    }

    //TODO chequear
    @Test
    public void deleteUserOK(){

        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(null);

        userService.deleteUser("hola@prueba.com", "password");
    }

    //TODO chequear
    @Test
    public void deleteUser_throwDeleteUserBadPasswordException(){

        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(getUser());
        assertThatThrownBy(() -> userService.deleteUser(USER_ID, "password")).isInstanceOf(DeleteUserBadPasswordException.class);

    }

    @Test
    public void updateUserInfoOK(){

        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("Jose");
        request.setLastName("Perez");
        request.setToConfirmEmail("jose@test.com");

        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(getUser(), null);
        Mockito.doNothing().when(emailSender).sendEmail(any(EmailModel.class));

        userService.updateUserInfo(request, USER_ID);
    }

    @Test
    public void recoverPasswordOK(){

        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(getUser());
        Mockito.doNothing().when(emailSender).sendEmail(any(EmailModel.class));

        userService.recoverPassword(USER_ID);
    }

    @Test
    public void recoverPassword_throwUserNotFoundException(){

        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(null);

        assertThatThrownBy(() -> userService.recoverPassword(USER_ID)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void verifyRecoverPassword_throwUnequalRecoverPasswordTokenException(){

        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(getUser());
        Mockito.doNothing().when(emailSender).sendEmail(any(EmailModel.class));

        assertThatThrownBy(() -> userService.verifyRecoverPassword(USER_ID, "token"))
                .isInstanceOf(UnequalRecoverPasswordTokenException.class);
    }

    private User getUser(){
        User user = new User();
        user.setEmail(USER_ID);
        user.setPassword("pasword");
        user.setRecoverPasswordToken("token_1");
        return user;
    }

    private B2BUserEmployer getB2BUser() {
        B2BUserEmployer user = new B2BUserEmployer();
        user.setCompanyId("1");
        user.setCompanyName("Coto");
        user.setPercentageDiscountPerMembership(new ArrayList<>(Arrays.asList(3F)));
        return user;
    }

}
