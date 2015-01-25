package volo.voloCalendar.util;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 25/01/2015.
 */
public class MyGrantedAuthority implements GrantedAuthority, Serializable {
    private String role;
    public MyGrantedAuthority(String role){
        this.role = role;
    }
    @Override
    public String getAuthority() {
        return role;
    }
}
