package volo.voloCalendar.viewModel.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 20/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HourStatistics implements Serializable { // parent class for AdminHourStatistics and DriverHourStatistics, defines current situation for the hour
    protected int plannedHours; // (proper hourForecasting.count - count of all drivers who registered for the hour) if current user is driver , else proper hourForecasting.count
    protected int index; // from 0 to 23, defining index of the hour within day

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

    public void decreasePlannedHours(int count) {
        plannedHours -= count;
    }

    public void increasePlannedHours(int count) {
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
