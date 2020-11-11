package restopass.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import restopass.dto.Dish;
import restopass.dto.Membership;
import restopass.dto.Restaurant;
import restopass.dto.User;
import restopass.dto.request.UpdateMembershipToUserRequest;
import restopass.dto.response.ChangeMembershipResponse;
import restopass.dto.response.MembershipsResponse;
import restopass.exception.UserNotFoundException;
import restopass.mongo.MembershipRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class MembershipServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Spy
    private MembershipRepository membershipRepository;

    @Mock
    private UserService userService;

    @MockBean
    private RestaurantService restaurantService;

    @InjectMocks
    private MembershipService membershipService;

    public static final String USER_ID = "prueba@prueba.com";
    public static final int MEMBERSHIP_1 = 1;
    public static final int MEMBERSHIP_2 = 2;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createMembershipOK(){
        membershipService.createMembership(getMembership(2));
    }

    @Test
    public void getUserMembershipOK(){

        when(userService.findById(anyString())).thenReturn(getUserWithMembership());
        when(membershipRepository.findAll()).thenReturn(getMemberships());
        when(restaurantService.getRestaurantInAMemberships(anyInt())).thenReturn(Collections.singletonList(getRestaurant()));

        MembershipsResponse response = membershipService.getMemberships(USER_ID);

        assertEquals(2, response.getActualMembership().getMembershipInfo().getMembershipId().intValue());
    }

    @Test
    public void userWithNoMembershipsOK(){

        when(userService.findById(anyString())).thenReturn(getUser());
        when(membershipRepository.findAll()).thenReturn(getMemberships());
        when(restaurantService.getRestaurantInAMemberships(anyInt())).thenReturn(Collections.singletonList(getRestaurant()));

        MembershipsResponse response = membershipService.getMemberships(USER_ID);

        assertEquals(1, response.getMemberships().size());
        assertNull(response.getActualMembership());
    }

    @Test
    public void updateUserMembershipOK(){

        mongoTemplate = mock(MongoTemplate.class);
        userService = mock(UserService.class);
        membershipRepository = mock(MembershipRepository.class);

        membershipService = new MembershipService(membershipRepository, mongoTemplate, userService);

        UpdateMembershipToUserRequest request = new UpdateMembershipToUserRequest();
        request.setMembershipId(MEMBERSHIP_1);

        when(userService.findById(anyString())).thenReturn(getUserWithMembership());
        when(userService.updateMembership(anyString(), any(Membership.class))).thenReturn(LocalDateTime.now());
        when(mongoTemplate.findOne(any(Query.class), eq(Membership.class))).thenReturn(getMembership(MEMBERSHIP_1));

        ChangeMembershipResponse response = membershipService.updateMembershipToUser(USER_ID, request);

        assertNotNull(response.getChangedDate());
    }

    @Test
    public void updateMembership_thorwUserNotFoundException(){

        when(userService.findById(anyString())).thenReturn(null);

        UpdateMembershipToUserRequest request = new UpdateMembershipToUserRequest();
        request.setMembershipId(MEMBERSHIP_1);

        assertThatThrownBy(() -> membershipService.updateMembershipToUser(USER_ID, request))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void removeMembershipToUserOK(){

        when(userService.findById(anyString())).thenReturn(getUser());
        when(userService.removeMembership(anyString())).thenReturn(LocalDateTime.now());

        ChangeMembershipResponse response = membershipService.removeMembershipToUser(USER_ID);

        assertNotNull(response.getChangedDate());
    }


    @Test
    public void removeMembershipToUser_throwUserNotFoundException(){

        when(userService.findById(anyString())).thenReturn(null);

        assertThatThrownBy(() -> membershipService.removeMembershipToUser(USER_ID))
                .isInstanceOf(UserNotFoundException.class);
    }

    private Membership getMembership(Integer id){
        Membership membership = new Membership();
        membership.setMembershipId(id);
        return  membership;
    }

    private List<Membership> getMemberships(){
        return new ArrayList<>(Arrays.asList(getMembership(2)));
    }

    private User getUser(){
        User user = new User();
        user.setEmail(USER_ID);
        user.setPassword("pasword");
        return user;
    }

    private User getUserWithMembership(){
        User user = getUser();
        user.setActualMembership(MEMBERSHIP_2);
        return user;
    }

    private Restaurant getRestaurant(){
        Restaurant restaurant = new Restaurant();
        restaurant.setName("La Continental");
        restaurant.setDishes(Collections.singletonList(getDish()));
        return restaurant;
    }

    private Dish getDish(){
        Dish dish = new Dish();
        dish.setBaseMembership(2);
        dish.setCountStars(3);
        dish.setName("Muzzarella");
        return dish;
    }
}
