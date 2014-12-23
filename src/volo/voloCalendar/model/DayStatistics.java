package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Emin Guliyev on 20/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DayStatistics  implements Serializable {
    private LocalDate date;
    private HourStatistics[] hourStatisticsArray;
    public static final int changeLimit = 3;

    public DayStatistics() {
        hourStatisticsArray = new HourStatistics[24];
        for (int i=0; i < hourStatisticsArray.length; i++){
            hourStatisticsArray[i] = new HourStatistics(this);
        }
    }

    public DayStatistics(LocalDate date) {
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

    public HourStatistics[] getHourStatisticsArray() {
        return hourStatisticsArray;
    }

    public void setHourStatisticsArray(HourStatistics[] hourStatisticsArray) {
        this.hourStatisticsArray = hourStatisticsArray;
    }

    public boolean isActive(){
        boolean result = LocalDate.now().isBefore(date.minusDays(changeLimit));
        return result;
    }

    @JsonIgnore
    public int getDoneHours() {
        int result = 0;
        for(HourStatistics hourStatistics: hourStatisticsArray){
            if (hourStatistics.isSelected()){
                result++;
            }
        }
        return result;
    }
}
