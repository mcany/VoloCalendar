package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 07/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverHourStatistics extends HourStatistics implements Serializable {// defines current situation of the hour for driver
    private boolean selected; //if the hour selected by the driver whom dayStatistics field belongs
    private DriverDayStatistics dayStatistics; // parent object that holds all driverHourStatistics(current driverHourStatistics also)

    public DriverHourStatistics() {
    }

    public DriverHourStatistics(DriverDayStatistics dayStatistics, int index) {
        this.dayStatistics = dayStatistics;
        this.index = index;
    }

    public DriverHourStatistics( DriverDayStatistics dayStatistics, int plannedHours, int index) {
        this(dayStatistics, index);
        this.plannedHours = plannedHours;
    }

    public DriverHourStatistics(DriverDayStatistics dayStatistics, int plannedHours, int index, boolean selected) {
        this(dayStatistics, plannedHours, index);
        this.selected = selected;
    }

    public void init(DriverDayStatistics dayStatistics) {
        this.dayStatistics = dayStatistics;
    }

    public boolean isEnabled() {
        return dayStatistics.isActive();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setDayStatistics(DriverDayStatistics dayStatistics) {
        this.dayStatistics = dayStatistics;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public DriverHourStatistics copy() {
        return new DriverHourStatistics(this.dayStatistics, this.plannedHours, this.index, this.selected);
    }
}
