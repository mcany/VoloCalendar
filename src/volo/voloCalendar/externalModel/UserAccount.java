package volo.voloCalendar.externalModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 28/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccount implements Serializable {
    private String email;
    private String password;
    private String gender;
    private String name;

    public UserAccount() {
    }

    public UserAccount(String email, String password, String gender, String name) {
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.name = name;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
