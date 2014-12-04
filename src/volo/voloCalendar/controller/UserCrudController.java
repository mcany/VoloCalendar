package volo.voloCalendar.controller;

import org.springframework.web.bind.annotation.*;
import volo.voloCalendar.service.Backend;
import volo.voloCalendar.model.User;
import volo.voloCalendar.util.UtilMethods;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by Emin Guliyev on 28/11/2014.
 */
@RestController
@RequestMapping("/databases/scrumdb/collections/users")
public class UserCrudController {
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public User[] getAllUsers(){
        Collection<User> users = Backend.getAllUsers();
        User[] userArray = new User[users.size()];
        return users.toArray(userArray);
    }

    @RequestMapping(value = "/email/{email}_", method = RequestMethod.GET, produces = "application/json")
    public Object[] getUserByEmail(@PathVariable String email){
        User user = Backend.getUserByEmail(email);
        return UtilMethods.getArray(user);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
    public User createUser(@RequestBody User user){
        user.setId(UUID.randomUUID().toString());
        return Backend.insertOrUpdateUser(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public User getUserById(@PathVariable String id){
        return Backend.getUserById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json")
    public User update(@RequestBody User user){
        return Backend.insertOrUpdateUser(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public User deleteUser(@PathVariable String id){
        return Backend.deleteUser(id);
    }
}
