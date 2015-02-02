package volo.voloCalendar.viewModel.driver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import volo.voloCalendar.entity.DayStatistics;
import volo.voloCalendar.util.Settings;
import volo.voloCalendar.viewModel.forecasting.HourForecast;
import volo.voloCalendar.viewModel.forecasting.ManualForecasting;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emin Guliyev on 20/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverCalendarWeek implements Serializable {//defines current situation of the week for driver
    private boolean existsInDb;
    private String userId; // driver id
    private LocalDate beginDate; //begin date of the week
    private DriverDayStatistics[] dayStatisticsArray;// driverDayStatistics objects for the week

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

    public void fillDayStatisticsArray(DriverCalendarWeek driverCalendarWeek) {
        LocalDate date = LocalDate.of(this.beginDate.getYear(), this.beginDate.getMonth(), this.beginDate.getDayOfMonth());
        ArrayList<DriverDayStatistics> list = new ArrayList<DriverDayStatistics>();
        do {
            DriverDayStatistics dayStatistics = null;
            if (LocalDate.now().isBefore(date.minusDays(Settings.driverRestriction))) {
                if (driverCalendarWeek != null) {
                    dayStatistics = getDayStatistics(driverCalendarWeek, date);
                } else {
                    dayStatistics = new DriverDayStatistics(date);
                }
            } else {
                if (this.dayStatisticsArray != null && this.dayStatisticsArray.length > 0) {
                    dayStatistics = getDayStatistics(this, date);
                } else {
                    dayStatistics = new DriverDayStatistics(date);
                }
            }
            list.add(dayStatistics);
            date = date.plusDays(1);
        } while (date.getDayOfWeek() != DayOfWeek.MONDAY && date.getMonth() == this.beginDate.getMonth());
        dayStatisticsArray = list.toArray(new DriverDayStatistics[list.size()]);
    }

    public DriverDayStatistics getDayStatistics(DriverCalendarWeek driverCalendarWeekTemplate, LocalDate date) {
        DriverDayStatistics dayStatistics;
        int numberOfFirstDayInTemplateWeek = driverCalendarWeekTemplate.dayStatisticsArray[0].getDate().getDayOfWeek().getValue();
        int indexOfProperDayInTemplateWeek = date.getDayOfWeek().getValue() - numberOfFirstDayInTemplateWeek;
        if (indexOfProperDayInTemplateWeek > -1 && driverCalendarWeekTemplate.dayStatisticsArray.length > indexOfProperDayInTemplateWeek) {
            dayStatistics = new DriverDayStatistics(driverCalendarWeekTemplate.dayStatisticsArray[indexOfProperDayInTemplateWeek]);
            dayStatistics.setDate(date);
        } else {
            dayStatistics = new DriverDayStatistics(date);
        }
        return dayStatistics;
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

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public DriverDayStatistics[] getDayStatisticsArray() {
        return dayStatisticsArray;
    }

    public void setDayStatisticsArray(DriverDayStatistics[] dayStatisticsArray) {
        this.dayStatisticsArray = dayStatisticsArray;
    }
    @JsonIgnore
    public boolean getExistsInDb() {
        return existsInDb;
    }

    public void setExistsInDb(boolean existsInDb) {
        this.existsInDb = existsInDb;
    }

    public void fixPlannedHours(ManualForecasting manualForecasting) {
        for (DriverDayStatistics dayStatistics : dayStatisticsArray) {
            HourForecast[] forecastForSameDayOfWeek = manualForecasting.getDays()[dayStatistics.getDate().getDayOfWeek().getValue() - 1];
            for (int i = 0; i < 24; i++) {
                DriverHourStatistics hourStatistics = dayStatistics.getHourStatisticsArray()[i];
                hourStatistics.setPlannedHours(forecastForSameDayOfWeek[i].getCount());
            }
        }
    }

    public void subtractStatistics(List<DayStatistics> dayStatisticsList) {
        if (dayStatisticsList == null || dayStatisticsList.size() == 0){
            return;
        }
        int dayOfWeekForFirstDay = dayStatisticsList.get(0).getWeekDayIndex();
        for (DriverDayStatistics driverDayStatistics : dayStatisticsArray) {
            int indexOfSameDayOfWeek = driverDayStatistics.getDate().getDayOfWeek().getValue() - dayOfWeekForFirstDay;
            if (indexOfSameDayOfWeek < 0 || indexOfSameDayOfWeek >= dayStatisticsList.size()){
                continue;
            }
            DayStatistics sameDayOfWeekStatistics = dayStatisticsList.get(indexOfSameDayOfWeek);
            driverDayStatistics.subtractStatistics(sameDayOfWeekStatistics);
        }
    }

    private DriverDayStatistics getDayStatistics(DayOfWeek dayOfWeek) {
        for (DriverDayStatistics dayStatistics : dayStatisticsArray) {
            if (dayStatistics.getDate().getDayOfWeek() == dayOfWeek) {
                return dayStatistics;
            }
            if (dayStatistics.getDate().getDayOfWeek().getValue() > dayOfWeek.getValue()) {
                break;
            }
        }
        return null;
    }

    @JsonIgnore
    public int getDoneHours() {
        int result = 0;
        for (DriverDayStatistics dayStatistics : dayStatisticsArray) {
            result += dayStatistics.getDoneHours();
        }
        return result;
    }

    public void init() {
        for (DriverDayStatistics dayStatistics : dayStatisticsArray) {
            dayStatistics.init(this.getUserId());
        }
    }

    public int getSelectedHoursCount() {
        int result = 0;
        for (DriverDayStatistics dayStatistics : dayStatisticsArray) {
            for (DriverHourStatistics hourStatistics : dayStatistics.getHourStatisticsArray()) {
                if (hourStatistics.isSelected()) {
                    result++;
                }
            }
        }
        return result;
    }
    @JsonIgnore
    public boolean isNotEmpty() {
        for (DriverDayStatistics driverDayStatistics: dayStatisticsArray){
            if (driverDayStatistics.isNotEmpty()){
                return true;
            }
        }
        return false;
    }

    public void fixDoneHours(List<DayStatistics> dayStatisticsList) {
        int dayOfWeekForFirstDay = dayStatisticsList.get(0).getWeekDayIndex();
        for (DriverDayStatistics driverDayStatistics : dayStatisticsArray) {
            int indexOfSameDayOfWeek = driverDayStatistics.getDate().getDayOfWeek().getValue() - dayOfWeekForFirstDay;
            if (indexOfSameDayOfWeek < 0 || indexOfSameDayOfWeek >= dayStatisticsList.size()){
                continue;
            }
            DayStatistics sameDayOfWeekStatistics = dayStatisticsList.get(indexOfSameDayOfWeek);
            driverDayStatistics.fixHourStatisticsArray(sameDayOfWeekStatistics);
        }
    }
}
