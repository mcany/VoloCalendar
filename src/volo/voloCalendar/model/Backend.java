package volo.voloCalendar.model;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Emin Guliyev on 28/11/2014.
 */
public class Backend {
    private static HashMap<String, User> usersByEmail = new HashMap<String, User>();
    private static HashMap<String, User> usersById = new HashMap<String, User>();
    static {
        User emin = new User("123", "eminguliyev1987@gmail.com", "sindibad1987", "Emin", "Guliyev", true);
        User mertcan = new User("456", "mcanyigin@gmail.com", "sindibad1987", "Mertcan", "Yigin", true);
        usersByEmail.put(emin.getEmail(), emin);
        usersByEmail.put(mertcan.getEmail(), mertcan);
        usersById.put(emin.getId(), emin);
        usersById.put(mertcan.getId(), mertcan);
    }

    public static User getUserByEmail(String email){
        return usersByEmail.get(email);
    }

    public static User getUserById(String id){
        return usersById.get(id);
    }

    public static Collection<User> getAllUsers(){
        return usersByEmail.values();
    }

    public static User insertOrUpdateUser(User user){
        usersByEmail.put(user.getEmail(), user);
        usersById.put(user.getId(), user);
        return user;
    }

    public static User deleteUser(String id){
        User user = usersById.get(id);
        usersByEmail.remove(user.getEmail());
        usersById.remove(user.getId());
        return user;
    }
}
