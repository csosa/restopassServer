package restopass.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import restopass.dto.B2BUserEmployer;

@Service
public interface B2BUserRepository extends MongoRepository<B2BUserEmployer, String> {
}
