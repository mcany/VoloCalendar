package volo.voloCalendar.service;

import org.springframework.web.client.HttpClientErrorException;
import volo.voloCalendar.entity.User;
import volo.voloCalendar.viewModel.user.UserTable;
import volo.voloCalendar.viewModel.user.UserTableItems;

import java.util.List;

/**
 * Created by Emin Guliyev on 08/02/2015.
 */
public interface UserManagement {
    public boolean authenticate(String email, String password) throws HttpClientErrorException;

    public UserTableItems getSortedFilteredPagedUsersWithoutStatistics(UserTable userTable, boolean isKeywordGlobal);

    public User getUserById(String id);

    public User getUserByEmail(String email);

    public List<User> getActiveDrivers();

    public User insertOrUpdateUser(User user);

}
