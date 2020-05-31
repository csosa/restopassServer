package restopass.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import restopass.dto.RestaurantConfig;

@Service
public interface RestaurantConfigRepository extends MongoRepository<RestaurantConfig, String> {
}
