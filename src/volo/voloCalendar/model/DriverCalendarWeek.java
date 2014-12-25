package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Emin Guliyev on 20/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverCalendarWeek implements Serializable {
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

    public void fillDayStatisticsArray(DriverCalendarWeek driverCalendarWeek) {
        LocalDate date = LocalDate.of(this.beginDate.getYear(), this.beginDate.getMonth(), this.beginDate.getDayOfMonth());
        ArrayList<DayStatistics> list = new ArrayList<DayStatistics>();
        do {
            DayStatistics dayStatistics = null;
            if (LocalDate.now().isBefore(date.minusDays(DayStatistics.changeLimit))) {
                if (driverCalendarWeek != null) {
                    dayStatistics = getDayStatistics(driverCalendarWeek, date);
                } else {
                    dayStatistics = new DayStatistics(date);
                }
            }else{
                if (this.dayStatisticsArray != null && this.dayStatisticsArray.length > 0) {
                    dayStatistics = getDayStatistics(this, date);
                }else {
                    dayStatistics = new DayStatistics(date);
                }
            }
            list.add(dayStatistics);
            date = date.plusDays(1);
        } while (date.getDayOfWeek() != DayOfWeek.MONDAY && date.getMonth() == this.beginDate.getMonth());
        dayStatisticsArray = list.toArray(new DayStatistics[list.size()]);
    }

    public DayStatistics getDayStatistics(DriverCalendarWeek driverCalendarWeekTemplate, LocalDate date) {
        DayStatistics dayStatistics;
        int numberOfFirstDayInTemplateWeek = driverCalendarWeekTemplate.dayStatisticsArray[0].getDate().getDayOfWeek().getValue();
        int indexOfProperDayInTemplateWeek = date.getDayOfWeek().getValue() - numberOfFirstDayInTemplateWeek;
        if (indexOfProperDayInTemplateWeek > -1 && driverCalendarWeekTemplate.dayStatisticsArray.length > indexOfProperDayInTemplateWeek) {
            dayStatistics = driverCalendarWeekTemplate.dayStatisticsArray[indexOfProperDayInTemplateWeek];
            dayStatistics.setDate(date);
        } else {
            dayStatistics = new DayStatistics(date);
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

    public DayStatistics[] getDayStatisticsArray() {
        return dayStatisticsArray;
    }

    public void setDayStatisticsArray(DayStatistics[] dayStatisticsArray) {
        this.dayStatisticsArray = dayStatisticsArray;
    }

    public void setRequiredDriverCountStatistics(ManualForecasting manualForecasting) {
        for (DayStatistics dayStatistics : dayStatisticsArray) {
            HourForecast[] forecastForSameDayOfWeek = manualForecasting.getDays()[dayStatistics.getDate().getDayOfWeek().getValue() - 1];
            for (int i = 0; i < 24; i++) {
                HourStatistics hourStatistics = dayStatistics.getHourStatisticsArray()[i];
                hourStatistics.setRequiredDriverCount(forecastForSameDayOfWeek[i].getCount());
            }
        }
    }

    public void subtractStatistics(DriverCalendarWeek driverCalendarWeek) {
        for (DayStatistics dayStatistics : dayStatisticsArray) {
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
        for (DayStatistics dayStatistics : dayStatisticsArray) {
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
        for (DayStatistics dayStatistics : dayStatisticsArray) {
            result += dayStatistics.getDoneHours();
        }
        return result;
    }

    public void init() {
        for (DayStatistics dayStatistics : dayStatisticsArray) {
            for (HourStatistics hourStatistics : dayStatistics.getHourStatisticsArray()) {
                hourStatistics.init(dayStatistics);
            }
        }
    }

    public int getSelectedHoursCount() {
        int result = 0;
        for (DayStatistics dayStatistics : dayStatisticsArray) {
            for (HourStatistics hourStatistics : dayStatistics.getHourStatisticsArray()) {
                if (hourStatistics.isSelected()) {
                    result++;
                }
            }
        }
        return result;
    }
}
