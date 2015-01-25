package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import volo.voloCalendar.entity.DayStatistics;
import volo.voloCalendar.util.Settings;
import volo.voloCalendar.viewModel.DetailedDriverDayStatistics;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Emin Guliyev on 20/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverDayStatistics implements Serializable { // defines current situation of the day for driver
    protected String userId; // id of driver whom belongs this statistics
    protected LocalDate date; // date of the day
    protected DriverHourStatistics[] hourStatisticsArray; //hourStatistics for the day

    public DriverDayStatistics() {
        hourStatisticsArray = new DriverHourStatistics[24];
        for (int i = 0; i < hourStatisticsArray.length; i++) {
            hourStatisticsArray[i] = new DriverHourStatistics(this, i);
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
        boolean result = LocalDate.now().isBefore(date.minusDays(Settings.driverRestriction));
        return result;
    }

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

    public void init(String userId) {
        this.userId = userId;
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

    public void subtractStatistics(DayStatistics dayStatistics) {
        hourStatisticsArray[0].decreasePlannedHours(dayStatistics.getHour0());
        hourStatisticsArray[1].decreasePlannedHours(dayStatistics.getHour1());
        hourStatisticsArray[2].decreasePlannedHours(dayStatistics.getHour2());
        hourStatisticsArray[3].decreasePlannedHours(dayStatistics.getHour3());
        hourStatisticsArray[4].decreasePlannedHours(dayStatistics.getHour4());
        hourStatisticsArray[5].decreasePlannedHours(dayStatistics.getHour5());
        hourStatisticsArray[6].decreasePlannedHours(dayStatistics.getHour6());
        hourStatisticsArray[7].decreasePlannedHours(dayStatistics.getHour7());
        hourStatisticsArray[8].decreasePlannedHours(dayStatistics.getHour8());
        hourStatisticsArray[9].decreasePlannedHours(dayStatistics.getHour9());
        hourStatisticsArray[10].decreasePlannedHours(dayStatistics.getHour10());
        hourStatisticsArray[11].decreasePlannedHours(dayStatistics.getHour11());
        hourStatisticsArray[12].decreasePlannedHours(dayStatistics.getHour12());
        hourStatisticsArray[13].decreasePlannedHours(dayStatistics.getHour13());
        hourStatisticsArray[14].decreasePlannedHours(dayStatistics.getHour14());
        hourStatisticsArray[15].decreasePlannedHours(dayStatistics.getHour15());
        hourStatisticsArray[16].decreasePlannedHours(dayStatistics.getHour16());
        hourStatisticsArray[17].decreasePlannedHours(dayStatistics.getHour17());
        hourStatisticsArray[18].decreasePlannedHours(dayStatistics.getHour18());
        hourStatisticsArray[19].decreasePlannedHours(dayStatistics.getHour19());
        hourStatisticsArray[20].decreasePlannedHours(dayStatistics.getHour20());
        hourStatisticsArray[21].decreasePlannedHours(dayStatistics.getHour21());
        hourStatisticsArray[22].decreasePlannedHours(dayStatistics.getHour22());
        hourStatisticsArray[23].decreasePlannedHours(dayStatistics.getHour23());
    }

    public void fixHourStatisticsArray(DayStatistics dayStatistics) {
        hourStatisticsArray[0].setSelected(dayStatistics.getHour0() > 0);
        hourStatisticsArray[1].setSelected(dayStatistics.getHour1() > 0);
        hourStatisticsArray[2].setSelected(dayStatistics.getHour2() > 0);
        hourStatisticsArray[3].setSelected(dayStatistics.getHour3() > 0);
        hourStatisticsArray[4].setSelected(dayStatistics.getHour4() > 0);
        hourStatisticsArray[5].setSelected(dayStatistics.getHour5() > 0);
        hourStatisticsArray[6].setSelected(dayStatistics.getHour6() > 0);
        hourStatisticsArray[7].setSelected(dayStatistics.getHour7() > 0);
        hourStatisticsArray[8].setSelected(dayStatistics.getHour8() > 0);
        hourStatisticsArray[9].setSelected(dayStatistics.getHour9() > 0);
        hourStatisticsArray[10].setSelected(dayStatistics.getHour10() > 0);
        hourStatisticsArray[11].setSelected(dayStatistics.getHour11() > 0);
        hourStatisticsArray[12].setSelected(dayStatistics.getHour12() > 0);
        hourStatisticsArray[13].setSelected(dayStatistics.getHour13() > 0);
        hourStatisticsArray[14].setSelected(dayStatistics.getHour14() > 0);
        hourStatisticsArray[15].setSelected(dayStatistics.getHour15() > 0);
        hourStatisticsArray[16].setSelected(dayStatistics.getHour16() > 0);
        hourStatisticsArray[17].setSelected(dayStatistics.getHour17() > 0);
        hourStatisticsArray[18].setSelected(dayStatistics.getHour18() > 0);
        hourStatisticsArray[19].setSelected(dayStatistics.getHour19() > 0);
        hourStatisticsArray[20].setSelected(dayStatistics.getHour20() > 0);
        hourStatisticsArray[21].setSelected(dayStatistics.getHour21() > 0);
        hourStatisticsArray[22].setSelected(dayStatistics.getHour22() > 0);
        hourStatisticsArray[23].setSelected(dayStatistics.getHour23() > 0);
    }
}
