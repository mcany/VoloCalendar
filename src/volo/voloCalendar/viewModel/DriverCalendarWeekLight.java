package volo.voloCalendar.viewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDate;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

/**
 * Created by Emin Guliyev on 22/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverCalendarWeekLight implements Serializable{
    private LocalDate weekBeginDate;
    private LocalDate weekEndDate;

    public DriverCalendarWeekLight(){}

    public DriverCalendarWeekLight(LocalDate weekBeginDate) {
        this.weekBeginDate = weekBeginDate;
        LocalDate date = weekBeginDate.plusDays(7);
        if (date.getMonth() != weekBeginDate.getMonth()){
            date = date.with(lastDayOfMonth());
        }
        this.weekEndDate = date;
    }

    public LocalDate getWeekBeginDate() {
        return weekBeginDate;
    }

    public void setWeekBeginDate(LocalDate weekBeginDate) {
        this.weekBeginDate = weekBeginDate;
    }

    public LocalDate getWeekEndDate() {
        return weekEndDate;
    }

    public void setWeekEndDate(LocalDate weekEndDate) {
        this.weekEndDate = weekEndDate;
    }
}
