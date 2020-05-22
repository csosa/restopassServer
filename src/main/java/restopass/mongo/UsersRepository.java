package restopass.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import restopass.dto.User;

@Service
public interface UsersRepository extends MongoRepository<User, String> {
}
