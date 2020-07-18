package restopass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import restopass.dto.B2CUserEmployer;
import restopass.mongo.B2CUserRepository;

import java.util.UUID;

@Service
public class B2CUserService {

    private String EMPLOYEES_EMAILS_FIELD = "employeesEmails";
    private String DISCOUNTS_FIELD = "percentageDiscountPerMembership";
    private String ID_FIELD = "companyId";
    private String B2C_USERS_COLLECTION = "b2c_users";

    @Autowired
    private UserService userService;
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
        user.setCompanyId(UUID.randomUUID().toString());
        this.b2CUserRepository.save(user);

        user.getEmployeesEmails().forEach(
                employee -> this.userService.setB2CUserToEmployees(employee, user.getPercentageDiscountPerMembership()));
    }

    public void updateDiscounts(B2CUserEmployer user) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ID_FIELD).in(user.getCompanyId()));

        Update update = new Update().set(DISCOUNTS_FIELD, user.getPercentageDiscountPerMembership());

        this.mongoTemplate.updateMulti(query, update, B2C_USERS_COLLECTION);

        B2CUserEmployer employer = this.findById(user.getCompanyId());

        employer.getEmployeesEmails().forEach(
                employee -> this.userService.setB2CUserToEmployees(employee, user.getPercentageDiscountPerMembership())
        );
    }

    private B2CUserEmployer findById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ID_FIELD).in(id));

        return this.mongoTemplate.findOne(query, B2CUserEmployer.class);
    }
}
