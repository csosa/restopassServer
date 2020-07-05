package restopass.dto.response;

import restopass.dto.User;

public class UserLoginResponse {

    private String xAuthToken;
    private String xRefreshToken;
    private User user;
    private boolean isCreation;

    public boolean isCreation() {
        return isCreation;
    }

    public void setCreation(boolean creation) {
        isCreation = creation;
    }

    public String getxAuthToken() {
        return xAuthToken;
    }

    public void setxAuthToken(String xAuthToken) {
        this.xAuthToken = xAuthToken;
    }

    public String getxRefreshToken() {
        return xRefreshToken;
    }

    public void setxRefreshToken(String xRefreshToken) {
        this.xRefreshToken = xRefreshToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

