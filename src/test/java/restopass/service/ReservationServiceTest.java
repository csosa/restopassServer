package restopass.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import restopass.dto.*;
import restopass.dto.request.CreateReservationRequest;
import restopass.dto.response.DoneReservationResponse;
import restopass.dto.response.ReservationResponse;
import restopass.exception.*;
import restopass.mongo.MembershipRepository;
import restopass.mongo.ReservationRepository;
import restopass.utils.EmailSender;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class ReservationServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private FirebaseService firebaseService;

    @MockBean
    private UserService userService;

    @MockBean
    private RestaurantService restaurantService;

    @Mock
    private EmailSender emailSender;

    @MockBean
    private UserRestaurantService userRestaurantService;

    @InjectMocks
    private ReservationService reservationService;

    public static final String USER_ID_1 = "prueba@prueba.com";
    public static final String USER_ID_2 = "prueba2@prueba.com";
    public LocalDateTime RESERVATION_DATE = LocalDateTime.of(2020, Month.DECEMBER, 01, 21, 00, 00);

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createReservationOK() {

        CreateReservationRequest request = new CreateReservationRequest();
        request.setRestaurantId("1");
        request.setDate(RESERVATION_DATE.toString());
        request.setDinners(2);
        request.setToConfirmUsers(Arrays.asList(USER_ID_1, "maria@prueba.com"));

        Mockito.doNothing().when(emailSender).sendEmail(any(EmailModel.class));

        when(userService.findById(anyString())).thenReturn(getUser());
        when(restaurantService.findById(anyString())).thenReturn(getRestaurant());
        when(restaurantService.findConfigurationByRestaurantId(anyString())).thenReturn(getRestaurantConfig());
        when(restaurantService.decrementTableInSlot(any(RestaurantConfig.class), any(LocalDateTime.class))).thenReturn(getRestaurantSlots());

        reservationService.createReservation(request, USER_ID_1);
    }

    @Test
    public void getReservationsForUserOK(){

        when(userService.findById(anyString())).thenReturn(getUser());
        when(restaurantService.findById(anyString())).thenReturn(getRestaurant());

        List<ReservationResponse> reservationResponses = reservationService.getReservationsForUser(USER_ID_2);

        assertNotNull(reservationResponses);
        assertTrue(reservationResponses.isEmpty());
    }

    @Test
    public void getReservationsHistoryForUserOK(){

        when(userService.findById(anyString())).thenReturn(getUser());
        when(restaurantService.findById(anyString())).thenReturn(getRestaurant());

        List<ReservationResponse> reservationResponses = reservationService.getReservationsHistoryForUser(USER_ID_2);

        assertNotNull(reservationResponses);
        assertTrue(reservationResponses.isEmpty());
    }

    @Test
    public void confirmReservationOK(){

        when(mongoTemplate.findOne(any(Query.class), eq(Reservation.class))).thenReturn(getReservation());
        when(userService.findById(anyString())).thenReturn(getUser(), getUser2());
        when(restaurantService.findById(anyString())).thenReturn(getRestaurant());

        reservationService.confirmReservation("1234", USER_ID_1);
    }

    @Test
    public void confirmReservation_throwReservationCanceledException(){

        when(mongoTemplate.findOne(any(Query.class), eq(Reservation.class))).thenReturn(getCanceledReservation());
        when(userService.findById(anyString())).thenReturn(getUser());
        when(restaurantService.findById(anyString())).thenReturn(getRestaurant());

        assertThatThrownBy(() -> reservationService.confirmReservation("123", USER_ID_2))
                .isInstanceOf(ReservationCanceledException.class);
    }

    @Test
    public void confirmReservation_throwReservationAlreadyConfirmedException(){

        when(mongoTemplate.findOne(any(Query.class), eq(Reservation.class))).thenReturn(getDoneReservation());
        when(userService.findById(anyString())).thenReturn(getUser());
        when(restaurantService.findById(anyString())).thenReturn(getRestaurant());

        assertThatThrownBy(() -> reservationService.confirmReservation("123", USER_ID_1))
                .isInstanceOf(ReservationAlreadyConfirmedException.class);
    }

    @Test
    public void confirmReservation_throwNoMoreVisitsException(){

        when(mongoTemplate.findOne(any(Query.class), eq(Reservation.class))).thenReturn(getDoneReservation());
        when(userService.findById(anyString())).thenReturn(getUserWithNoVisits());
        when(restaurantService.findById(anyString())).thenReturn(getRestaurant());

        assertThatThrownBy(() -> reservationService.confirmReservation("123", USER_ID_2))
                .isInstanceOf(NoMoreVisitsException.class);
    }

    @Test
    public void cancelReservationOK(){

        List<Reservation> reservationList = Collections.singletonList(getDoneReservation());
        when(mongoTemplate.find(any(Query.class), eq(Reservation.class))).thenReturn(reservationList);
        when(userService.findById(anyString())).thenReturn(getUser());
        when(restaurantService.findById(anyString())).thenReturn(getRestaurant());

        List<ReservationResponse> reservationResponses = reservationService.cancelReservation("123", USER_ID_1);

        assertNotNull(reservationResponses);
    }

    @Test
    public void cancelReservation_throwReservationCancelTimeExpiredException(){

        List<Reservation> reservationList = Collections.singletonList(getReservation());
        when(mongoTemplate.find(any(Query.class), eq(Reservation.class))).thenReturn(reservationList);
        when(userService.findById(anyString())).thenReturn(getUser2());
        when(restaurantService.findById(anyString())).thenReturn(getRestaurant());

        assertThatThrownBy(() -> reservationService.cancelReservation("1234", USER_ID_2))
                .isInstanceOf(ReservationCancelTimeExpiredException.class);
    }

    @Test
    public void doneReservationOK(){

        when(mongoTemplate.findOne(any(Query.class), eq(Reservation.class))).thenReturn(getDoneReservation());
        when(userService.findById(anyString())).thenReturn(getUser(), getUser2());
        when(restaurantService.findById(anyString())).thenReturn(getRestaurant());
        when(userRestaurantService.findById(anyString())).thenReturn(getUserRestaurant());
        when(membershipRepository.findAll()).thenReturn(getMemberships());

        DoneReservationResponse response = reservationService.doneReservation("1234", "1", USER_ID_1, "1");

        assertNotNull(response);
    }

    @Test
    public void doneReservation_throwReservationNofFoundException(){

        when(mongoTemplate.findOne(any(Query.class), eq(Reservation.class))).thenReturn(null);

        assertThatThrownBy(() -> reservationService.doneReservation("1234", "1", USER_ID_1, "1"))
        .isInstanceOf(ReservationNofFoundException.class);
    }

    @Test
    public void doneReservation_throwReservationNotInThisRestaurantException(){

        when(mongoTemplate.findOne(any(Query.class), eq(Reservation.class))).thenReturn(getReservation());
        when(userRestaurantService.findById(anyString())).thenReturn(null);

        assertThatThrownBy(() -> reservationService.doneReservation("1234", "1", USER_ID_1, ""))
                .isInstanceOf(ReservationNotInThisRestaurantException.class);
    }

    private User getUser(){
        User user = new User();
        user.setEmail(USER_ID_1);
        user.setPassword("pasword");
        user.setVisits(3);
        user.setActualMembership(2);
        user.setSecondaryEmails(new HashSet<>(Arrays.asList("hola2@prueba.com")));
        return user;
    }

    private User getUser2(){
        User user = new User();
        user.setEmail(USER_ID_2);
        user.setPassword("pasword");
        user.setVisits(3);
        user.setActualMembership(2);
        return user;
    }

    private User getUserWithNoVisits(){
        User user = new User();
        user.setEmail(USER_ID_1);
        user.setPassword("pasword");
        return user;
    }

    private Restaurant getRestaurant(){
        Restaurant restaurant = new Restaurant();
        restaurant.setName("La Continental");
        restaurant.setDishes(Collections.singletonList(getDish()));
        restaurant.setHoursToCancel(2);
        restaurant.setRestaurantId("1");
        return restaurant;
    }

    private UserRestaurant getUserRestaurant(){
        return new UserRestaurant("restaurante@prueba.com", "password", "1");
    }

    private Dish getDish(){
        Dish dish = new Dish();
        dish.setBaseMembership(2);
        dish.setBaseMembershipName("Oro");
        dish.setCountStars(3);
        dish.setName("Muzzarella");
        return dish;
    }

    private RestaurantConfig getRestaurantConfig(){
        RestaurantConfig restaurantConfig = new RestaurantConfig();
        restaurantConfig.setMaxDiners(3);
        restaurantConfig.setMinutesGap(20);
        restaurantConfig.setRestaurantId("1");
        restaurantConfig.setTablesPerShift(2);
        return  restaurantConfig;
    }

    private Reservation getDoneReservation(){
        Reservation reservation = new Reservation();
        reservation.setDate(RESERVATION_DATE);
        reservation.setDinners(2);
        reservation.setOwnerUser(USER_ID_1);
        reservation.setRestaurantId("1");
        reservation.setConfirmedUsers(Arrays.asList(USER_ID_1, USER_ID_2));
        reservation.setReservationId("123");
        reservation.setState(ReservationState.DONE);
        return reservation;
    }

    private Reservation getReservation(){
        Reservation reservation = new Reservation();
        reservation.setDate(LocalDateTime.now());
        reservation.setDinners(2);
        reservation.setOwnerUser(USER_ID_2);
        reservation.setRestaurantId("1");
        reservation.setToConfirmUsers(Arrays.asList(USER_ID_1, USER_ID_2));
        reservation.setReservationId("1234");
        reservation.setState(ReservationState.CONFIRMED);
        return reservation;
    }

    private Reservation getCanceledReservation(){
        Reservation reservation = new Reservation();
        reservation.setState(ReservationState.CANCELED);
        return reservation;
    }

    private List<RestaurantSlot> getRestaurantSlots(){
        return Collections.unmodifiableList(new ArrayList<>());
    }

    private List<Membership> getMemberships(){
        return new ArrayList<>(Arrays.asList(getMembership(2)));
    }

    private Membership getMembership(Integer id){
        Membership membership = new Membership();
        membership.setMembershipId(id);
        membership.setName("Oro");
        return  membership;
    }
}
