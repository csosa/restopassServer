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
import restopass.dto.B2BUserEmployer;
import restopass.mongo.B2BUserRepository;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class B2BUserServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Spy
    private B2BUserRepository b2BUserRepository;

    @MockBean
    private UserService userService;

    @InjectMocks
    private B2BUserService b2BUserService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saverB2BUserOK() {
        b2BUserService.createUser(getOtherB2BUserEmployer());
    }

    @Test
    public void checkIfB2BUserOK() {

        when(mongoTemplate.findOne(any(Query.class), eq(B2BUserEmployer.class))).thenReturn(getB2BUserEmployer());

        B2BUserEmployer findUser = b2BUserService.checkIfB2BUser("prueba@coto.com");

        assertEquals("Coto", findUser.getCompanyName());
    }

    @Test
    public void updateDiscountOK() {

        when(mongoTemplate.findOne(any(Query.class), eq(B2BUserEmployer.class))).thenReturn(getOtherB2BUserEmployer());

        b2BUserService.updateDiscounts(getOtherB2BUserEmployer());
    }

    private B2BUserEmployer getB2BUserEmployer() {
        B2BUserEmployer user = new B2BUserEmployer();
        user.setCompanyId("1");
        user.setCompanyName("Coto");
        return user;
    }

    private B2BUserEmployer getOtherB2BUserEmployer() {
        B2BUserEmployer user = new B2BUserEmployer();
        user.setCompanyId("2");
        user.setCompanyName("Wallmart");
        user.setPercentageDiscountPerMembership(new ArrayList<>(Arrays.asList(3F)));
        user.setEmployeesEmails(new ArrayList<>(Arrays.asList("p@p.com")));
        return user;
    }
}
