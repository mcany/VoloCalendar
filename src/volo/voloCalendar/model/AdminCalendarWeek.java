package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Emin Guliyev on 07/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminCalendarWeek implements Serializable{
    private LocalDate beginDate;
    private AdminDayStatistics[] adminDayStatisticsArray;

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
}
