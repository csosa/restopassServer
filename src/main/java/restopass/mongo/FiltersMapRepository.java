package restopass.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import restopass.dto.FilterMap;

@Service
public interface FiltersMapRepository extends MongoRepository<FilterMap, String> {
}
