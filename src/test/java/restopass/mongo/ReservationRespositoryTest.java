package restopass.mongo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import restopass.dto.Reservation;
import restopass.dto.ReservationState;
import restopass.service.FirebaseService;
import restopass.service.ReservationService;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@DataMongoTest
public class ReservationRespositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ReservationRepository reservationRepository;

    private ReservationService reservationService;
    private FirebaseService firebaseService;
    private MembershipRepository membershipRepository;
    public static final String USER_ID_1 = "prueba@prueba.com";

    @Before
    public void init(){
        this.firebaseService = mock(FirebaseService.class);
        this.membershipRepository = mock(MembershipRepository.class);
        this.reservationService = new ReservationService(mongoTemplate, reservationRepository, firebaseService, membershipRepository);
        this.reservationRepository.save(getReservation());
    }

    @Test
    public void findByIdOK(){

        Reservation reservation = reservationService.findById("123");

        assertNotNull(reservation);
    }


    @Test
    public void deleteUserReservationsOK(){

        reservationService.deleteUserReservations(USER_ID_1);
        Reservation reservation = reservationService.findById("123");

        assertNull(reservation);
    }

    private Reservation getReservation(){
        Reservation reservation = new Reservation();
        reservation.setDate(LocalDateTime.now());
        reservation.setDinners(2);
        reservation.setOwnerUser(USER_ID_1);
        reservation.setConfirmedUsers(Arrays.asList(USER_ID_1, "jose@prueba.com"));
        reservation.setReservationId("123");
        reservation.setState(ReservationState.CONFIRMED);
        return reservation;
    }
}
