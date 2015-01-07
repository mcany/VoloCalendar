package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 07/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminHourStatistics extends HourStatistics implements Serializable{
    private int doneHours;
    public AdminHourStatistics(){}

    public AdminHourStatistics(DriverDayStatistics dayStatistics) {
        super(dayStatistics);
    }

    public AdminHourStatistics(int requiredDriverCount, DriverDayStatistics dayStatistics) {
        super(requiredDriverCount, dayStatistics);
    }

    public int getDoneHours() {
        return doneHours;
    }

    public void setDoneHours(int doneHours) {
        this.doneHours = doneHours;
    }
}
