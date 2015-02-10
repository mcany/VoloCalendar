package volo.voloCalendar.viewModel.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import volo.voloCalendar.viewModel.forecasting.HourForecast;
import volo.voloCalendar.viewModel.forecasting.ManualForecasting;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Emin Guliyev on 21/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthStatistics implements Serializable { //defines month statistics for selected month(tab) in the main page(calendar view) for the user
    private int plannedHours; // for driver user type : 45 if minijob contract , else 0; for admin user type sum of forecasting data for days in month
    private int doneHours; // for driver user type : selected hours for the month; for admin user type all selected hours by all users in the system
    private LocalDate beginDate; // begin date of month

    public MonthStatistics() {
    }

    public MonthStatistics(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public MonthStatistics(int plannedHours, int doneHours, LocalDate beginDate) {
        this.plannedHours = plannedHours;
        this.doneHours = doneHours;
        this.beginDate = beginDate;
    }

    public int getPlannedHours() {
        return plannedHours;
    }

    public void setPlannedHours(int plannedHours) {
        this.plannedHours = plannedHours;
    }

    public int getDoneHours() {
        return doneHours;
    }

    public void setDoneHours(int doneHours) {
        this.doneHours = doneHours;
    }

    public int getDiffHours() {
        return plannedHours - doneHours;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }


    public void fixPlannedHours(ManualForecasting manualForecasting) {
        int plannedHours = 0;
        int month = beginDate.getMonthValue();
        LocalDate date = LocalDate.of(beginDate.getYear(), beginDate.getMonthValue(), beginDate.getDayOfMonth());
        while (date.getMonthValue() == month) {
            plannedHours += calculatePlannedHours(manualForecasting.getDays()[date.getDayOfWeek().getValue() - 1]);
            date = date.plusDays(1);
        }
        setPlannedHours(plannedHours);
    }

    private int calculatePlannedHours(HourForecast[] hourForecasts) {
        int result = 0;
        for (HourForecast hourForecast : hourForecasts) {
            result += hourForecast.getCount();
        }
        return result;
    }

}
