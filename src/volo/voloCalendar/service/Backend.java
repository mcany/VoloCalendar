package volo.voloCalendar.service;

import org.springframework.web.bind.annotation.RequestBody;
import volo.voloCalendar.model.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Emin Guliyev on 28/11/2014.
 */
public class Backend {
    private static HashMap<String, User> usersByEmail = new HashMap<String, User>();
    private static HashMap<String, User> usersById = new HashMap<String, User>();
    private static ManualForecasting manualForecasting = new ManualForecasting();
    static {
        User emin = new User("123", "eminguliyev1987@gmail.com", "sindibad1987", "Emin Guliyev");
        User mertcan = new User("456", "mcanyigin@gmail.com", "sindibad1987", "Mertcan Yigin"
                , "Christ-Probst.", "10/142", "80805", "Munich", "017678947235", TransportType.bicycle, TelephoneType.ios
                , "DE64IRCE92222212345678", "BINFFDDDXXX", ContractType.minijob);
        usersByEmail.put(emin.getEmail(), emin);
        usersById.put(emin.getId(), emin);
        usersByEmail.put(mertcan.getEmail(), mertcan);
        usersById.put(mertcan.getId(), mertcan);
        for (int i=0; i < 35; i++){
            emin = new User(emin);
            emin.setId(UUID.randomUUID().toString());
            emin.setEmail(UUID.randomUUID().toString() + "@gmail.com");
            usersByEmail.put(emin.getEmail(), emin);
            usersById.put(emin.getId(), emin);
        }

        for (int i=0; i < 35; i++){
            mertcan = new User(mertcan);
            mertcan.setId(UUID.randomUUID().toString());
            mertcan.setEmail(UUID.randomUUID().toString() + "@gmail.com");
            usersByEmail.put(mertcan.getEmail(), mertcan);
            usersById.put(mertcan.getId(), mertcan);
        }
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

    public static User[] getUsers(UserTableLogic userTableLogic){
        Collection<User> users = Backend.getAllUsers();
        User[] userArray = new User[users.size()];
        userArray = users.toArray(userArray);
        int end = userTableLogic.getBeginIndex() - 1 + userTableLogic.getMaxNumber();
        if (end >= userArray.length){
            end = userArray.length;
        }
        userArray = Arrays.copyOfRange(userArray, userTableLogic.getBeginIndex() - 1, end);
        return userArray;
    }

    public static int getAllUsersCount() {
        return getAllUsers().size();
    }

    public static ManualForecasting getManualForecasting() {
        return manualForecasting;
    }

    public static void setManualForecasting(ManualForecasting manualForecasting){
        Backend.manualForecasting = manualForecasting;
    }
}
