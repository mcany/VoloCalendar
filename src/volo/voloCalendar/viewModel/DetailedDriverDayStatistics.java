package volo.voloCalendar.viewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import volo.voloCalendar.model.DriverDayStatistics;
import volo.voloCalendar.model.DriverHourStatistics;
import volo.voloCalendar.util.Settings;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Emin Guliyev on 08/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailedDriverDayStatistics extends DriverDayStatistics implements Serializable{ // defines driverDayStatistics + driverInfo string for driver whom the object belongs, used in admin's main page(calendar view)
    private String driverInfo; //driver info(name of driver)

    public DetailedDriverDayStatistics() {
    }

    public DetailedDriverDayStatistics(DriverDayStatistics driverDayStatistics, String driverInfo, String driverId){
        this.date = driverDayStatistics.getDate();
        this.userId = driverId;
        this.hourStatisticsArray = new DriverHourStatistics[driverDayStatistics.getHourStatisticsArray().length];
        for(int i = 0; i < driverDayStatistics.getHourStatisticsArray().length; i++){
            DriverHourStatistics hourStatistics = driverDayStatistics.getHourStatisticsArray()[i].copy();
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
        boolean result = LocalDate.now().isBefore(date.minusDays(Settings.adminRestriction));
        return result;
    }
}
