package restopass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import restopass.dto.B2CUserEmployer;
import restopass.mongo.B2CUserRepository;

@Service
public class B2CUserService {

    private String EMPLOYEES_EMAILS_FIELD = "employeesEmails";

    private B2CUserRepository b2CUserRepository;
    private MongoTemplate mongoTemplate;

    @Autowired
    public B2CUserService(B2CUserRepository b2CUserRepository, MongoTemplate mongoTemplate) {
        this.b2CUserRepository = b2CUserRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public B2CUserEmployer checkIfB2CUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(EMPLOYEES_EMAILS_FIELD).in(userId));

        return this.mongoTemplate.findOne(query, B2CUserEmployer.class);
    }

    public void createUser(B2CUserEmployer user) {
        this.b2CUserRepository.save(user);
    }
}
