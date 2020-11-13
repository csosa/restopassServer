package restopass.mongo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import restopass.dto.CreditCard;
import restopass.dto.Membership;
import restopass.dto.User;
import restopass.dto.request.UserUpdateRequest;
import restopass.exception.*;
import restopass.service.GoogleService;
import restopass.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    private UserService userService;
    public static final String USER_ID_1 = "prueba@prueba.com";
    public static final String USER_ID_2 = "juan@prueba.com";
    public static final String USER_ID_3 = "sofia@prueba.com";


    @Before
    public void init(){
        GoogleService googleService = mock(GoogleService.class);
        this.userService = new UserService(mongoTemplate, userRepository, googleService);
        this.userRepository.save(getUser());
        this.userRepository.save(getUser2());
        this.userRepository.save(getUserWithNoMembership());
    }

    @After
    public void after(){
        userRepository.deleteAll();
    }

    @Test
    public void findUserByIdOK(){

        User userFind = userService.findById(USER_ID_1);

        assertNotNull(userFind);
        assertEquals(USER_ID_1, userFind.getEmail());
    }

    @Test
    public void decrementUserVisits(){

        userService.decrementUserVisits(USER_ID_1);
        User userFind = userService.findById(USER_ID_1);


        assertNotNull(userFind);
        assertEquals(4, userFind.getVisits().intValue());
    }

    @Test
    public void incrementUserVisits(){

        userService.incrementUserVisits(USER_ID_1);
        User userFind = userService.findById(USER_ID_1);

        assertNotNull(userFind);
        assertEquals(6, userFind.getVisits().intValue());
    }

    @Test
    public void updateMembershipOK(){

        Membership membership = new Membership();
        membership.setMembershipId(2);
        membership.setVisits(8);

        LocalDateTime time = userService.updateMembership(USER_ID_1, membership);
        User userFind = userService.findById(USER_ID_1);

        assertEquals(time, userFind.getMembershipEnrolledDate());
        assertEquals(8, userFind.getVisits().intValue());
        assertEquals(2, userFind.getActualMembership().intValue());
    }

    @Test
    public void removeMembershipOK(){

        LocalDateTime time = userService.removeMembership(USER_ID_1);
        User userFind = userService.findById(USER_ID_1);

        assertEquals(time, userFind.getMembershipFinalizeDate());
        assertNotNull(userFind.getActualMembership());
        assertNull(userFind.getMembershipEnrolledDate());
    }

    @Test
    public void addNewFavoriteRestaurantOK(){

        userService.addNewRestaurantFavorite("3", USER_ID_1);
        User userFind = userService.findById(USER_ID_1);

        assertEquals(3, userFind.getFavoriteRestaurants().size());
    }

    @Test
    public void removeFavoriteRestaurantOK(){

        userService.removeRestaurantFavorite("1", USER_ID_1);
        User userFind = userService.findById(USER_ID_1);

        assertEquals(1, userFind.getFavoriteRestaurants().size());
    }

    @Test
    public void checkCanAddToReservationOK(){

        User userFind = userService.checkCanAddToReservation(USER_ID_1, USER_ID_2);

        assertNotNull(userFind);
    }

    @Test
    public void checkCanAddToReservation_throwUserNotFoundException(){

        assertThatThrownBy(() -> userService.checkCanAddToReservation(USER_ID_1, "guest"))
                .isInstanceOf(UserNotFoundException.class);
    }


    @Test
    public void checkCanAddToReservation_throwCannotSelfInviteException(){

        assertThatThrownBy(() -> userService.checkCanAddToReservation(USER_ID_1, USER_ID_1))
                .isInstanceOf(CannotSelfInviteException.class);
    }


    @Test
    public void updateUserInfo_throwEmailAlreadyAddedException(){

        UserUpdateRequest request = new UserUpdateRequest();
        request.setToConfirmEmail("hola2@prueba.com");

        assertThatThrownBy(() -> userService.updateUserInfo(request, USER_ID_1))
                .isInstanceOf(EmailAlreadyAddedException.class);
    }

    @Test
    public void updateUserInfo_thowEmailAlreadyExistsException(){

        UserUpdateRequest request = new UserUpdateRequest();
        request.setToConfirmEmail(USER_ID_1);

        assertThatThrownBy(() -> userService.updateUserInfo(request, USER_ID_1))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    public void updateUserInfo_thowForeignEmailAddedException(){

        UserUpdateRequest request = new UserUpdateRequest();
        request.setToConfirmEmail(USER_ID_2);

        assertThatThrownBy(() -> userService.updateUserInfo(request, USER_ID_1))
                .isInstanceOf(ForeignEmailAddedException.class);
    }

    @Test
    public void removeEmailOK(){

        userService.removeEmail("hola2@prueba.com", USER_ID_1);
        User userFind = userService.findById(USER_ID_1);

        assertNotNull(userFind);
        assertEquals(0, userFind.getSecondaryEmails().size());
        assertEquals(1, userFind.getToConfirmEmails().size());
    }

    @Test
    public void confirmEmailOK(){

        userService.confirmEmail("hola3@prueba.com", USER_ID_1);
        User userFind = userService.findById(USER_ID_1);

        assertNotNull(userFind);
        assertEquals(2, userFind.getSecondaryEmails().size());
        assertEquals(0, userFind.getToConfirmEmails().size());
    }

    @Test
    public void getPaymentOK(){

        CreditCard creditCard = userService.getPayment(USER_ID_1);

        assertNotNull(creditCard);
    }

    @Test
    public void getPayment_throwUserNotFoundException(){

        assertThatThrownBy(() -> userService.getPayment("email"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void getPayment_throwNoCreditCardException(){

        assertThatThrownBy(() -> userService.getPayment(USER_ID_2))
                .isInstanceOf(NoCreditCardException.class);
    }

    @Test
    public void updatePaymentOK(){

        CreditCard creditCardNew = new CreditCard();
        creditCardNew.setHolderName("HOLA PRUEBA");
        creditCardNew.setLastFourDigits("2222");
        creditCardNew.setType("MASTER");

        userService.updatePayment(creditCardNew, USER_ID_1);
        CreditCard creditCard = userService.getPayment(USER_ID_1);

        assertNotNull(creditCard);
        assertEquals(creditCardNew.getHolderName(), creditCard.getHolderName());
    }

    @Test
    public void removePaymentOK(){

        userService.removePayment(USER_ID_1);

        assertThatThrownBy(() -> userService.getPayment(USER_ID_2))
                .isInstanceOf(NoCreditCardException.class);
    }

    @Test
    public void setB2BUserToEmployeesOK(){

        userService.setB2BUserToEmployees(USER_ID_2, Arrays.asList(Float.MIN_NORMAL), "Coto");
        User userFind = userService.findById(USER_ID_2);

        assertNotNull(userFind);
        assertNotNull(userFind.getB2BUserEmployee());
    }

    @Test
    public void verifyRecoverPasswordOK(){

        userService.verifyRecoverPassword(USER_ID_2, "token");
        User userFind = userService.findById(USER_ID_2);

        assertNull(userFind.getRecoverPasswordToken());
    }

    @Test
    public void suscribeToTopicOK(){

        userService.subscribeToTopic(USER_ID_2);
        User userFind = userService.findById(USER_ID_2);

        assertTrue(userFind.getSubscribedToTopic());
    }

    @Test
    public void unsuscribeToTopicOK(){

        userService.unsubscribeToTopic(USER_ID_2);
        User userFind = userService.findById(USER_ID_2);

        assertFalse(userFind.getSubscribedToTopic());
    }

    private User getUser(){
        User user = new User();
        user.setEmail(USER_ID_1);
        user.setPassword("pasword");
        user.setVisits(5);
        user.setActualMembership(1);
        user.setMembershipEnrolledDate(LocalDateTime.now());
        user.setFavoriteRestaurants(new HashSet<>(Arrays.asList("1", "2")));
        user.setSecondaryEmails(new HashSet<>(Arrays.asList("hola2@prueba.com")));
        user.setToConfirmEmails(new HashSet<>(Arrays.asList("hola3@prueba.com")));
        user.setCreditCard(getCreditCard());
        user.setRecoverPasswordToken("token_1");
        return user;
    }

    private CreditCard getCreditCard() {
        CreditCard creditCard = new CreditCard();
        creditCard.setHolderName("PRUEBA PRUEBA");
        creditCard.setLastFourDigits("1111");
        creditCard.setType("VISA");
        return creditCard;
    }

    private User getUser2() {
        User user = new User();
        user.setEmail(USER_ID_2);
        user.setPassword("pasword");
        user.setRecoverPasswordToken("token");
        user.setActualMembership(2);
        return user;
    }

    private User getUserWithNoMembership() {
        User user = new User();
        user.setEmail(USER_ID_3);
        user.setPassword("pasword");
        user.setRecoverPasswordToken("token");
        return user;
    }

}
