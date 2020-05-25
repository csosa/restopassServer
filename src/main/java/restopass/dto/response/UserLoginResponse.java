package restopass.dto.response;

public class UserLoginResponse {

    private String xAuthToken;
    private String xRefreshToken;
    private String userId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

