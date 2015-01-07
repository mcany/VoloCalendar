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
}
