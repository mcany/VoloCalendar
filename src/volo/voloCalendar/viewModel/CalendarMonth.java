package volo.voloCalendar.viewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Emin Guliyev on 22/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarMonth implements Serializable{
    private LocalDate monthBeginDate;
    private DriverCalendarWeekLight[] driverCalendarWeekLights;

    public CalendarMonth(LocalDate monthBeginDate) {
        this.monthBeginDate = monthBeginDate;
    }

    public LocalDate getMonthBeginDate() {
        return monthBeginDate;
    }

    public void setMonthBeginDate(LocalDate monthBeginDate) {
        this.monthBeginDate = monthBeginDate;
    }

    public DriverCalendarWeekLight[] getDriverCalendarWeekLights() {
        return driverCalendarWeekLights;
    }

    public void setDriverCalendarWeekLights(DriverCalendarWeekLight[] driverCalendarWeekLights) {
        this.driverCalendarWeekLights = driverCalendarWeekLights;
    }
}
