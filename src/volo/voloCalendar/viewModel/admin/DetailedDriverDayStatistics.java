package volo.voloCalendar.viewModel.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import volo.voloCalendar.entity.DayStatistics;
import volo.voloCalendar.entity.User;
import volo.voloCalendar.util.Settings;
import volo.voloCalendar.viewModel.driver.DriverDayStatistics;
import volo.voloCalendar.viewModel.driver.DriverHourStatistics;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Emin Guliyev on 08/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailedDriverDayStatistics extends DriverDayStatistics implements Serializable { // defines driverDayStatistics + driverInfo string for driver whom the object belongs, used in admin's main page(calendar view)
    private String driverInfo; //driver info(name of driver)

    public DetailedDriverDayStatistics() {
    }

    public DetailedDriverDayStatistics(DriverDayStatistics driverDayStatistics, String driverInfo, String driverId) {
        this.date = driverDayStatistics.getDate();
        this.driverInfo = driverInfo;
        this.userId = driverId;
        this.hourStatisticsArray = new DriverHourStatistics[driverDayStatistics.getHourStatisticsArray().length];
        for (int i = 0; i < driverDayStatistics.getHourStatisticsArray().length; i++) {
            DriverHourStatistics hourStatistics = driverDayStatistics.getHourStatisticsArray()[i].copy();
            hourStatistics.setDayStatistics(this);
            this.hourStatisticsArray[i] = hourStatistics;
        }
    }

    public DetailedDriverDayStatistics(DayStatistics dayStatistics, User user) {
        this.date = dayStatistics.getDate().toLocalDate();
        this.driverInfo = user.getName();
        this.userId = user.getId();
        this.hourStatisticsArray = new DriverHourStatistics[24];
        DriverHourStatistics driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour0() > 0, 0);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[0] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour1() > 0, 1);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[1] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour2() > 0, 2);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[2] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour3() > 0, 3);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[3] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour4() > 0, 4);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[4] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour5() > 0, 5);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[5] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour6() > 0, 6);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[6] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour7() > 0, 7);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[7] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour8() > 0, 8);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[8] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour9() > 0, 9);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[9] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour10() > 0, 10);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[10] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour11() > 0, 11);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[11] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour12() > 0, 12);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[12] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour13() > 0, 13);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[13] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour14() > 0, 14);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[14] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour15() > 0, 15);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[15] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour16() > 0, 16);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[16] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour17() > 0, 17);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[17] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour18() > 0, 18);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[18] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour19() > 0, 19);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[19] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour20() > 0, 20);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[20] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour21() > 0, 21);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[21] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour22() > 0, 22);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[22] = driverHourStatistics;
        driverHourStatistics = new DriverHourStatistics(dayStatistics.getHour23() > 0, 23);
        driverHourStatistics.setDayStatistics(this);
        hourStatisticsArray[23] = driverHourStatistics;
    }

    public String getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(String driverInfo) {
        this.driverInfo = driverInfo;
    }

    public void init() {
        init(userId);
    }

    public boolean isActive() {
        boolean result = LocalDate.now().isBefore(date.minusDays(Settings.adminRestriction));
        return result;
    }
}
