package volo.voloCalendar.externalModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import volo.voloCalendar.entity.User;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 30/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Drivers implements Serializable {
    private Driver[] drivers;

    public Drivers() {
    }

    public Driver[] getDrivers() {
        return drivers;
    }

    public void setDrivers(Driver[] drivers) {
        this.drivers = drivers;
    }

    public User[] convertToUserArray() {
        if (drivers == null){
            return new User[0];
        }
        User[] result = new User[drivers.length];
        for (int i=0; i < drivers.length; i++){
            Driver driver = drivers[i];
            result[i] = driver.convertToUser();
        }
        return result;
    }
}
