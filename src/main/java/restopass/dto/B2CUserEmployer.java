package restopass.dto;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;

@Document(collection = "b2c_users")
public class B2CUserEmployer {

    private String companyId;
    private String companyName;
    private HashMap<Integer, Float> percentageDiscountPerMembership;
    private List<String> employeesEmails;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public HashMap<Integer, Float> getPercentageDiscountPerMembership() {
        return percentageDiscountPerMembership;
    }

    public void setPercentageDiscountPerMembership(HashMap<Integer, Float> percentageDiscountPerMembership) {
        this.percentageDiscountPerMembership = percentageDiscountPerMembership;
    }

    public List<String> getEmployeesEmails() {
        return employeesEmails;
    }

    public void setEmployeesEmails(List<String> employeesEmails) {
        this.employeesEmails = employeesEmails;
    }
}
