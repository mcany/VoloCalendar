package volo.voloCalendar.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import volo.voloCalendar.model.ContractType;
import volo.voloCalendar.model.TelephoneType;
import volo.voloCalendar.model.TransportType;
import volo.voloCalendar.model.User;
import volo.voloCalendar.viewModel.UserTable;
import volo.voloCalendar.viewModel.UserTableItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Emin Guliyev on 25/01/2015.
 */
@Service
public class UserManagementLocalLogic {

    private static HashMap<String, User> usersByEmail = new HashMap<String, User>();
    private static HashMap<String, User> usersById = new HashMap<String, User>();
    static {
        User emin = new User("123", "eminguliyev1987@gmail.com", "1", "Emin Guliyev");
        User mertcan = new User(2, "456", "dr_ilashka@live.ru", "1", "Mertcan Yigin"
                , "Christ-Probst.", "10/142", "80805", "Munich", "017678947235", TransportType.bicycle, TelephoneType.ios
                , "DE64IRCE92222212345678", "BINFFDDDXXX", ContractType.minijob);
        usersByEmail.put(emin.getEmail(), emin);
        usersById.put(emin.getId(), emin);
        usersByEmail.put(mertcan.getEmail(), mertcan);
        usersById.put(mertcan.getId(), mertcan);
        for (int i = 0; i < 35; i++) {
            User emin1 = new User(emin);
            emin1.setId(emin.getId().replace(' ', '_') + "_" + i);
            emin1.setEmail(i + emin.getEmail());
            usersByEmail.put(emin1.getEmail(), emin1);
            usersById.put(emin1.getId(), emin1);
        }

        for (int i = 0; i < 35; i++) {
            User mertcan1 = new User(mertcan);
            mertcan1.setId(mertcan.getId().replace(' ', '_') + "_" + i);
            mertcan1.setEmail(i + mertcan.getEmail());
            usersByEmail.put(mertcan1.getEmail(), mertcan1);
            usersById.put(mertcan1.getId(), mertcan1);
        }
    }

    //rest
    public boolean authenticate(String email, String password) throws HttpClientErrorException {
        User user = usersByEmail.get(email);
        if (user == null || user.isDeleted()
                || (user.getPassword() == null) || (password == null)
                || !user.getPassword().equals(password)) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        return user.isAdmin();
    }

    //rest
    public  User getUserByEmail(String email) {
        return usersByEmail.get(email);
    }

    //rest
    public  User getUserById(String id) {
        return usersById.get(id);
    }

    //rest
    public  User insertOrUpdateUser(User user) {
        User userInDb = getUserById(user.getId());
        usersByEmail.put(user.getEmail(), user);
        usersById.put(user.getId(), user);
        return user;
    }

    //rest
    public UserTableItems getSortedFilteredPagedUsersWithoutStatistics(UserTable userTable) {
        Collection<User> users = usersById.values();
        User[] userArray = new User[users.size()];
        userArray = users.toArray(userArray);
        int end = userTable.getItemsPerPage()*(userTable.getCurrentPage() - 1) + userTable.getItemsPerPage();
        if (end >= userArray.length) {
            end = userArray.length;
        }
        userArray = Arrays.copyOfRange(userArray, userTable.getItemsPerPage()*(userTable.getCurrentPage() - 1), end);
        return new UserTableItems(users.size(), userArray);
    }

    //rest
    public User[] getActiveDrivers() {
        Collection<User> all = usersById.values();
        ArrayList<User> result = new ArrayList<User>();
        for (User user : all){
            if (!user.isAdmin() && !user.isDeleted()){
                result.add(user);
            }
        }
        return result.toArray(new User[result.size()]);
    }
}

