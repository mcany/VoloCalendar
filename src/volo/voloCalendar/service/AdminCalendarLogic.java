package volo.voloCalendar.service;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import volo.voloCalendar.entity.DayStatistics;
import volo.voloCalendar.entity.User;
import volo.voloCalendar.util.CalendarUtilMethods;
import volo.voloCalendar.viewModel.admin.AdminCalendarWeek;
import volo.voloCalendar.viewModel.admin.DetailedAdminDayStatistics;
import volo.voloCalendar.viewModel.admin.DetailedDriverDayStatistics;
import volo.voloCalendar.viewModel.common.MonthStatistics;
import volo.voloCalendar.viewModel.driver.DriverCalendarWeek;
import volo.voloCalendar.viewModel.driver.DriverDayStatistics;
import volo.voloCalendar.viewModel.user.UserTable;
import volo.voloCalendar.viewModel.user.UserTableItems;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Emin Guliyev on 10/02/2015.
 */
@Service
public class AdminCalendarLogic extends CalendarLogic {

    private void setStatistics(User user, LocalDate monthBeginDate) {
        setDoneHours(user, monthBeginDate);
        MonthStatistics monthStatistics;
        monthStatistics = getMonthStatisticsForDriverUser(user, monthBeginDate.minusMonths(1));
        if (monthStatistics != null) {
            user.setDiffPrevHours(monthStatistics.getDiffHours());
        } else {
            user.setDiffPrevHours(0);
        }
    }

    public UserTableItems getUsers(UserTable userTable) {
        UserTableItems userTableItems = userManagementLogic.getSortedFilteredPagedUsersWithoutStatistics(userTable, true);

        LocalDate beginDateOfCurrentMonth = CalendarUtilMethods.getBeginLocalDateOfCurrentMonth();
        for (User user : userTableItems.getItems()) {
            setStatistics(user, beginDateOfCurrentMonth);
        }

        return userTableItems;
    }


    public MonthStatistics getMonthStatisticsForAdminUser(LocalDate monthBeginDate) {
        MonthStatistics monthStatistics = new MonthStatistics(monthBeginDate);

        monthStatistics.fixPlannedHours(forecastingLogic.getManualForecasting());

        int doneHours = 0;
        LocalDate[] weekBeginDates = CalendarUtilMethods.getWeekBeginLocalDatesForMonth(monthBeginDate);
        for (LocalDate localDate : weekBeginDates) {
            Long doneHoursInWeek = dayStatisticsDAO.getWeekDoneHoursByWeekBeginDate(new Date(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth()));
            doneHours += doneHoursInWeek == null ? 0 : doneHoursInWeek.intValue();
        }
        monthStatistics.setDoneHours(doneHours);

        return monthStatistics;
    }

    public AdminCalendarWeek getAdminCalendarWeek(LocalDate beginDate) {
        AdminCalendarWeek adminCalendarWeek = new AdminCalendarWeek(beginDate);

        adminCalendarWeek.fixPlannedHours(forecastingLogic.getManualForecasting());
        LocalDate localDate = beginDate;
        List<DayStatistics> dayStatisticsList = dayStatisticsDAO.getWeekStatisticsByWeekBeginDate(new Date(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth()));
        adminCalendarWeek.raiseStatistics(dayStatisticsList);

        return adminCalendarWeek;
    }

    public DetailedDriverDayStatistics insertDriverDayStatistics(LocalDate date, String userId) {
        User user = userManagementLogic.getUserById(userId);
        if (user == null || user.isDeleted() || user.isAdmin()) {
            return null;
        }

        LocalDate beginDateOfProperWeek = date.minusDays(date.getDayOfWeek() - 1);
        if (beginDateOfProperWeek.getMonthOfYear() != date.getMonthOfYear()) {
            beginDateOfProperWeek = new LocalDate(date.getYear(), date.getMonthOfYear(), 1);
        }

        DriverCalendarWeek driverCalendarWeek = getDriverCalendarWeek(userId, beginDateOfProperWeek);
        DriverDayStatistics driverDayStatistics = driverCalendarWeek.getDayStatisticsArray()[date.getDayOfWeek() - beginDateOfProperWeek.getDayOfWeek()];

        if (!driverCalendarWeek.getExistsInDb()) {
            insertOrUpdateDriverCalendarWeek(driverCalendarWeek);
        } else {
            if (driverDayStatistics.isNotEmpty()) {
                return null;
            }
        }
        return driverDayStatistics.addDriverInfo(user);
    }

    public DriverDayStatistics deleteDriverDayStatistics(LocalDate date, String userId) {
        User user = userManagementLogic.getUserById(userId);
        if (user == null) {
            return null;
        }

        LocalDate beginDateOfProperWeek = date.minusDays(date.getDayOfWeek() - 1);
        if (beginDateOfProperWeek.getMonthOfYear() != date.getMonthOfYear()) {
            beginDateOfProperWeek = new LocalDate(date.getYear(), date.getMonthOfYear(), 1);
        }

        DriverCalendarWeek driverCalendarWeekInDB = getDriverCalendarWeekFromDB(user.getId(), beginDateOfProperWeek);
        if (driverCalendarWeekInDB == null) {
            return null;
        }

        DriverDayStatistics driverDayStatistics = driverCalendarWeekInDB.getDayStatisticsArray()[date.getDayOfWeek() - beginDateOfProperWeek.getDayOfWeek()];
        driverDayStatistics.cancelAll();
        insertOrUpdateDriverCalendarWeek(driverCalendarWeekInDB);
        return driverDayStatistics;
    }

    public DetailedAdminDayStatistics getDetailedAdminDayStatistics(LocalDate localDate) {
        DetailedAdminDayStatistics detailedAdminDayStatistics = new DetailedAdminDayStatistics(localDate);

        ArrayList<DetailedDriverDayStatistics> detailedDriverDayStatisticsArrayList = new ArrayList<DetailedDriverDayStatistics>();
        List<DayStatistics> dayStatisticsList = dayStatisticsDAO.getNotEmptyDayByDate(new Date(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth()));
        for (DayStatistics dayStatistics : dayStatisticsList) {
            User user = userManagementLogic.getUserById(dayStatistics.getUserId());
            detailedDriverDayStatisticsArrayList.add(new DetailedDriverDayStatistics(dayStatistics, user));
        }
        detailedAdminDayStatistics.setDetailedDriverDayStatisticsArray(detailedDriverDayStatisticsArrayList.toArray(new DetailedDriverDayStatistics[detailedDriverDayStatisticsArrayList.size()]));
        return detailedAdminDayStatistics;
    }

    public DetailedAdminDayStatistics insertOrUpdateDetailedAdminDayStatistics(DetailedAdminDayStatistics detailedAdminDayStatistics) {
        LocalDate beginDateOfProperWeek = detailedAdminDayStatistics.getDate().minusDays(detailedAdminDayStatistics.getDate().getDayOfWeek() - 1);
        if (beginDateOfProperWeek.getMonthOfYear() != detailedAdminDayStatistics.getDate().getMonthOfYear()) {
            beginDateOfProperWeek = new LocalDate(detailedAdminDayStatistics.getDate().getYear(), detailedAdminDayStatistics.getDate().getMonthOfYear(), 1);
        }

        for (DetailedDriverDayStatistics detailedDriverDayStatistics : detailedAdminDayStatistics.getDetailedDriverDayStatisticsArray()) {
            DriverCalendarWeek driverCalendarWeekInDB
                    = getDriverCalendarWeekFromDB(detailedDriverDayStatistics.getUserId(), beginDateOfProperWeek);
            DriverDayStatistics driverDayStatistics
                    = driverCalendarWeekInDB.getDayStatisticsArray()
                    [detailedAdminDayStatistics.getDate().getDayOfWeek() - beginDateOfProperWeek.getDayOfWeek()];
            for (int i = 0; i < driverDayStatistics.getHourStatisticsArray().length; i++) {
                driverDayStatistics.getHourStatisticsArray()[i]
                        .setSelected(detailedDriverDayStatistics.getHourStatisticsArray()[i].isSelected());
            }
            insertOrUpdateDriverCalendarWeek(driverCalendarWeekInDB);
        }
        detailedAdminDayStatistics.init();
        return detailedAdminDayStatistics;
    }

    public ArrayList<User> getActiveDriversForMonth(LocalDate monthBeginDate) {
        ArrayList<User> result = new ArrayList<User>();
        Set<String> activeUserIds = new HashSet<String>();

        LocalDate[] weekBeginDates = CalendarUtilMethods.getWeekBeginLocalDatesForMonth(monthBeginDate);
        for (LocalDate localDate : weekBeginDates) {
            List<String> activeUserIdsInWeek = dayStatisticsDAO.getActiveDriverIdsByWeekBeginDate(new Date(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth()));
            activeUserIds.addAll(activeUserIdsInWeek);
        }

        for (String userId : activeUserIds) {
            User user = userManagementLogic.getUserById(userId);
            if (!user.isAdmin()) {
                setDoneHours(user, monthBeginDate);
                result.add(user);
            }
        }
        return result;
    }
}
