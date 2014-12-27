package volo.voloCalendar.viewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 22/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarViewModel implements Serializable {
    private CalendarMonth[] calendarMonths;

    public CalendarMonth[] getCalendarMonths() {
        return calendarMonths;
    }

    public void setCalendarMonths(CalendarMonth[] calendarMonths) {
        this.calendarMonths = calendarMonths;
    }
}
