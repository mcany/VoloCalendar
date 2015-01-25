package volo.voloCalendar.viewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import volo.voloCalendar.model.HourStatistics;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 07/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminHourStatistics extends HourStatistics implements Serializable{ // defines current situation of the day for admin
    private int doneHours; // count of all drivers who registered for the hour
    private AdminDayStatistics adminDayStatistics; // parent object that holds all adminHourStatistics(current adminHourStatistics also)

    public AdminHourStatistics(){}

    public AdminHourStatistics(AdminDayStatistics adminDayStatistics, int index) {
        this.adminDayStatistics = adminDayStatistics;
        this.index = index;
    }

    public int getDoneHours() {
        return doneHours;
    }

    public void setDoneHours(int doneHours) {
        this.doneHours = doneHours;
    }

    public boolean isEnabled() {
        return adminDayStatistics.isActive();
    }

    public void increaseDoneHours(short count) {
        doneHours+=count;
    }
}
