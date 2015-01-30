package volo.voloCalendar.externalModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 30/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverHolder implements Serializable {
    private Driver driver;

    public DriverHolder() {
    }

    public DriverHolder(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
