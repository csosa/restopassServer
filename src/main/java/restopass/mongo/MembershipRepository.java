package restopass.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import restopass.dto.Membership;

@Service
public interface MembershipRepository extends MongoRepository<Membership, String> {
}
