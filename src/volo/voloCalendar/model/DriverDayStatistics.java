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
public class DriverDayStatistics implements Serializable {
    protected String userId;
    protected LocalDate date;
    protected DriverHourStatistics[] hourStatisticsArray;
    public static final int changeLimit = 3;

    public DriverDayStatistics() {
        hourStatisticsArray = new DriverHourStatistics[24];
        for (int i = 0; i < hourStatisticsArray.length; i++) {
            hourStatisticsArray[i] = new DriverHourStatistics(this);
        }
    }

    public DriverDayStatistics(LocalDate date) {
        this();
        this.date = date;
    }

    public DriverDayStatistics(DriverDayStatistics dayStatistics) {
        this();
        this.date = dayStatistics.date;
        for (int i = 0; i < this.hourStatisticsArray.length; i++){
            this.hourStatisticsArray[i].setSelected(dayStatistics.hourStatisticsArray[i].isSelected());
        }
    }

    public LocalDate getDate() {
        return date;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public DriverHourStatistics[] getHourStatisticsArray() {
        return hourStatisticsArray;
    }

    public void setHourStatisticsArray(DriverHourStatistics[] hourStatisticsArray) {
        this.hourStatisticsArray = hourStatisticsArray;
    }

    public boolean isActive() {
        boolean result = LocalDate.now().isBefore(date.minusDays(changeLimit));
        return result;
    }

    @JsonIgnore
    public int getDoneHours() {
        int result = 0;
        for (DriverHourStatistics hourStatistics : hourStatisticsArray) {
            if (hourStatistics.isSelected()) {
                result++;
            }
        }
        return result;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void init(DriverCalendarWeek driverCalendarWeek) {
        this.userId = driverCalendarWeek.getUserId();
        for (DriverHourStatistics hourStatistics : this.getHourStatisticsArray()) {
            hourStatistics.init(this);
        }
    }

    public void cancelAll() {
        for (DriverHourStatistics hourStatistics : this.getHourStatisticsArray()) {
            hourStatistics.setSelected(false);
        }
    }
    @JsonIgnore
    public boolean isNotEmpty() {
        for (DriverHourStatistics hourStatistics : hourStatisticsArray) {
            if (hourStatistics.isSelected()) {
                return true;
            }
        }
        return false;
    }

    public DetailedDriverDayStatistics addDriverInfo(User user) {
        return new DetailedDriverDayStatistics(this, user.getName(), user.getId());
    }
}
