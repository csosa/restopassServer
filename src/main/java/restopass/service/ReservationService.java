package restopass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import restopass.dto.Reservation;
import restopass.dto.ReservationState;
import restopass.mongo.ReservationRepository;

import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private MongoTemplate mongoTemplate;
    private ReservationRepository reservationRepository;
    private String OWNER_USER_ID = "ownerUser";
    private String RESERVATION_ID = "reservationId";
    private String RESERVATION_STATE = "state";
    private String RESERVATION_COLLECTION = "reservations";

    @Autowired
    public ReservationService(MongoTemplate mongoTemplate, ReservationRepository reservationRepository) {
        this.mongoTemplate = mongoTemplate;
        this.reservationRepository = reservationRepository;
    }

    public void createReservation(Reservation reservation) {
        String reservationId = UUID.randomUUID().toString();
        reservation.setReservationId(reservationId);
        this.reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsForUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(OWNER_USER_ID).is(userId));

        return this.mongoTemplate.find(query, Reservation.class);
    }

    public void cancelReservation(String reservationId) {
        this.updateReservationState(reservationId, ReservationState.CANCELED);
    }

    public void doneReservation(String reservationId) {
        this.updateReservationState(reservationId, ReservationState.DONE);
    }

    private void updateReservationState(String reservationId, ReservationState state) {
        Query query = new Query();
        query.addCriteria(Criteria.where(RESERVATION_ID).is(reservationId));

        Update update = new Update();
        update.set(RESERVATION_STATE, state);

        this.mongoTemplate.updateMulti(query, update, RESERVATION_COLLECTION);
    }


}
