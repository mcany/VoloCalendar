package volo.voloCalendar.viewModel.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.io.Serializable;
import java.time.LocalDate;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

/**
 * Created by Emin Guliyev on 22/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarWeekLight implements Serializable { //defines a week
    private LocalDate beginDate; // begin date
    private LocalDate endDate; // end date

    public CalendarWeekLight() {
    }

    public CalendarWeekLight(LocalDate beginDate) {
        this.beginDate = beginDate;
        LocalDate date = beginDate.plusDays(7 - beginDate.getDayOfWeek().getValue());
        if (date.getMonth() != beginDate.getMonth()) {
            date = beginDate.with(lastDayOfMonth());
        }
        this.endDate = date;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
