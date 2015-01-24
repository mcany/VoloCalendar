package volo.voloCalendar.viewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import volo.voloCalendar.util.Settings;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Emin Guliyev on 07/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminDayStatistics implements Serializable {// defines current situation of the day for admin
    private LocalDate date;//date of the day
    private AdminHourStatistics[] adminHourStatisticsArray;//hourStatistics for the day

    public AdminDayStatistics() {
        adminHourStatisticsArray = new AdminHourStatistics[24];
        for (int i = 0; i < adminHourStatisticsArray.length; i++){
            adminHourStatisticsArray[i] = new AdminHourStatistics(this, i);
        }
    }

    public AdminDayStatistics(LocalDate date) {
        this();
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public AdminHourStatistics[] getAdminHourStatisticsArray() {
        return adminHourStatisticsArray;
    }

    public void setAdminHourStatisticsArray(AdminHourStatistics[] adminHourStatisticsArray) {
        this.adminHourStatisticsArray = adminHourStatisticsArray;
    }

    public boolean isActive() {
        boolean result = LocalDate.now().isBefore(date.minusDays(Settings.adminRestriction));
        return result;
    }

    public int getDoneHours(){
        int result = 0;
        for (AdminHourStatistics adminHourStatistics: adminHourStatisticsArray){
            result += adminHourStatistics.getDoneHours();
        }
        return result;
    }

    public int getPlannedHours(){
        int result = 0;
        for (AdminHourStatistics adminHourStatistics: adminHourStatisticsArray){
            result += adminHourStatistics.getPlannedHours();
        }
        return result;
    }
}