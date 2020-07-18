package restopass.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import restopass.dto.B2CUserEmployer;

@Service
public interface B2CUserRepository extends MongoRepository<B2CUserEmployer, String> {
}
