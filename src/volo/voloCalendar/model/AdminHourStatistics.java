package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 07/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminHourStatistics extends HourStatistics implements Serializable{
    private int doneHours;
    private AdminDayStatistics adminDayStatistics;

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

    public void increaseDoneHours() {
        doneHours++;
    }

    public boolean isEnabled() {
        return adminDayStatistics.isActive();
    }

}
