package volo.voloCalendar.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import volo.voloCalendar.util.UserGrantedAuthority;


@Component(value = "loginService")
public class LoginLogic implements Serializable {
    @Autowired
    public UserManagement userManagementLogic;

    private static final long serialVersionUID = 1L;

    public UserDetails loadUser(String email, String password)
            throws HttpClientErrorException {
        boolean isAdmin;
        try {
            isAdmin = userManagementLogic.authenticate(email, password);
        } catch (HttpClientErrorException ex) {
            return null;
        }

        List<GrantedAuthority> authorities = addAccordingRoleByUserType(isAdmin ? "ROLE_ADMIN" : "ROLE_DRIVER");
        org.springframework.security.core.userdetails.User springUser = new org.springframework.security.core.userdetails.User(email, password,
                authorities);
        return springUser;
    }

    private List<GrantedAuthority> addAccordingRoleByUserType(
            final String userType) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new UserGrantedAuthority(userType));
        return authorities;
    }

}
