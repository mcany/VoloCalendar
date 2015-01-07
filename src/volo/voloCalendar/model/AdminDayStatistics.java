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
public class AdminDayStatistics implements Serializable {
    public static final int changeLimit = 1;
    private LocalDate date;
    private AdminHourStatistics[] adminHourStatisticsArray;
    public AdminDayStatistics() {
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
        boolean result = LocalDate.now().isBefore(date.minusDays(changeLimit));
        return result;
    }
}
