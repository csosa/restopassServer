package restopass.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public interface RestaurantRepository extends MongoRepository<RestaurantDTO, String> {
}
