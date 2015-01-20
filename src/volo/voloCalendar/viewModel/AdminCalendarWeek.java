package volo.voloCalendar.viewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import volo.voloCalendar.model.DriverCalendarWeek;
import volo.voloCalendar.model.HourForecast;
import volo.voloCalendar.model.ManualForecasting;
import volo.voloCalendar.model.User;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Emin Guliyev on 07/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminCalendarWeek implements Serializable{//defines current situation of the week for admin
    private LocalDate beginDate; // begin date of the week
    private AdminDayStatistics[] adminDayStatisticsArray; //dayStatistics objects for the week

    public AdminCalendarWeek() {
    }

    public AdminCalendarWeek(LocalDate beginDate) {
        this.beginDate = beginDate;
        LocalDate date = LocalDate.of(beginDate.getYear(), beginDate.getMonthValue(), beginDate.getDayOfMonth());
        int month = date.getMonthValue();
        ArrayList<AdminDayStatistics> adminDayStatisticsArrayList = new ArrayList<AdminDayStatistics>();
        do{
            adminDayStatisticsArrayList.add(new AdminDayStatistics(date));
            date = date.plusDays(1);
        }while(date.getDayOfWeek() != DayOfWeek.MONDAY && month == date.getMonthValue());
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

    public int getDoneHours(){
        int result = 0;
        for (AdminDayStatistics adminDayStatistics: adminDayStatisticsArray){
            result += adminDayStatistics.getDoneHours();
        }
        return result;
    }

    public int getPlannedHours(){
        int result = 0;
        for (AdminDayStatistics adminDayStatistics: adminDayStatisticsArray){
            result += adminDayStatistics.getPlannedHours();
        }
        return result;
    }


    public void fixPlannedHours(ManualForecasting manualForecasting) {
        for (int i = 0; i < this.getAdminDayStatisticsArray().length; i++) {
            for (int j = 0; j < this.getAdminDayStatisticsArray()[i].getAdminHourStatisticsArray().length; j++) {
                HourForecast hourForecast = manualForecasting.getDays()[this.getAdminDayStatisticsArray()[i].getDate().getDayOfWeek().getValue() - 1][j];
                this.getAdminDayStatisticsArray()[i].getAdminHourStatisticsArray()[j].increaseRequiredHours(hourForecast.getCount());
            }
        }
    }

    public void fixDoneHours(Collection<User> allUsers) {
        for(User user: allUsers){
            if (user.getDriverCalendarWeekHashMap() != null){
                DriverCalendarWeek driverCalendarWeek = user.getDriverCalendarWeekHashMap().get(this.getBeginDate());
                if (driverCalendarWeek != null) {
                    for (int i = 0; i < driverCalendarWeek.getDayStatisticsArray().length; i++){
                        for (int j = 0; j < driverCalendarWeek.getDayStatisticsArray()[i].getHourStatisticsArray().length; j++){
                            if (driverCalendarWeek.getDayStatisticsArray()[i].getHourStatisticsArray()[j].isSelected()){
                                this.getAdminDayStatisticsArray()[i].getAdminHourStatisticsArray()[j].increaseDoneHours();
                            }
                        }
                    }
                }
            }
        }
    }
}
