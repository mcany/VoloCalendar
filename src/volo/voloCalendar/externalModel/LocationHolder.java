package volo.voloCalendar.externalModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 30/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationHolder implements Serializable {
    private Location location;

    public LocationHolder() {
    }

    public LocationHolder(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
