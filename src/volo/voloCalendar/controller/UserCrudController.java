package volo.voloCalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import volo.voloCalendar.service.Logic;
import volo.voloCalendar.viewModel.UserTableViewModel;
import volo.voloCalendar.model.User;
import volo.voloCalendar.util.UtilMethods;

import java.util.UUID;

/**
 * Created by Emin Guliyev on 28/11/2014.
 */
@RestController
@Secured({"ROLE_ADMIN"})
@RequestMapping("/admin/users")
public class UserCrudController {
    @Autowired
    public Logic logic;
    @RequestMapping(value = "/count", method = RequestMethod.GET, produces = "application/json")
    public int getAllUsersCount() {
        return logic.getAllUsersCount();
    }

    @RequestMapping(value = "/pagination", method = RequestMethod.POST, produces = "application/json")
    public User[] getUsers(@RequestBody UserTableViewModel userTableViewModel) {
        User[] users = logic.getUsers(userTableViewModel);
        return users;
    }

    @RequestMapping(value = "/email/{email}_", method = RequestMethod.GET, produces = "application/json")
    public Object[] getUserByEmail(@PathVariable String email) {
        User user = logic.getUserByEmail(email);
        return UtilMethods.getArray(user);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
    public User createUser(@RequestBody User user) {
        user.setId(UUID.randomUUID().toString());
        return logic.insertOrUpdateUser(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public User getUserById(@PathVariable String id) {
        return logic.getUserById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json")
    public User update(@RequestBody User user) {
        return logic.insertOrUpdateUser(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public User deleteUser(@PathVariable String id) {
        return logic.deleteUser(id);
    }
}
