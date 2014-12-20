package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Emin Guliyev on 20/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverCalendarWeek  implements Serializable {
    private String userId;
    private DayStatistics mondayStatistics;
    private DayStatistics tuesdayStatistics;
    private DayStatistics wednesdayStatistics;
    private DayStatistics thursdayStatistics;
    private DayStatistics fridayStatistics;
    private DayStatistics saturdayStatistics;
    private DayStatistics sundayStatistics;

    public DriverCalendarWeek() {
    }

    public DriverCalendarWeek(DriverCalendarWeek driverCalendarWeek, LocalDate beginDate) {
        userId = driverCalendarWeek.userId;
        mondayStatistics = driverCalendarWeek.mondayStatistics;
        mondayStatistics.setDate(beginDate);
        tuesdayStatistics = driverCalendarWeek.tuesdayStatistics;
        tuesdayStatistics.setDate(mondayStatistics.getDate().plusDays(1));
        wednesdayStatistics = driverCalendarWeek.wednesdayStatistics;
        tuesdayStatistics.setDate(tuesdayStatistics.getDate().plusDays(1));
        thursdayStatistics = driverCalendarWeek.thursdayStatistics;
        tuesdayStatistics.setDate(wednesdayStatistics.getDate().plusDays(1));
        fridayStatistics = driverCalendarWeek.fridayStatistics;
        tuesdayStatistics.setDate(thursdayStatistics.getDate().plusDays(1));
        saturdayStatistics = driverCalendarWeek.saturdayStatistics;
        tuesdayStatistics.setDate(fridayStatistics.getDate().plusDays(1));
        sundayStatistics = driverCalendarWeek.sundayStatistics;
        tuesdayStatistics.setDate(saturdayStatistics.getDate().plusDays(1));
    }

    public DriverCalendarWeek(String userId, LocalDate beginDate) {
        this.userId = userId;
        mondayStatistics = new DayStatistics(beginDate);
        tuesdayStatistics = new DayStatistics(mondayStatistics.getDate().plusDays(1));
        wednesdayStatistics = new DayStatistics(tuesdayStatistics.getDate().plusDays(1));
        thursdayStatistics = new DayStatistics(wednesdayStatistics.getDate().plusDays(1));
        fridayStatistics = new DayStatistics(thursdayStatistics.getDate().plusDays(1));
        saturdayStatistics = new DayStatistics(fridayStatistics.getDate().plusDays(1));
        sundayStatistics = new DayStatistics(saturdayStatistics.getDate().plusDays(1));
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public DayStatistics getMondayStatistics() {
        return mondayStatistics;
    }

    public void setMondayStatistics(DayStatistics mondayStatistics) {
        this.mondayStatistics = mondayStatistics;
    }

    public DayStatistics getTuesdayStatistics() {
        return tuesdayStatistics;
    }

    public void setTuesdayStatistics(DayStatistics tuesdayStatistics) {
        this.tuesdayStatistics = tuesdayStatistics;
    }

    public DayStatistics getWednesdayStatistics() {
        return wednesdayStatistics;
    }

    public void setWednesdayStatistics(DayStatistics wednesdayStatistics) {
        this.wednesdayStatistics = wednesdayStatistics;
    }

    public DayStatistics getThursdayStatistics() {
        return thursdayStatistics;
    }

    public void setThursdayStatistics(DayStatistics thursdayStatistics) {
        this.thursdayStatistics = thursdayStatistics;
    }

    public DayStatistics getFridayStatistics() {
        return fridayStatistics;
    }

    public void setFridayStatistics(DayStatistics fridayStatistics) {
        this.fridayStatistics = fridayStatistics;
    }

    public DayStatistics getSaturdayStatistics() {
        return saturdayStatistics;
    }

    public void setSaturdayStatistics(DayStatistics saturdayStatistics) {
        this.saturdayStatistics = saturdayStatistics;
    }

    public DayStatistics getSundayStatistics() {
        return sundayStatistics;
    }

    public void setSundayStatistics(DayStatistics sundayStatistics) {
        this.sundayStatistics = sundayStatistics;
    }
}
