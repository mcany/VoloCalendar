package volo.voloCalendar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import volo.voloCalendar.dao.UserDAO;
import volo.voloCalendar.entity.User;
import volo.voloCalendar.viewModel.user.UserTable;
import volo.voloCalendar.viewModel.user.UserTableItems;

import java.util.*;

/**
 * Created by Emin Guliyev on 25/01/2015.
 */
@Service
public class UserManagementLocalLogic {
    @Autowired
    public UserDAO userDAO;

    public boolean authenticate(String email, String password) throws HttpClientErrorException {
        User user = userDAO.findByEmailAndPassword(email, password);
        if (user == null || user.isDeleted()) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        return user.isAdmin();
    }

    public  User getUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public  User getUserById(String id) {
        return userDAO.findById(id);
    }

    public  User insertOrUpdateUser(User user) {
        user = userDAO.save(user);
        return user;
    }

    public UserTableItems getSortedFilteredPagedUsersWithoutStatistics(UserTable userTable) {
        Page<User> users = userDAO.search(userTable.getKeywordLike(), new PageRequest(userTable.getCurrentPage() - 1, userTable.getItemsPerPage(), new Sort(userTable.isReverse() ? Sort.Direction.DESC : Sort.Direction.ASC, userTable.getSortingField())));
        return new UserTableItems(users);
    }

    private List<User> getDrivers() {
        List<User> result = userDAO.findByAdmin(false);
        return result;
    }

    public List<User> getActiveDrivers() {
        List<User> result = userDAO.findByAdminAndDeleted(false, false);
        return result;
    }
}

