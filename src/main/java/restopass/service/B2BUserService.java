package restopass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import restopass.dto.B2BUserEmployer;
import restopass.mongo.B2BUserRepository;

import java.util.UUID;

@Service
public class B2BUserService {

    private String EMPLOYEES_EMAILS_FIELD = "employeesEmails";
    private String DISCOUNTS_FIELD = "percentageDiscountPerMembership";
    private String COMPANY_NAME_FIELD = "companyName";
    private String ID_FIELD = "companyId";
    private String B2B_USERS_COLLECTION = "b2b_users";

    @Autowired
    private UserService userService;
    private B2BUserRepository b2BUserRepository;
    private MongoTemplate mongoTemplate;

    @Autowired
    public B2BUserService(B2BUserRepository b2BUserRepository, MongoTemplate mongoTemplate) {
        this.b2BUserRepository = b2BUserRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public B2BUserEmployer checkIfB2BUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(EMPLOYEES_EMAILS_FIELD).in(userId));

        return this.mongoTemplate.findOne(query, B2BUserEmployer.class);
    }

    public void createUser(B2BUserEmployer user) {
        user.setCompanyId(UUID.randomUUID().toString());
        this.b2BUserRepository.save(user);

        user.getEmployeesEmails().forEach(
                employee -> this.userService.setB2BUserToEmployees(employee, user.getPercentageDiscountPerMembership(), user.getCompanyName()));
    }

    public void addExistingUserToCompany(B2BUserEmployer company, String userId) {
        B2BUserEmployer companyEntity = this.findByName(company.getCompanyName());
        this.userService.setB2BUserToEmployees(userId, companyEntity.getPercentageDiscountPerMembership(), companyEntity.getCompanyName());
    }

    public void updateDiscounts(B2BUserEmployer user) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ID_FIELD).in(user.getCompanyId()));

        Update update = new Update().set(DISCOUNTS_FIELD, user.getPercentageDiscountPerMembership());

        this.mongoTemplate.updateMulti(query, update, B2B_USERS_COLLECTION);

        B2BUserEmployer employer = this.findById(user.getCompanyId());

        employer.getEmployeesEmails().forEach(
                employee -> this.userService.setB2BUserToEmployees(employee, user.getPercentageDiscountPerMembership(), user.getCompanyName())
        );
    }

    private B2BUserEmployer findById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ID_FIELD).in(id));

        return this.mongoTemplate.findOne(query, B2BUserEmployer.class);
    }

    private B2BUserEmployer findByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where(COMPANY_NAME_FIELD).in(name));

        return this.mongoTemplate.findOne(query, B2BUserEmployer.class);
    }
}
