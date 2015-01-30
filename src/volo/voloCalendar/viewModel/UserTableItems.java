package volo.voloCalendar.viewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import volo.voloCalendar.model.User;

import java.io.Serializable;

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
