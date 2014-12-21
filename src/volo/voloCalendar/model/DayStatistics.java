package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Emin Guliyev on 20/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DayStatistics  implements Serializable {
    private LocalDate date;
    private HourStatistics[] hourStatisticsArray;

    public DayStatistics() {
        hourStatisticsArray = new HourStatistics[24];
        for (int i=0; i < hourStatisticsArray.length; i++){
            hourStatisticsArray[i] = new HourStatistics();
        }
    }

    public DayStatistics(LocalDate date) {
        this();
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public HourStatistics[] getHourStatisticsArray() {
        return hourStatisticsArray;
    }

    public void setHourStatisticsArray(HourStatistics[] hourStatisticsArray) {
        this.hourStatisticsArray = hourStatisticsArray;
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
