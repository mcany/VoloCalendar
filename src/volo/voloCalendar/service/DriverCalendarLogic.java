package volo.voloCalendar.service;

import org.springframework.stereotype.Service;
import volo.voloCalendar.entity.User;
import volo.voloCalendar.viewModel.common.MonthStatistics;
import volo.voloCalendar.viewModel.driver.DriverCalendarWeek;

import java.time.LocalDate;
import java.time.Month;

/**
 * Created by Emin Guliyev on 10/02/2015.
 */
@Service
public class DriverCalendarLogic extends CalendarLogic{

    public DriverCalendarWeek setAnnualCalendarForDriver(String userId, LocalDate weekBeginDate) {
        return setPeriodicalCalendarForDriver(userId, weekBeginDate, false);
    }

    public DriverCalendarWeek setMonthlyCalendarForDriver(String userId, LocalDate weekBeginDate) {
        return setPeriodicalCalendarForDriver(userId, weekBeginDate, true);
    }

    private DriverCalendarWeek setPeriodicalCalendarForDriver(String userId, LocalDate weekBeginDate, boolean isForMonthlyOperation) {
        User user = userManagementLogic.getUserById(userId);
        if (user == null || user.isAdmin()) {
            return null;
        }
        DriverCalendarWeek driverCalendarWeek = getDriverCalendarWeekFromDB(user.getId(), weekBeginDate);
        if (driverCalendarWeek == null) {
            return null;
        }
        LocalDate date = driverCalendarWeek.getBeginDate();
        Month month = date.getMonth();
        int year = date.getYear();
        LocalDate nextWeekBeginDate = date.plusDays(8 - date.getDayOfWeek().getValue());
        while (isForMonthlyOperation ? (month == nextWeekBeginDate.getMonth()) : (year == nextWeekBeginDate.getYear())) {
            if (month != nextWeekBeginDate.getMonth()) {
                nextWeekBeginDate = LocalDate.of(nextWeekBeginDate.getYear(), nextWeekBeginDate.getMonthValue(), 1);
                month = nextWeekBeginDate.getMonth();
            }
            generateNewDriverCalendarWeek(user, driverCalendarWeek, nextWeekBeginDate);
            nextWeekBeginDate = nextWeekBeginDate.plusDays(8 - nextWeekBeginDate.getDayOfWeek().getValue());
        }
        return driverCalendarWeek;
    }

    public DriverCalendarWeek setNextWeekCalendarForDriver(String userId, LocalDate weekBeginDate) {
        User user = userManagementLogic.getUserById(userId);
        if (user == null || user.isAdmin()) {
            return null;
        }
        DriverCalendarWeek driverCalendarWeek = getDriverCalendarWeekFromDB(user.getId(), weekBeginDate);
        if (driverCalendarWeek == null) {
            return null;
        }
        LocalDate date = driverCalendarWeek.getBeginDate();
        Month month = date.getMonth();
        LocalDate nextWeekBeginDate = date.plusDays(8 - date.getDayOfWeek().getValue());
        if (month == nextWeekBeginDate.getMonth()) {
            generateNewDriverCalendarWeek(user, driverCalendarWeek, nextWeekBeginDate);
        }
        return driverCalendarWeek;
    }

    private void generateNewDriverCalendarWeek(User user, DriverCalendarWeek driverCalendarWeekTemplate, LocalDate newWeekBeginDate) {
        DriverCalendarWeek nextDriverCalendarWeek = getDriverCalendarWeekFromDB(user.getId(), newWeekBeginDate);
        if (nextDriverCalendarWeek == null) {
            nextDriverCalendarWeek = new DriverCalendarWeek(driverCalendarWeekTemplate, newWeekBeginDate);
        } else {
            nextDriverCalendarWeek.fillDayStatisticsArray(driverCalendarWeekTemplate);
        }
        nextDriverCalendarWeek.setUserId(user.getId());
        insertOrUpdateDriverCalendarWeek(nextDriverCalendarWeek);
    }

    public MonthStatistics getMonthStatisticsForDriverUser(String userId, LocalDate monthBeginDate) {
        User user = userManagementLogic.getUserById(userId);
        if (user == null) {
            return null;
        }
        return getMonthStatisticsForDriverUser(user, monthBeginDate);
    }
}
