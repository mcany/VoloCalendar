package volo.voloCalendar.viewModel.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import volo.voloCalendar.entity.DayStatistics;
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

    public void raiseStatistics(DayStatistics dayStatistics) {
        adminHourStatisticsArray[0].increaseDoneHours(dayStatistics.getHour0());
        adminHourStatisticsArray[1].increaseDoneHours(dayStatistics.getHour1());
        adminHourStatisticsArray[2].increaseDoneHours(dayStatistics.getHour2());
        adminHourStatisticsArray[3].increaseDoneHours(dayStatistics.getHour3());
        adminHourStatisticsArray[4].increaseDoneHours(dayStatistics.getHour4());
        adminHourStatisticsArray[5].increaseDoneHours(dayStatistics.getHour5());
        adminHourStatisticsArray[6].increaseDoneHours(dayStatistics.getHour6());
        adminHourStatisticsArray[7].increaseDoneHours(dayStatistics.getHour7());
        adminHourStatisticsArray[8].increaseDoneHours(dayStatistics.getHour8());
        adminHourStatisticsArray[9].increaseDoneHours(dayStatistics.getHour9());
        adminHourStatisticsArray[10].increaseDoneHours(dayStatistics.getHour10());
        adminHourStatisticsArray[11].increaseDoneHours(dayStatistics.getHour11());
        adminHourStatisticsArray[12].increaseDoneHours(dayStatistics.getHour12());
        adminHourStatisticsArray[13].increaseDoneHours(dayStatistics.getHour13());
        adminHourStatisticsArray[14].increaseDoneHours(dayStatistics.getHour14());
        adminHourStatisticsArray[15].increaseDoneHours(dayStatistics.getHour15());
        adminHourStatisticsArray[16].increaseDoneHours(dayStatistics.getHour16());
        adminHourStatisticsArray[17].increaseDoneHours(dayStatistics.getHour17());
        adminHourStatisticsArray[18].increaseDoneHours(dayStatistics.getHour18());
        adminHourStatisticsArray[19].increaseDoneHours(dayStatistics.getHour19());
        adminHourStatisticsArray[20].increaseDoneHours(dayStatistics.getHour20());
        adminHourStatisticsArray[21].increaseDoneHours(dayStatistics.getHour21());
        adminHourStatisticsArray[22].increaseDoneHours(dayStatistics.getHour22());
        adminHourStatisticsArray[23].increaseDoneHours(dayStatistics.getHour23());
    }
}
