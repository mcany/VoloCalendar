package volo.voloCalendar.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import volo.voloCalendar.model.User;
import volo.voloCalendar.util.MyGrantedAuthority;


@Component(value = "loginService")
public class LoginLogic implements UserDetailsService, Serializable {
    @Autowired
    public UserManagementLogic userManagementLogic;

    private static final long serialVersionUID = 1L;

    public UserDetails loadUserByUsername(String userEmail)
            throws UsernameNotFoundException {
        try {
            User user = userManagementLogic.getUserByEmail(userEmail);
            if (user == null || user.isDeleted()) {
                return null;
            }
            List<GrantedAuthority> authorities = addAccordingRoleByUserType(user
                    .isAdmin() ? "ROLE_ADMIN" : "ROLE_DRIVER");
            org.springframework.security.core.userdetails.User springUser = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                    authorities);
            return springUser;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<GrantedAuthority> addAccordingRoleByUserType(
            final String userType) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new MyGrantedAuthority(userType));
        return authorities;
    }

}
