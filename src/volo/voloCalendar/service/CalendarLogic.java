package volo.voloCalendar.service;

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
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

/**
 * Created by Emin Guliyev on 28/11/2014.
 */
@Service
public class CalendarLogic {
    @Autowired
    public DayStatisticsDAO dayStatisticsDAO;
    @Autowired
    public UserManagement userManagementLogic;
    @Autowired
    public ForecastingLogic forecastingLogic;

    public static void main(String[] args) {
        final AbstractApplicationContext context = new ClassPathXmlApplicationContext("file:D:/projects/VoloCalendar/web/WEB-INF/config/applicationContext.xml");
        CalendarLogic calendarLogic = context.getBean(CalendarLogic.class);
        /*DayStatistics dayStatistics = new DayStatistics();
        dayStatistics.setDate(new java.sql.Date(2015 - 1900, 1, 22));
        dayStatistics.setWeekBeginDate(new java.sql.Date(2015 - 1900, 1, 19));
        dayStatistics.setUserId("3");
        dayStatistics.setId(UUID.randomUUID().toString());
        dayStatistics.setWeekDayIndex((short) 3);
        logic.dayStatisticsDAO.save(dayStatistics);

        dayStatistics = new DayStatistics();
        dayStatistics.setDate(new java.sql.Date(2015 - 1900, 1, 23));
        dayStatistics.setWeekBeginDate(new java.sql.Date(2015 - 1900, 1, 19));
        dayStatistics.setUserId("1");
        dayStatistics.setId(UUID.randomUUID().toString());
        dayStatistics.setWeekDayIndex((short)5);
        dayStatistics.setHour0((short)2);
        dayStatistics.setHour1((short)2);
        logic.dayStatisticsDAO.save(dayStatistics);

        dayStatistics = new DayStatistics();
        dayStatistics.setDate(new java.sql.Date(2015 - 1900, 1, 22));
        dayStatistics.setWeekBeginDate(new java.sql.Date(2015 - 1900, 1, 19));
        dayStatistics.setUserId("2");
        dayStatistics.setId(UUID.randomUUID().toString());
        dayStatistics.setWeekDayIndex((short)4);
        dayStatistics.setHour0((short)3);
        dayStatistics.setHour1((short)3);
        logic.dayStatisticsDAO.save(dayStatistics);

        dayStatistics = new DayStatistics();
        dayStatistics.setDate(new java.sql.Date(2015 - 1900, 1, 23));
        dayStatistics.setWeekBeginDate(new java.sql.Date(2015 - 1900, 1, 19));
        dayStatistics.setUserId("2");
        dayStatistics.setId(UUID.randomUUID().toString());
        dayStatistics.setWeekDayIndex((short)5);
        dayStatistics.setHour0((short)4);
        dayStatistics.setHour1((short)4);
        logic.dayStatisticsDAO.save(dayStatistics);

        List<DayStatistics> list = logic.dayStatisticsDAO.getWeekStatisticsByWeekBeginDate(new java.sql.Date(2015 - 1900, 1, 19));
        DayStatistics obj1 = list.get(0);
        DayStatistics obj2 = list.get(1);
        System.out.println(list.size());
        long count = logic.dayStatisticsDAO.getWeekDoneHoursByWeekBeginDate(new java.sql.Date(2015 - 1900, 1, 19));
        System.out.println(count);
        List<DayStatistics> list1 = logic.dayStatisticsDAO.getNotEmptyDayByDate(new java.sql.Date(2015 - 1900, 1, 22));
        List<DayStatistics> list2 = logic.dayStatisticsDAO.getNotEmptyDayByDate(new java.sql.Date(2015 - 1900, 1, 21));
        System.out.println(list1.size() + " - " + list2.size());
        List<String> ids = logic.dayStatisticsDAO.getActiveDriverIdsByWeekBeginDate(new java.sql.Date(2015 - 1900, 1, 19));
        System.out.println(ids.size());*/
    }



    private  void setStatistics(User user, LocalDate monthBeginDate) {
        setDoneHours(user, monthBeginDate);
        MonthStatistics monthStatistics;
        monthStatistics = getMonthStatisticsForDriverUser(user, monthBeginDate.minusMonths(1));
        if (monthStatistics != null) {
            user.setDiffPrevHours(monthStatistics.getDiffHours());
        } else {
            user.setDiffPrevHours(0);
        }
    }

    private  void setDoneHours(User user, LocalDate monthBeginDate) {
        MonthStatistics monthStatistics = getMonthStatisticsForDriverUser(user, monthBeginDate);
        if (monthStatistics != null) {
            user.setDoneHours(monthStatistics.getDoneHours());
        } else {
            user.setDoneHours(0);
        }
    }

    public UserTableItems getUsers(UserTable userTable) {
        UserTableItems userTableItems = userManagementLogic.getSortedFilteredPagedUsersWithoutStatistics(userTable);

        LocalDate beginDateOfCurrentMonth = CalendarUtilMethods.getBeginLocalDateOfCurrentMonth();
        for (User user : userTableItems.getItems()) {
            setStatistics(user, beginDateOfCurrentMonth);
        }

        return userTableItems;
    }

    public DriverCalendarWeek getDriverCalendarWeek(String userId, LocalDate beginDate) {
        User user = userManagementLogic.getUserById(userId);
        return getDriverCalendarWeek(user, beginDate);
    }

    private  DriverCalendarWeek getDriverCalendarWeek(User user, LocalDate beginDate) {
        if (user == null || user.isAdmin()) {
            return null;
        }
        DriverCalendarWeek driverCalendarWeekInDB = getDriverCalendarWeekFromDB(user.getId(), beginDate);
        if (driverCalendarWeekInDB == null) {
            driverCalendarWeekInDB = new DriverCalendarWeek(user.getId(), beginDate);
            driverCalendarWeekInDB.fixPlannedHours(forecastingLogic.getManualForecasting());
        }else {
            driverCalendarWeekInDB = getStatisticsForDriverWeek(driverCalendarWeekInDB);
        }
        return driverCalendarWeekInDB;
    }

    private  DriverCalendarWeek getStatisticsForDriverWeek(DriverCalendarWeek driverCalendarWeekInDB) {
        driverCalendarWeekInDB.fixPlannedHours(forecastingLogic.getManualForecasting());

        Date sqlDate = Date.valueOf(driverCalendarWeekInDB.getBeginDate());
        List<DayStatistics> dayStatisticsList = dayStatisticsDAO.getWeekStatisticsByWeekBeginDate(sqlDate);
        driverCalendarWeekInDB.subtractStatistics(dayStatisticsList);

        return driverCalendarWeekInDB;
    }

    public  DriverCalendarWeek insertOrUpdateDriverCalendarWeek(DriverCalendarWeek driverCalendarWeek) {
        User user = userManagementLogic.getUserById(driverCalendarWeek.getUserId());
        if (user == null || user.isAdmin()) {
            return null;
        }

        for (DriverDayStatistics driverDayStatistics: driverCalendarWeek.getDayStatisticsArray()){
            DayStatistics dayStatistics = dayStatisticsDAO.getDayByUserIdAndDate(driverCalendarWeek.getUserId(), Date.valueOf(driverDayStatistics.getDate()));
            DayStatistics newDayStatistics = new DayStatistics(driverCalendarWeek.getUserId(), driverCalendarWeek.getBeginDate(), driverDayStatistics);
            if (dayStatistics != null){
                newDayStatistics.setId(dayStatistics.getId());
            }else{
                newDayStatistics.setId(UUID.randomUUID().toString());
            }
            dayStatisticsDAO.save(newDayStatistics);
        }
        return driverCalendarWeek;
    }

    public  DriverCalendarWeek setAnnualCalendarForDriver(String userId, LocalDate weekBeginDate) {
        return setPeriodicalCalendarForDriver(userId, weekBeginDate, false);
    }

    public  DriverCalendarWeek setMonthlyCalendarForDriver(String userId, LocalDate weekBeginDate) {
        return setPeriodicalCalendarForDriver(userId, weekBeginDate, true);
    }

    private  DriverCalendarWeek setPeriodicalCalendarForDriver(String userId, LocalDate weekBeginDate, boolean isForMonthlyOperation) {
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
            if (month != nextWeekBeginDate.getMonth()){
                nextWeekBeginDate = LocalDate.of(nextWeekBeginDate.getYear(), nextWeekBeginDate.getMonthValue(), 1);
                month = nextWeekBeginDate.getMonth();
            }
            generateNewDriverCalendarWeek(user, driverCalendarWeek, nextWeekBeginDate);
            nextWeekBeginDate = nextWeekBeginDate.plusDays(8 - nextWeekBeginDate.getDayOfWeek().getValue());
        }
        return driverCalendarWeek;
    }

    public  DriverCalendarWeek setNextWeekCalendarForDriver(String userId, LocalDate weekBeginDate) {
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

    private  void generateNewDriverCalendarWeek(User user, DriverCalendarWeek driverCalendarWeekTemplate, LocalDate newWeekBeginDate) {
        DriverCalendarWeek nextDriverCalendarWeek = getDriverCalendarWeekFromDB(user.getId(), newWeekBeginDate);
        if (nextDriverCalendarWeek == null) {
            nextDriverCalendarWeek = new DriverCalendarWeek(driverCalendarWeekTemplate, newWeekBeginDate);
        } else {
            nextDriverCalendarWeek.fillDayStatisticsArray(driverCalendarWeekTemplate);
        }
        nextDriverCalendarWeek.setUserId(user.getId());
        insertOrUpdateDriverCalendarWeek(nextDriverCalendarWeek);
    }

    public  MonthStatistics getMonthStatisticsForDriverUser(String userId, LocalDate monthBeginDate) {
        User user = userManagementLogic.getUserById(userId);
        if (user == null) {
            return null;
        }
        return getMonthStatisticsForDriverUser(user, monthBeginDate);
    }

    public  MonthStatistics getMonthStatisticsForDriverUser(User user, LocalDate monthBeginDate) {
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

    public  MonthStatistics getMonthStatisticsForAdminUser(LocalDate monthBeginDate) {
        MonthStatistics monthStatistics = new MonthStatistics(monthBeginDate);

        monthStatistics.fixPlannedHours(forecastingLogic.getManualForecasting());

        int doneHours = 0;
        LocalDate[] weekBeginDates = CalendarUtilMethods.getWeekBeginLocalDatesForMonth(monthBeginDate);
        for (LocalDate date : weekBeginDates) {
            Long doneHoursInWeek = dayStatisticsDAO.getWeekDoneHoursByWeekBeginDate(Date.valueOf(date));
            doneHours += doneHoursInWeek == null?0:doneHoursInWeek.intValue();
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
        if (user == null || user.isDeleted() || user.isAdmin()){
            return null;
        }

        LocalDate beginDateOfProperWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
        if (beginDateOfProperWeek.getMonthValue() != date.getMonthValue()){
            beginDateOfProperWeek = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
        }

        DriverCalendarWeek driverCalendarWeek = getDriverCalendarWeek(userId, beginDateOfProperWeek);
        DriverDayStatistics driverDayStatistics = driverCalendarWeek.getDayStatisticsArray()[date.getDayOfWeek().getValue() - beginDateOfProperWeek.getDayOfWeek().getValue()];

        if (!driverCalendarWeek.getExistsInDb()){
            insertOrUpdateDriverCalendarWeek(driverCalendarWeek);
        }else{
            if (driverDayStatistics.isNotEmpty()){
                return null;
            }
        }
        return driverDayStatistics.addDriverInfo(user);
    }

    public  DriverDayStatistics deleteDriverDayStatistics(LocalDate date, String userId) {
        User user = userManagementLogic.getUserById(userId);
        if (user == null){
            return null;
        }

        LocalDate beginDateOfProperWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
        if (beginDateOfProperWeek.getMonthValue() != date.getMonthValue()){
            beginDateOfProperWeek = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
        }

        DriverCalendarWeek driverCalendarWeekInDB = getDriverCalendarWeekFromDB(user.getId(), beginDateOfProperWeek);
        if (driverCalendarWeekInDB == null){
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
        for(DayStatistics dayStatistics: dayStatisticsList){
            User user = userManagementLogic.getUserById(dayStatistics.getUserId());
            detailedDriverDayStatisticsArrayList.add(new DetailedDriverDayStatistics(dayStatistics, user));
        }
        detailedAdminDayStatistics.setDetailedDriverDayStatisticsArray(detailedDriverDayStatisticsArrayList.toArray(new DetailedDriverDayStatistics[detailedDriverDayStatisticsArrayList.size()]));
        return detailedAdminDayStatistics;
    }

    public  DetailedAdminDayStatistics insertOrUpdateDetailedAdminDayStatistics(DetailedAdminDayStatistics detailedAdminDayStatistics) {
        LocalDate beginDateOfProperWeek = detailedAdminDayStatistics.getDate().minusDays(detailedAdminDayStatistics.getDate().getDayOfWeek().getValue() - 1);
        if (beginDateOfProperWeek.getMonthValue() != detailedAdminDayStatistics.getDate().getMonthValue()){
            beginDateOfProperWeek = LocalDate.of(detailedAdminDayStatistics.getDate().getYear(), detailedAdminDayStatistics.getDate().getMonthValue(), 1);
        }

        for (DetailedDriverDayStatistics detailedDriverDayStatistics : detailedAdminDayStatistics.getDetailedDriverDayStatisticsArray()){
            DriverCalendarWeek driverCalendarWeekInDB
                    = getDriverCalendarWeekFromDB(detailedDriverDayStatistics.getUserId(), beginDateOfProperWeek);
            DriverDayStatistics driverDayStatistics
                    = driverCalendarWeekInDB.getDayStatisticsArray()
                    [detailedAdminDayStatistics.getDate().getDayOfWeek().getValue() - beginDateOfProperWeek.getDayOfWeek().getValue()];
            for (int i = 0; i < driverDayStatistics.getHourStatisticsArray().length; i++){
                driverDayStatistics.getHourStatisticsArray()[i]
                        .setSelected(detailedDriverDayStatistics.getHourStatisticsArray()[i].isSelected());
            }
            insertOrUpdateDriverCalendarWeek(driverCalendarWeekInDB);
        }
        detailedAdminDayStatistics.init();
        return detailedAdminDayStatistics;
    }

    private  DriverCalendarWeek getDriverCalendarWeekFromDB(String userId, LocalDate beginDateOfWeek) {
        User user = userManagementLogic.getUserById(userId);
        if (user == null || user.isAdmin()){
            return null;
        }

        List<DayStatistics> dayStatisticsList = dayStatisticsDAO.getWeekByUserIdAndWeekBeginDate(userId, Date.valueOf(beginDateOfWeek));
        if (dayStatisticsList == null || dayStatisticsList.size() == 0){
            return null;
        }

        DriverCalendarWeek driverCalendarWeek = new DriverCalendarWeek(userId, beginDateOfWeek);
        driverCalendarWeek.fixDoneHours(dayStatisticsList);

        driverCalendarWeek.setExistsInDb(true);
        return driverCalendarWeek;
    }

    public  ArrayList<User> getActiveDriversForMonth(LocalDate monthBeginDate) {
        ArrayList<User> result = new ArrayList<User>();
        Set<String> activeUserIds = new HashSet<String>();

        LocalDate[] weekBeginDates = CalendarUtilMethods.getWeekBeginLocalDatesForMonth(monthBeginDate);
        for (LocalDate date : weekBeginDates) {
            List<String> activeUserIdsInWeek = dayStatisticsDAO.getActiveDriverIdsByWeekBeginDate(Date.valueOf(date));
            activeUserIds.addAll(activeUserIdsInWeek);
        }

        for (String userId: activeUserIds){
            User user = userManagementLogic.getUserById(userId);
            if (!user.isAdmin()){
                setDoneHours(user, monthBeginDate);
                result.add(user);
            }
        }
        return result;
    }

}
