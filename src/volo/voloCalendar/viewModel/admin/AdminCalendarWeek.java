package volo.voloCalendar.viewModel.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import volo.voloCalendar.entity.DayStatistics;
import volo.voloCalendar.viewModel.forecasting.HourForecast;
import volo.voloCalendar.viewModel.forecasting.ManualForecasting;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emin Guliyev on 07/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminCalendarWeek implements Serializable {//defines current situation of the week for admin
    private LocalDate beginDate; // begin date of the week
    private AdminDayStatistics[] adminDayStatisticsArray; //dayStatistics objects for the week

    public AdminCalendarWeek() {
    }

    public AdminCalendarWeek(LocalDate beginDate) {
        this.beginDate = beginDate;
        LocalDate date = LocalDate.of(beginDate.getYear(), beginDate.getMonthValue(), beginDate.getDayOfMonth());
        int month = date.getMonthValue();
        ArrayList<AdminDayStatistics> adminDayStatisticsArrayList = new ArrayList<AdminDayStatistics>();
        do {
            adminDayStatisticsArrayList.add(new AdminDayStatistics(date));
            date = date.plusDays(1);
        } while (date.getDayOfWeek() != DayOfWeek.MONDAY && month == date.getMonthValue());
        setAdminDayStatisticsArray(adminDayStatisticsArrayList.toArray(new AdminDayStatistics[adminDayStatisticsArrayList.size()]));
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public AdminDayStatistics[] getAdminDayStatisticsArray() {
        return adminDayStatisticsArray;
    }

    public void setAdminDayStatisticsArray(AdminDayStatistics[] adminDayStatisticsArray) {
        this.adminDayStatisticsArray = adminDayStatisticsArray;
    }

    public int getDoneHours() {
        int result = 0;
        for (AdminDayStatistics adminDayStatistics : adminDayStatisticsArray) {
            result += adminDayStatistics.getDoneHours();
        }
        return result;
    }

    public int getPlannedHours() {
        int result = 0;
        for (AdminDayStatistics adminDayStatistics : adminDayStatisticsArray) {
            result += adminDayStatistics.getPlannedHours();
        }
        return result;
    }


    public void fixPlannedHours(ManualForecasting manualForecasting) {
        for (int i = 0; i < this.getAdminDayStatisticsArray().length; i++) {
            for (int j = 0; j < this.getAdminDayStatisticsArray()[i].getAdminHourStatisticsArray().length; j++) {
                HourForecast hourForecast = manualForecasting.getDays()[this.getAdminDayStatisticsArray()[i].getDate().getDayOfWeek().getValue() - 1][j];
                this.getAdminDayStatisticsArray()[i].getAdminHourStatisticsArray()[j].setPlannedHours(hourForecast.getCount());
            }
        }
    }

    public void raiseStatistics(List<DayStatistics> dayStatisticsList) {
        if (dayStatisticsList == null || dayStatisticsList.size() == 0) {
            return;
        }
        int dayOfWeekForFirstDay = dayStatisticsList.get(0).getWeekDayIndex();
        for (AdminDayStatistics adminDayStatistics : adminDayStatisticsArray) {
            int indexOfSameDayOfWeek = adminDayStatistics.getDate().getDayOfWeek().getValue() - dayOfWeekForFirstDay;
            if (indexOfSameDayOfWeek < 0 || indexOfSameDayOfWeek >= dayStatisticsList.size()) {
                continue;
            }
            DayStatistics sameDayOfWeekStatistics = dayStatisticsList.get(indexOfSameDayOfWeek);
            adminDayStatistics.raiseStatistics(sameDayOfWeekStatistics);
        }
    }
}
