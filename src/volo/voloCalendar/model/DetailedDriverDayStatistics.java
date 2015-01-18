package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Emin Guliyev on 08/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailedDriverDayStatistics extends DriverDayStatistics implements Serializable{
    private static final int changeLimit = 1;
    private String driverInfo;
    public DetailedDriverDayStatistics() {
    }

    public DetailedDriverDayStatistics(DriverDayStatistics driverDayStatistics, String driverInfo, String driverId){
        this.date = driverDayStatistics.date;
        this.userId = driverId;
        this.hourStatisticsArray = new DriverHourStatistics[driverDayStatistics.hourStatisticsArray.length];
        for(int i = 0; i < driverDayStatistics.hourStatisticsArray.length; i++){
            DriverHourStatistics hourStatistics = driverDayStatistics.hourStatisticsArray[i].copy();
            hourStatistics.setDayStatistics(this);
            this.hourStatisticsArray[i] = hourStatistics;
        }
        this.driverInfo = driverInfo;
    }

    public String getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(String driverInfo) {
        this.driverInfo = driverInfo;
    }

    public void init(){
        init(userId);
    }

    public boolean isActive() {
        boolean result = LocalDate.now().isBefore(date.minusDays(changeLimit));
        return result;
    }
}
