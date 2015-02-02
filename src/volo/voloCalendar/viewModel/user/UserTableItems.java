package volo.voloCalendar.viewModel.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.Page;
import volo.voloCalendar.entity.User;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by Emin Guliyev on 30/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTableItems implements Serializable{
    private int allCount;
    private User[] items;

    public UserTableItems() {
    }

    public UserTableItems(int allCount, User[] items) {
        this.allCount = allCount;
        this.items = items;
    }

    public UserTableItems(Page<User> users) {
        this.allCount = (int) users.getTotalElements();
        this.items = new User[users.getSize()];
        int i = 0;
        for (User user:  users){
            if (user == null){
                break;
            }
            this.items[i++] = user;
        }
        this.items = Arrays.copyOfRange(this.items,0,i);
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public User[] getItems() {
        return items;
    }

    public void setItems(User[] items) {
        this.items = items;
    }
}
