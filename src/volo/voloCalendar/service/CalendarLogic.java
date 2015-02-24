package volo.voloCalendar.service;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import volo.voloCalendar.dao.DayStatisticsDAO;
import volo.voloCalendar.entity.ContractType;
import volo.voloCalendar.entity.DayStatistics;
import volo.voloCalendar.entity.User;
import volo.voloCalendar.util.CalendarUtilMethods;
import volo.voloCalendar.util.Settings;
import volo.voloCalendar.viewModel.admin.AdminCalendarWeek;
import volo.voloCalendar.viewModel.admin.DetailedAdminDayStatistics;
import volo.voloCalendar.viewModel.admin.DetailedDriverDayStatistics;
import volo.voloCalendar.viewModel.common.MonthStatistics;
import volo.voloCalendar.viewModel.driver.DriverCalendarWeek;
import volo.voloCalendar.viewModel.driver.DriverDayStatistics;
import volo.voloCalendar.viewModel.user.UserTable;
import volo.voloCalendar.viewModel.user.UserTableItems;

import java.sql.Date;
import java.util.*;

/**
 * Created by Emin Guliyev on 28/11/2014.
 */

public class CalendarLogic {
    @Autowired
    public DayStatisticsDAO dayStatisticsDAO;
    @Autowired
    public UserManagement userManagementLogic;
    @Autowired
    public ForecastingLogic forecastingLogic;

    protected void setDoneHours(User user, LocalDate monthBeginDate) {
        MonthStatistics monthStatistics = getMonthStatisticsForDriverUser(user, monthBeginDate);
        if (monthStatistics != null) {
            user.setDoneHours(monthStatistics.getDoneHours());
        } else {
            user.setDoneHours(0);
        }
    }

    public DriverCalendarWeek getDriverCalendarWeek(String userId, LocalDate beginDate) {
        User user = userManagementLogic.getUserById(userId);
        return getDriverCalendarWeek(user, beginDate);
    }

    private DriverCalendarWeek getDriverCalendarWeek(User user, LocalDate beginDate) {
        if (user == null || user.isAdmin()) {
            return null;
        }
        DriverCalendarWeek driverCalendarWeekInDB = getDriverCalendarWeekFromDB(user.getId(), beginDate);
        if (driverCalendarWeekInDB == null) {
            driverCalendarWeekInDB = new DriverCalendarWeek(user.getId(), beginDate);
            driverCalendarWeekInDB.fixPlannedHours(forecastingLogic.getManualForecasting());
        } else {
            driverCalendarWeekInDB = getStatisticsForDriverWeek(driverCalendarWeekInDB);
        }
        return driverCalendarWeekInDB;
    }

    private DriverCalendarWeek getStatisticsForDriverWeek(DriverCalendarWeek driverCalendarWeekInDB) {
        driverCalendarWeekInDB.fixPlannedHours(forecastingLogic.getManualForecasting());

        LocalDate localDate = driverCalendarWeekInDB.getBeginDate();
        Date sqlDate = new Date(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth());
        List<DayStatistics> dayStatisticsList = dayStatisticsDAO.getWeekStatisticsByWeekBeginDate(sqlDate);
        driverCalendarWeekInDB.subtractStatistics(dayStatisticsList);

        return driverCalendarWeekInDB;
    }

    public DriverCalendarWeek insertOrUpdateDriverCalendarWeek(DriverCalendarWeek driverCalendarWeek) {
        User user = userManagementLogic.getUserById(driverCalendarWeek.getUserId());
        if (user == null || user.isAdmin()) {
            return null;
        }

        for (DriverDayStatistics driverDayStatistics : driverCalendarWeek.getDayStatisticsArray()) {
            LocalDate localDate = driverDayStatistics.getDate();
            DayStatistics dayStatistics = dayStatisticsDAO.getDayByUserIdAndDate(driverCalendarWeek.getUserId(), new Date(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth()));
            DayStatistics newDayStatistics = new DayStatistics(driverCalendarWeek.getUserId(), driverCalendarWeek.getBeginDate(), driverDayStatistics);
            if (dayStatistics != null) {
                newDayStatistics.setId(dayStatistics.getId());
            } else {
                newDayStatistics.setId(UUID.randomUUID().toString());
            }
            dayStatisticsDAO.save(newDayStatistics);
        }
        return driverCalendarWeek;
    }

    public MonthStatistics getMonthStatisticsForDriverUser(User user, LocalDate monthBeginDate) {
        if (user.isAdmin()) {
            return null;
        }
        int plannedHours = (user.getContractType() == ContractType.minijob) ? Settings.minijobMonthlyPlan : 0;

        int doneHours = 0;
        Collection<Date> weekBeginDates = CalendarUtilMethods.getWeekBeginDatesForMonth(monthBeginDate);
        Long doneHoursInMonth = dayStatisticsDAO.getWeekDoneHoursByUserIdAndWeekBeginDate(user.getId(), weekBeginDates);
        doneHours += doneHoursInMonth == null ? 0 : doneHoursInMonth.intValue();

        return new MonthStatistics(plannedHours, doneHours, monthBeginDate);
    }

    protected DriverCalendarWeek getDriverCalendarWeekFromDB(String userId, LocalDate beginDateOfWeek) {
        User user = userManagementLogic.getUserById(userId);
        if (user == null || user.isAdmin()) {
            return null;
        }
        LocalDate localDate = beginDateOfWeek;
        List<DayStatistics> dayStatisticsList = dayStatisticsDAO.getWeekByUserIdAndWeekBeginDate(userId, new Date(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth()));
        if (dayStatisticsList == null || dayStatisticsList.size() == 0) {
            return null;
        }

        DriverCalendarWeek driverCalendarWeek = new DriverCalendarWeek(userId, beginDateOfWeek);
        driverCalendarWeek.fixDoneHours(dayStatisticsList);

        driverCalendarWeek.setExistsInDb(true);
        return driverCalendarWeek;
    }

}
