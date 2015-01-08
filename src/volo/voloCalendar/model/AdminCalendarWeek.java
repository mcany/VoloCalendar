package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.io.Serializable;
import java.time.LocalDate;

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
        int lengthOfMonth = beginDate.getMonth().length(beginDate.isLeapYear());
        adminDayStatisticsArray = new AdminDayStatistics[lengthOfMonth];
        for (int i = 0; i < lengthOfMonth; i++){
            adminDayStatisticsArray[i] = new AdminDayStatistics(beginDate.plusDays(i));
        }
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

    public int getPlanningHours(){
        int result = 0;
        for (AdminDayStatistics adminDayStatistics: adminDayStatisticsArray){
            result += adminDayStatistics.getPlanningHours();
        }
        return result;
    }
}
