package restopass.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import restopass.dto.Reservation;

@Service
public interface ReservationRepository extends MongoRepository<Reservation, String> {
}
