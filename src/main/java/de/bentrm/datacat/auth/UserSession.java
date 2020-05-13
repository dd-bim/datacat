package de.bentrm.datacat.auth;

public class UserSession {

    private String token;
    private UserProfile user;

    public UserSession(String token, UserProfile userProfile) {
        this.token = token;
        this.user = userProfile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }
}
