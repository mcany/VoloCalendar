package volo.voloCalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import volo.voloCalendar.service.CalendarLogic;
import volo.voloCalendar.service.UserManagement;
import volo.voloCalendar.service.UserManagementLocalLogic;
import volo.voloCalendar.viewModel.user.UserTable;
import volo.voloCalendar.entity.User;
import volo.voloCalendar.util.UtilMethods;
import volo.voloCalendar.viewModel.user.UserTableItems;

import java.util.UUID;

/**
 * Created by Emin Guliyev on 28/11/2014.
 */
@RestController
@Secured({"ROLE_ADMIN"})
@RequestMapping("/admin/users")
public class UserManagementController {
    @Autowired
    public CalendarLogic calendarLogic;
    @Autowired
    public UserManagement userManagementLogic;

    @RequestMapping(value = "/pagination", method = RequestMethod.POST, produces = "application/json")
    public UserTableItems getUsers(@RequestBody UserTable userTable) {
        UserTableItems userTableItems = calendarLogic.getUsers(userTable);
        return userTableItems;
    }

    @RequestMapping(value = "/email/{email}_", method = RequestMethod.GET, produces = "application/json")
    public Object[] getUserByEmail(@PathVariable String email) {
        User user = userManagementLogic.getUserByEmail(email);
        return UtilMethods.getArray(user);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
    public User createUser(@RequestBody User user) {
        user.setId(UUID.randomUUID().toString());
        return userManagementLogic.insertOrUpdateUser(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public User getUserById(@PathVariable String id) {
        return userManagementLogic.getUserById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json")
    public User update(@RequestBody User user) {
        return userManagementLogic.insertOrUpdateUser(user);
    }

}
