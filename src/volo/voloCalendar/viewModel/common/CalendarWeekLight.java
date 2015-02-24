package volo.voloCalendar.viewModel.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.LocalDate;
import volo.voloCalendar.util.UtilMethods;

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
        LocalDate localDate = beginDate.plusDays(7 - beginDate.getDayOfWeek());
        if (localDate.getMonthOfYear() != beginDate.getMonthOfYear()) {
            Date date = UtilMethods.getLastDayOfMonth(beginDate.getMonthOfYear(), beginDate.getYear());
            localDate = new LocalDate(date.getYear(), date.getMonth() + 1, date.getDate());
        }
        this.endDate = localDate;
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
