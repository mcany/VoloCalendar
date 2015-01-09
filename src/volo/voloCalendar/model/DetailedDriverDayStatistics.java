package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 08/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailedDriverDayStatistics extends DriverDayStatistics implements Serializable{
    private String driverInfo;
    public DetailedDriverDayStatistics() {
    }

    public DetailedDriverDayStatistics(DriverDayStatistics driverDayStatistics, String driverInfo, String driverId){
        this.date = driverDayStatistics.date;
        this.userId = driverId;
        this.hourStatisticsArray = driverDayStatistics.hourStatisticsArray;
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
}
