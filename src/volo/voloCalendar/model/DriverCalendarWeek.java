package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Emin Guliyev on 20/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverCalendarWeek  implements Serializable {
    private String userId;
    private LocalDate beginDate;
    private DayStatistics[] dayStatisticsArray;

    public DriverCalendarWeek() {
    }

    public DriverCalendarWeek(DriverCalendarWeek driverCalendarWeek, LocalDate beginDate) {
        this.userId = driverCalendarWeek.userId;
        this.beginDate = beginDate;
        fillDayStatisticsArray(driverCalendarWeek);
    }

    public DriverCalendarWeek(String userId, LocalDate beginDate) {
        this.userId = userId;
        this.beginDate = beginDate;
        fillDayStatisticsArray(null);
    }

    private void fillDayStatisticsArray(DriverCalendarWeek driverCalendarWeek) {
        LocalDate date = LocalDate.of(this.beginDate.getYear(), this.beginDate.getMonth(), this.beginDate.getDayOfMonth());
        ArrayList<DayStatistics> list = new ArrayList<DayStatistics>();
        do{
            DayStatistics dayStatistics = null;
            if (driverCalendarWeek == null){
                dayStatistics = new DayStatistics(date);
            }else{
                dayStatistics = driverCalendarWeek.dayStatisticsArray[date.getDayOfWeek().getValue() - 1];
                dayStatistics.setDate(date);
            }
            list.add(dayStatistics);
            date = date.plusDays(1);
        }while(date.getDayOfWeek() != DayOfWeek.MONDAY && date.getMonth() == this.beginDate.getMonth());
        dayStatisticsArray = list.toArray(new DayStatistics[list.size()]);
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public DayStatistics[] getDayStatisticsArray() {
        return dayStatisticsArray;
    }

    public void setDayStatisticsArray(DayStatistics[] dayStatisticsArray) {
        this.dayStatisticsArray = dayStatisticsArray;
    }

    public void setRequiredDriverCountStatistics(ManualForecasting manualForecasting) {
        for(DayStatistics dayStatistics:dayStatisticsArray){
            HourForecast[] forecastForSameDayOfWeek = manualForecasting.getDays()[dayStatistics.getDate().getDayOfWeek().getValue() - 1];
            for (int i = 0; i < 24; i++){
                HourStatistics hourStatistics = dayStatistics.getHourStatisticsArray()[i];
                hourStatistics.setRequiredDriverCount(forecastForSameDayOfWeek[i].getCount());
            }
        }
    }

    public void subtractStatistics(DriverCalendarWeek driverCalendarWeek) {
        for (DayStatistics dayStatistics:dayStatisticsArray){
            DayStatistics sameDayOfWeekStatistics = driverCalendarWeek.getDayStatistics(dayStatistics.getDate().getDayOfWeek());
            if (sameDayOfWeekStatistics != null) {
                for (int i = 0; i < 24; i++) {
                    HourStatistics hourStatistics = dayStatistics.getHourStatisticsArray()[i];
                    if (sameDayOfWeekStatistics.getHourStatisticsArray()[i].isSelected()) {
                        hourStatistics.decreaseRequiredDriverCount();
                    }
                }
            }
        }
    }

    private DayStatistics getDayStatistics(DayOfWeek dayOfWeek) {
        for (DayStatistics dayStatistics: dayStatisticsArray){
            if (dayStatistics.getDate().getDayOfWeek() == dayOfWeek){
                return dayStatistics;
            }
            if (dayStatistics.getDate().getDayOfWeek().getValue() > dayOfWeek.getValue()){
                break;
            }
        }
        return null;
    }
    @JsonIgnore
    public int getDoneHours() {
        int result = 0;
        for (DayStatistics dayStatistics: dayStatisticsArray){
            result += dayStatistics.getDoneHours();
        }
        return result;
    }
}
