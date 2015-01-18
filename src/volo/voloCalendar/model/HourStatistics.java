package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 20/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HourStatistics implements Serializable {
    protected int plannedHours;
    protected int index;

    public HourStatistics() {
    }

    public int getPlannedHours() {
        return plannedHours;
    }

    public void setPlannedHours(int plannedHours) {
        this.plannedHours = plannedHours;
    }

    public void decreasePlannedHours() {
        plannedHours--;
    }

    public void increaseRequiredHours(int count) {
        plannedHours += count;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void increasePlannedHours() {
        plannedHours++;
    }
}
