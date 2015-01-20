package volo.voloCalendar.viewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Emin Guliyev on 22/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarMonth implements Serializable { // defines a month
    private LocalDate beginDate; // begin date of the month
    private CalendarWeekLight[] calendarWeekLights; // calendarWeekLight objects of the month

    public CalendarMonth(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public CalendarWeekLight[] getCalendarWeekLights() {
        return calendarWeekLights;
    }

    public void setCalendarWeekLights(CalendarWeekLight[] calendarWeekLights) {
        this.calendarWeekLights = calendarWeekLights;
    }
}
