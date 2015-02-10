package volo.voloCalendar.service;

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
import java.time.LocalDate;
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
        for (LocalDate date : weekBeginDates) {
            Long doneHoursInWeek = dayStatisticsDAO.getWeekDoneHoursByWeekBeginDate(Date.valueOf(date));
            doneHours += doneHoursInWeek == null ? 0 : doneHoursInWeek.intValue();
        }
        monthStatistics.setDoneHours(doneHours);

        return monthStatistics;
    }

    public AdminCalendarWeek getAdminCalendarWeek(LocalDate beginDate) {
        AdminCalendarWeek adminCalendarWeek = new AdminCalendarWeek(beginDate);

        adminCalendarWeek.fixPlannedHours(forecastingLogic.getManualForecasting());

        List<DayStatistics> dayStatisticsList = dayStatisticsDAO.getWeekStatisticsByWeekBeginDate(Date.valueOf(beginDate));
        adminCalendarWeek.raiseStatistics(dayStatisticsList);

        return adminCalendarWeek;
    }

    public DetailedDriverDayStatistics insertDriverDayStatistics(LocalDate date, String userId) {
        User user = userManagementLogic.getUserById(userId);
        if (user == null || user.isDeleted() || user.isAdmin()) {
            return null;
        }

        LocalDate beginDateOfProperWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
        if (beginDateOfProperWeek.getMonthValue() != date.getMonthValue()) {
            beginDateOfProperWeek = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
        }

        DriverCalendarWeek driverCalendarWeek = getDriverCalendarWeek(userId, beginDateOfProperWeek);
        DriverDayStatistics driverDayStatistics = driverCalendarWeek.getDayStatisticsArray()[date.getDayOfWeek().getValue() - beginDateOfProperWeek.getDayOfWeek().getValue()];

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

        LocalDate beginDateOfProperWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
        if (beginDateOfProperWeek.getMonthValue() != date.getMonthValue()) {
            beginDateOfProperWeek = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
        }

        DriverCalendarWeek driverCalendarWeekInDB = getDriverCalendarWeekFromDB(user.getId(), beginDateOfProperWeek);
        if (driverCalendarWeekInDB == null) {
            return null;
        }

        DriverDayStatistics driverDayStatistics = driverCalendarWeekInDB.getDayStatisticsArray()[date.getDayOfWeek().getValue() - beginDateOfProperWeek.getDayOfWeek().getValue()];
        driverDayStatistics.cancelAll();
        insertOrUpdateDriverCalendarWeek(driverCalendarWeekInDB);
        return driverDayStatistics;
    }

    public DetailedAdminDayStatistics getDetailedAdminDayStatistics(LocalDate date) {
        DetailedAdminDayStatistics detailedAdminDayStatistics = new DetailedAdminDayStatistics(date);

        ArrayList<DetailedDriverDayStatistics> detailedDriverDayStatisticsArrayList = new ArrayList<DetailedDriverDayStatistics>();
        List<DayStatistics> dayStatisticsList = dayStatisticsDAO.getNotEmptyDayByDate(Date.valueOf(date));
        for (DayStatistics dayStatistics : dayStatisticsList) {
            User user = userManagementLogic.getUserById(dayStatistics.getUserId());
            detailedDriverDayStatisticsArrayList.add(new DetailedDriverDayStatistics(dayStatistics, user));
        }
        detailedAdminDayStatistics.setDetailedDriverDayStatisticsArray(detailedDriverDayStatisticsArrayList.toArray(new DetailedDriverDayStatistics[detailedDriverDayStatisticsArrayList.size()]));
        return detailedAdminDayStatistics;
    }

    public DetailedAdminDayStatistics insertOrUpdateDetailedAdminDayStatistics(DetailedAdminDayStatistics detailedAdminDayStatistics) {
        LocalDate beginDateOfProperWeek = detailedAdminDayStatistics.getDate().minusDays(detailedAdminDayStatistics.getDate().getDayOfWeek().getValue() - 1);
        if (beginDateOfProperWeek.getMonthValue() != detailedAdminDayStatistics.getDate().getMonthValue()) {
            beginDateOfProperWeek = LocalDate.of(detailedAdminDayStatistics.getDate().getYear(), detailedAdminDayStatistics.getDate().getMonthValue(), 1);
        }

        for (DetailedDriverDayStatistics detailedDriverDayStatistics : detailedAdminDayStatistics.getDetailedDriverDayStatisticsArray()) {
            DriverCalendarWeek driverCalendarWeekInDB
                    = getDriverCalendarWeekFromDB(detailedDriverDayStatistics.getUserId(), beginDateOfProperWeek);
            DriverDayStatistics driverDayStatistics
                    = driverCalendarWeekInDB.getDayStatisticsArray()
                    [detailedAdminDayStatistics.getDate().getDayOfWeek().getValue() - beginDateOfProperWeek.getDayOfWeek().getValue()];
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
        for (LocalDate date : weekBeginDates) {
            List<String> activeUserIdsInWeek = dayStatisticsDAO.getActiveDriverIdsByWeekBeginDate(Date.valueOf(date));
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
