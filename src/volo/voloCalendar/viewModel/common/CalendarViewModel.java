package volo.voloCalendar.viewModel.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 22/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarViewModel implements Serializable { //defines all months in main page(calendar view) of the user
    private CalendarMonth[] calendarMonths; // all calendarMonth objects of the calendar for the user

    public CalendarMonth[] getCalendarMonths() {
        return calendarMonths;
    }

    public void setCalendarMonths(CalendarMonth[] calendarMonths) {
        this.calendarMonths = calendarMonths;
    }
}
