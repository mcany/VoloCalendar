package volo.voloCalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import volo.voloCalendar.service.Logic;
import volo.voloCalendar.model.User;
import volo.voloCalendar.service.LoginService;
import volo.voloCalendar.util.UtilMethods;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Created by Emin Guliyev on 28/11/2014.
 */
@RestController
public class LoginController {
    @Autowired
    public Logic logic;
    @Autowired
    LoginService loginService;

    //private static String email;
    @RequestMapping(value = "/current-user", method = RequestMethod.GET, produces = "application/json")
    public HashMap<String, User> currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return UtilMethods.getHashMap(logic.getUserByEmail(email));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public HashMap<String, User> login(@RequestBody User user, HttpServletRequest request) {
        UserDetails userDetails = loginService.loadUserByUsername(user.getEmail());
        if (userDetails != null
                && (user.getPassword() != null && userDetails.getPassword() != null
                && user.getPassword().equals(userDetails.getPassword()))) {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

            // Authenticate the user
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authRequest);

            // Create a new session and add the security context.
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            return UtilMethods.getHashMap(logic.getUserByEmail(user.getEmail()));
        } else {
            return UtilMethods.getHashMap(null);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json")
    @Secured({"ROLE_ADMIN", "ROLE_DRIVER"})
    public void logout(HttpServletRequest httpServletRequest) throws ServletException {
        SecurityContextHolder.clearContext();
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST, produces = "application/json")
    @Secured({"ROLE_ADMIN", "ROLE_DRIVER"})
    public User updateProfile(@RequestBody User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = logic.getUserByEmail(email);
        if (user != null && user.equals(currentUser)){
            logic.insertOrUpdateUser(user);
            return user;
        }else {
            return null;
        }

    }
}
