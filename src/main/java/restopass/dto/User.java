package restopass.dto;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Document(collection = "users")
public class User {

    @Indexed(unique=true)
    private String email;
    private String password;
    private String name;
    private String lastName;
    private String creditCard;
    private Integer visits;
    private MembershipType actualMembership;
    private HashMap<String, Boolean> userPreferences;

    public User(String email, String password, String name, String lastName) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public MembershipType getActualMembership() {
        return actualMembership;
    }

    public void setActualMembership(MembershipType actualMembership) {
        this.actualMembership = actualMembership;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public HashMap<String, Boolean> getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(HashMap<String, Boolean> userPreferences) {
        this.userPreferences = userPreferences;
    }
}
