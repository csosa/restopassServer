package restopass.dto;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Document(collection = "users")
public class User {

    @Indexed(unique = true)
    private String email;
    private String password;
    private String name;
    private String lastName;
    private CreditCard creditCard;
    private Integer visits;
    private Integer actualMembership;
    private List<String> secondaryEmails;
    private List<String> toConfirmEmails;
    private Set<String> favoriteRestaurants;
    private B2CUserEmployee b2CUserEmployee;

    public User() {
    }

    public User(String email, String password, String name, String lastName) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
    }

    public Set<String> getFavoriteRestaurants() {
        return favoriteRestaurants;
    }

    public void setFavoriteRestaurants(Set<String> favoriteRestaurants) {
        this.favoriteRestaurants = favoriteRestaurants;
    }

    public List<String> getSecondaryEmails() {
        return secondaryEmails;
    }

    public void setSecondaryEmails(List<String> secondaryEmails) {
        this.secondaryEmails = secondaryEmails;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public Integer getActualMembership() {
        return actualMembership;
    }

    public void setActualMembership(Integer actualMembership) {
        this.actualMembership = actualMembership;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public B2CUserEmployee getB2CUserEmployee() {
        return b2CUserEmployee;
    }

    public void setB2CUserEmployee(B2CUserEmployee b2CUserEmployee) {
        this.b2CUserEmployee = b2CUserEmployee;
    }

    public List<String> getToConfirmEmails() {
        return toConfirmEmails;
    }

    public void setToConfirmEmails(List<String> toConfirmEmails) {
        this.toConfirmEmails = toConfirmEmails;
    }
}
