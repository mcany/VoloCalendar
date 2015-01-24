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


@Component(value = "loginService")
public class LoginService implements UserDetailsService, Serializable {
    @Autowired
    public Logic logic;
    private static final long serialVersionUID = 1L;

    public UserDetails loadUserByUsername(String userEmail)
            throws UsernameNotFoundException {
        try {
            User user = logic.getUserByEmail(userEmail);
            if (user == null) {
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
        GrantedAuthority authority = new GrantedAuthority() {
            private static final long serialVersionUID = 1L;

            public String getAuthority() {
                return userType;
            }
        };
        authorities.add(authority);
        return authorities;
    }

}
