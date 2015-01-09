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
public class DetailedAdminDayStatistics implements Serializable {
    private LocalDate date;
    private DetailedDriverDayStatistics[] detailedDriverDayStatisticsArray;
    public DetailedAdminDayStatistics(){}
    public DetailedAdminDayStatistics(LocalDate date) {
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

    public DetailedDriverDayStatistics[] getDetailedDriverDayStatisticsArray() {
        return detailedDriverDayStatisticsArray;
    }

    public void setDetailedDriverDayStatisticsArray(DetailedDriverDayStatistics[] detailedDriverDayStatisticsArray) {
        this.detailedDriverDayStatisticsArray = detailedDriverDayStatisticsArray;
    }

    public void init() {
        for (DetailedDriverDayStatistics detailedDriverDayStatistics: detailedDriverDayStatisticsArray){
            detailedDriverDayStatistics.init();
        }
    }
}
