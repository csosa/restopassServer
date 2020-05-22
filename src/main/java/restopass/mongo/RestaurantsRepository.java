package restopass.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import restopass.dto.Restaurant;

@Service
public interface RestaurantsRepository extends MongoRepository<Restaurant, String> {
}
