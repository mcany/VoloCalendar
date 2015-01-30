package volo.voloCalendar.externalModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 28/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationRequest implements Serializable{
    private String type;
    private UserAccount user_account;
    private User user;
    private Location location;

    public RegistrationRequest() {
    }

    public RegistrationRequest(boolean admin, User user, Location location, UserAccount userAccount) {
        this.user = user;
        this.location = location;
        this.user_account = userAccount;
        this.type = admin?"operator":"driver";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserAccount getUser_account() {
        return user_account;
    }

    public void setUser_account(UserAccount user_account) {
        this.user_account = user_account;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
