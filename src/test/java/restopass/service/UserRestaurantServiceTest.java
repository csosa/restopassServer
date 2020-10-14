package restopass.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import restopass.dto.Dish;
import restopass.dto.Restaurant;
import restopass.dto.UserRestaurant;
import restopass.dto.request.UserLoginRequest;
import restopass.dto.response.UserLoginResponse;
import restopass.exception.UserAlreadyExistsException;
import restopass.mongo.UserRestaurantRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserRestaurantServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private UserRestaurantRepository userRestaurantRepository;

    @InjectMocks
    private UserRestaurantService userRestaurantService;

    private static final String ACCESS_TOKEN_HEADER = "X-Auth-Token";
    private static final String REFRESH_TOKEN_HEADER = "X-Refresh-Token";
    public static final String USER_ID = "prueba@prueba.com";

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createUserRestaurantOK(){

        when(mongoTemplate.findOne(any(Query.class), eq(UserRestaurant.class))).thenReturn(null);
        when(restaurantService.findById(anyString())).thenReturn(getRestaurant());

        userRestaurantService.createUserRestaurant(getUserRestaurant());
    }

    @Test
    public void createUserRestaurant_UserAlreadyExists(){

        when(mongoTemplate.findOne(any(Query.class), eq(UserRestaurant.class))).thenReturn(getUserRestaurant());
        when(restaurantService.findById(anyString())).thenReturn(getRestaurant());

        assertThatThrownBy(() -> userRestaurantService.createUserRestaurant(getUserRestaurant()))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    public void loginRestaurantUserOK(){

        when(mongoTemplate.findOne(any(Query.class), eq(UserRestaurant.class))).thenReturn(getUserRestaurant());
        when(restaurantService.findById(anyString())).thenReturn(getRestaurant());

        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setEmail(USER_ID);
        loginRequest.setPassword("password");

        UserLoginResponse<UserRestaurant> loginResponse = userRestaurantService.loginRestaurantUser(loginRequest);

        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getxAuthToken());
        assertEquals(USER_ID, loginResponse.getUser().getEmail());
    }

    @Test
    public void refreshRestaurantTokenOK(){

        HttpServletRequest servletRequest = mock(HttpServletRequest.class);

        when(mongoTemplate.findOne(any(Query.class), eq(UserRestaurant.class))).thenReturn(getUserRestaurant());
        when(restaurantService.findById(anyString())).thenReturn(getRestaurant());
        when(servletRequest.getHeader(REFRESH_TOKEN_HEADER)).thenReturn(getHeaders().get(REFRESH_TOKEN_HEADER));
        when(servletRequest.getHeader(ACCESS_TOKEN_HEADER)).thenReturn(getHeaders().get(ACCESS_TOKEN_HEADER));

        UserLoginResponse<UserRestaurant> loginResponse = userRestaurantService.refreshRestaurantToken(servletRequest);

        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getxAuthToken());
        assertNotNull(loginResponse.getxRefreshToken());
        assertEquals(USER_ID, loginResponse.getUser().getEmail());
    }

    @Test
    public void deleteUserRestaurantOK(){

        when(mongoTemplate.findOne(any(Query.class), eq(UserRestaurant.class))).thenReturn(null);

        userRestaurantService.deleteUserRestaurant(USER_ID);
    }

    private UserRestaurant getUserRestaurant(){
      return new UserRestaurant(USER_ID, "password", "1");
    }

    private Restaurant getRestaurant(){
        Restaurant restaurant = new Restaurant();
        restaurant.setName("La Continental");
        restaurant.setDishes(new ArrayList<>(Arrays.asList(getDish())));
        return restaurant;
    }

    private Dish getDish(){
        Dish dish = new Dish();
        dish.setBaseMembership(2);
        dish.setCountStars(3);
        dish.setName("Muzzarella");
        return dish;
    }

    private Map<String, String> getHeaders(){
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(ACCESS_TOKEN_HEADER, "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJwcnVlYmFAcHJ1ZWJhLmNvbSIsImlhdCI6MTYwMTc3MjY3OCwiZXhwIjoxNjAxNzczMjc4fQ.jFNUlz5f1DM3M5QTvmAZDn7qZgHG_dDALwHMWxHBbp0");
        headersMap.put(REFRESH_TOKEN_HEADER, "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJwcnVlYmFAcHJ1ZWJhLmNvbSIsImlhdCI6MTYwMTc3MjY3OH0.QHnepDenT49SZMbhvYWokKTZq0afIPz1oC65dLx2CUk");
        return headersMap;
    }
}
