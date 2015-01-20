package volo.voloCalendar.service;

import volo.voloCalendar.model.*;
import volo.voloCalendar.util.Settings;
import volo.voloCalendar.util.UtilMethods;
import volo.voloCalendar.viewModel.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

/**
 * Created by Emin Guliyev on 28/11/2014.
 */
public class Backend {
    private static HashMap<String, User> usersByEmail = new HashMap<String, User>();
    private static HashMap<String, User> usersById = new HashMap<String, User>();
    private static ManualForecasting manualForecasting = new ManualForecasting();

    static {
        User emin = new User("123", "eminguliyev1987@gmail.com", "1", "Emin Guliyev");
        User mertcan = new User("456", "dr_ilashka@live.ru", "1", "Mertcan Yigin"
                , "Christ-Probst.", "10/142", "80805", "Munich", "017678947235", TransportType.bicycle, TelephoneType.ios
                , "DE64IRCE92222212345678", "BINFFDDDXXX", ContractType.minijob);
        usersByEmail.put(emin.getEmail(), emin);
        usersById.put(emin.getId(), emin);
        usersByEmail.put(mertcan.getEmail(), mertcan);
        usersById.put(mertcan.getId(), mertcan);
        for (int i = 0; i < 35; i++) {
            User emin1 = new User(emin);
            emin1.setId(UUID.randomUUID().toString());
            emin1.setEmail(i + emin.getEmail());
            usersByEmail.put(emin1.getEmail(), emin1);
            usersById.put(emin1.getId(), emin1);
        }

        for (int i = 0; i < 35; i++) {
            User mertcan1 = new User(mertcan);
            mertcan1.setId(UUID.randomUUID().toString());
            mertcan1.setEmail(i + mertcan.getEmail());
            usersByEmail.put(mertcan1.getEmail(), mertcan1);
            usersById.put(mertcan1.getId(), mertcan1);
        }
    }

    //rest
    public static User getUserByEmail(String email) {
        return usersByEmail.get(email);
    }

    //rest
    public static User getUserById(String id) {
        return usersById.get(id);
    }

    private static Collection<User> getAllUsers() {
        return usersByEmail.values();
    }

    //rest
    public static User insertOrUpdateUser(User user) {
        User userInDb = getUserById(user.getId());
        if (userInDb != null && !userInDb.isAdmin() && user.isAdmin()) {
            userInDb.setDriverCalendarWeekHashMap(new HashMap<LocalDate, DriverCalendarWeek>());
        }
        usersByEmail.put(user.getEmail(), user);
        usersById.put(user.getId(), user);
        return user;
    }

    //rest
    public static User deleteUser(String id) {
        User user = usersById.get(id);
        usersByEmail.remove(user.getEmail());
        usersById.remove(user.getId());
        return user;
    }

    public static User[] getUsers(UserTableViewModel userTableViewModel) {
        User[] userArray = getSortedFilteredPagedUsersWithoutStatistics(userTableViewModel);

        LocalDate beginDateOfCurrentMonth = UtilMethods.getBeginDateOfCurrentMonth();
        for (User user : userArray) {
            setStatistics(user, beginDateOfCurrentMonth);
        }

        return userArray;
    }

    private static void setStatistics(User user, LocalDate beginDateOfMonth) {
        setDoneHours(user, beginDateOfMonth);
        MonthStatistics monthStatistics;
        monthStatistics = getMonthStatisticsForDriverUser(user, beginDateOfMonth.minusMonths(1));
        if (monthStatistics != null) {
            user.setDiffPrevHours(monthStatistics.getDiffHours());
        } else {
            user.setDiffPrevHours(0);
        }
    }

    private static void setDoneHours(User user, LocalDate beginDateOfMonth) {
        MonthStatistics monthStatistics = getMonthStatisticsForDriverUser(user, beginDateOfMonth);
        if (monthStatistics != null) {
            user.setDoneHours(monthStatistics.getDoneHours());
        } else {
            user.setDoneHours(0);
        }
    }

    //rest
    private static User[] getSortedFilteredPagedUsersWithoutStatistics(UserTableViewModel userTableViewModel) {
        Collection<User> users = Backend.getAllUsers();
        User[] userArray = new User[users.size()];
        userArray = users.toArray(userArray);
        int end = userTableViewModel.getBeginIndex() - 1 + userTableViewModel.getMaxNumber();
        if (end >= userArray.length) {
            end = userArray.length;
        }
        userArray = Arrays.copyOfRange(userArray, userTableViewModel.getBeginIndex() - 1, end);
        return userArray;
    }

    //rest
    public static int getAllUsersCount() {
        return getAllUsers().size();
    }

    //rest
    public static ManualForecasting getManualForecasting() {
        return manualForecasting;
    }

    //rest
    public static void setManualForecasting(ManualForecasting manualForecasting) {
        Backend.manualForecasting = manualForecasting;
    }

    public static DriverCalendarWeek getDriverCalendarWeek(String userId, LocalDate beginDate) {
        User user = getUserById(userId);
        return getDriverCalendarWeek(user, beginDate);
    }

    private static DriverCalendarWeek getDriverCalendarWeek(User user, LocalDate beginDate) {
        if (user == null || user.isAdmin()) {
            return null;
        }
        DriverCalendarWeek driverCalendarWeekInDB = getDriverCalendarWeekFromDB(user, beginDate);
        if (driverCalendarWeekInDB == null) {
            driverCalendarWeekInDB = new DriverCalendarWeek(user.getId(), beginDate);
            driverCalendarWeekInDB.fixPlannedHours(getManualForecasting());
        }else {
            driverCalendarWeekInDB = getStatisticsForDriverWeek(driverCalendarWeekInDB);
        }
        return driverCalendarWeekInDB;
    }

    //rest
    private static DriverCalendarWeek getStatisticsForDriverWeek(DriverCalendarWeek driverCalendarWeekInDB) {
        driverCalendarWeekInDB.fixPlannedHours(getManualForecasting());
        for (User userInDb : getAllUsers()) {
            DriverCalendarWeek driverCalendarWeekForDbUser = getDriverCalendarWeekFromDB(userInDb, driverCalendarWeekInDB.getBeginDate());
            if (driverCalendarWeekForDbUser != null) {
                driverCalendarWeekInDB.subtractStatistics(driverCalendarWeekForDbUser);
            }
        }
        return driverCalendarWeekInDB;
    }

    //rest
    private static DriverCalendarWeek getDriverCalendarWeekFromDB(User user, LocalDate beginDate) {
        return user.getDriverCalendarWeekHashMap().get(beginDate);
    }

    //rest
    public static DriverCalendarWeek insertOrUpdateDriverCalendarWeek(DriverCalendarWeek driverCalendarWeek) {
        User user = getUserById(driverCalendarWeek.getUserId());
        if (user == null || user.isAdmin()) {
            return null;
        }
        user.getDriverCalendarWeekHashMap().put(driverCalendarWeek.getBeginDate(), driverCalendarWeek);
        return driverCalendarWeek;
    }

    public static DriverCalendarWeek setAnnualCalendarForDriver(String userId, LocalDate weekBeginDate) {
        return setPeriodicalCalendarForDriver(userId, weekBeginDate, false);
    }

    public static DriverCalendarWeek setMonthlyCalendarForDriver(String userId, LocalDate weekBeginDate) {
        return setPeriodicalCalendarForDriver(userId, weekBeginDate, true);
    }

    private static DriverCalendarWeek setPeriodicalCalendarForDriver(String userId, LocalDate weekBeginDate, boolean isForMonthlyOperation) {
        User user = getUserById(userId);
        if (user == null || user.isAdmin()) {
            return null;
        }
        DriverCalendarWeek driverCalendarWeek = getDriverCalendarWeekFromDB(user, weekBeginDate);
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

    public static DriverCalendarWeek setNextWeekCalendarForDriver(String userId, LocalDate weekBeginDate) {
        User user = getUserById(userId);
        if (user == null || user.isAdmin()) {
            return null;
        }
        DriverCalendarWeek driverCalendarWeek = getDriverCalendarWeekFromDB(user, weekBeginDate);
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

    private static void generateNewDriverCalendarWeek(User user, DriverCalendarWeek driverCalendarWeekTemplate, LocalDate newWeekBeginDate) {
        DriverCalendarWeek nextDriverCalendarWeek = getDriverCalendarWeekFromDB(user, newWeekBeginDate);
        if (nextDriverCalendarWeek == null) {
            nextDriverCalendarWeek = new DriverCalendarWeek(driverCalendarWeekTemplate, newWeekBeginDate);
        } else {
            nextDriverCalendarWeek.fillDayStatisticsArray(driverCalendarWeekTemplate);
        }
        nextDriverCalendarWeek.setUserId(user.getId());
        insertOrUpdateDriverCalendarWeek(nextDriverCalendarWeek);
    }

    public static MonthStatistics getMonthStatisticsForDriverUser(String userId, LocalDate monthBeginDate) {
        User user = getUserById(userId);
        if (user == null) {
            return null;
        }
        return getMonthStatisticsForDriverUser(user, monthBeginDate);
    }

    //rest
    public static MonthStatistics getMonthStatisticsForDriverUser(User user, LocalDate monthBeginDate) {
        if (user.isAdmin()) {
            return null;
        }
        int plannedHours = (user.getContractType() == ContractType.minijob) ? Settings.minijobMonthlyPlan : 0;
        int doneHours = 0;
        LocalDate[] weekBeginDates = UtilMethods.getWeekBeginDatesForMonth(monthBeginDate);
        for (LocalDate date : weekBeginDates) {
            DriverCalendarWeek driverCalendarWeek = getDriverCalendarWeekFromDB(user, date);
            if (driverCalendarWeek != null) {
                doneHours += driverCalendarWeek.getDoneHours();
            }
        }
        return new MonthStatistics(plannedHours, doneHours, monthBeginDate);
    }
    //rest
    public static MonthStatistics getMonthStatisticsForAdminUser(LocalDate monthBeginDate) {
        MonthStatistics monthStatistics = new MonthStatistics(monthBeginDate);
        monthStatistics.fixDoneHours(getAllUsers());
        monthStatistics.fixPlannedHours(getManualForecasting());
        return monthStatistics;
    }
    //rest
    public static AdminCalendarWeek getAdminCalendarWeek(LocalDate beginDate) {
        AdminCalendarWeek adminCalendarWeek = new AdminCalendarWeek(beginDate);

        adminCalendarWeek.fixDoneHours(getAllUsers());
        adminCalendarWeek.fixPlannedHours(getManualForecasting());
        return adminCalendarWeek;
    }

    public static DetailedDriverDayStatistics insertDriverDayStatistics(LocalDate date, String userId) {
        User user = getUserById(userId);
        if (user == null || user.isDeleted() || user.isAdmin()){
            return null;
        }

        LocalDate beginDateOfProperWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
        if (beginDateOfProperWeek.getMonthValue() != date.getMonthValue()){
            beginDateOfProperWeek = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
        }

        DriverCalendarWeek driverCalendarWeek = getDriverCalendarWeek(userId, beginDateOfProperWeek);
        DriverDayStatistics driverDayStatistics = driverCalendarWeek.getDayStatisticsArray()[date.getDayOfWeek().getValue() - beginDateOfProperWeek.getDayOfWeek().getValue()];

        if (!driverHasCalendarWeek(user, beginDateOfProperWeek)){
            insertOrUpdateDriverCalendarWeek(driverCalendarWeek);
        }else{
            if (driverDayStatistics.isNotEmpty()){
                return null;
            }
        }
        return driverDayStatistics.addDriverInfo(user);
    }

    //rest
    private static boolean driverHasCalendarWeek(User user, LocalDate beginDateOfProperWeek) {
        return user.getDriverCalendarWeekHashMap().get(beginDateOfProperWeek) != null;
    }

    public static DriverDayStatistics deleteDriverDayStatistics(LocalDate date, String userId) {
        User user = getUserById(userId);
        if (user == null){
            return null;
        }

        LocalDate beginDateOfProperWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
        if (beginDateOfProperWeek.getMonthValue() != date.getMonthValue()){
            beginDateOfProperWeek = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
        }

        DriverCalendarWeek driverCalendarWeekInDB = getDriverCalendarWeekFromDB(user, beginDateOfProperWeek);
        if (driverCalendarWeekInDB == null){
            return null;
        }

        DriverDayStatistics driverDayStatistics = driverCalendarWeekInDB.getDayStatisticsArray()[date.getDayOfWeek().getValue() - beginDateOfProperWeek.getDayOfWeek().getValue()];
        driverDayStatistics.cancelAll();
        insertOrUpdateDriverCalendarWeek(driverCalendarWeekInDB);
        return driverDayStatistics;
    }
    //rest
    public static DetailedAdminDayStatistics getDetailedAdminDayStatistics(LocalDate date) {
        DetailedAdminDayStatistics detailedAdminDayStatistics = new DetailedAdminDayStatistics(date);

        LocalDate beginDateOfProperWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
        if (beginDateOfProperWeek.getMonthValue() != date.getMonthValue()){
            beginDateOfProperWeek = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
        }

        ArrayList<DetailedDriverDayStatistics> dayStatisticsArrayList = new ArrayList<DetailedDriverDayStatistics>();
        for(User user: getAllUsers()){
            if (user.getDriverCalendarWeekHashMap() != null){
                DriverCalendarWeek driverCalendarWeek = user.getDriverCalendarWeekHashMap().get(beginDateOfProperWeek);
                if (driverCalendarWeek != null) {
                    DriverDayStatistics driverDayStatistics = driverCalendarWeek.getDayStatisticsArray()[date.getDayOfWeek().getValue() - beginDateOfProperWeek.getDayOfWeek().getValue()];
                    if (driverDayStatistics.isNotEmpty()){
                        dayStatisticsArrayList.add(driverDayStatistics.addDriverInfo(user));
                    }
                }
            }
        }
        detailedAdminDayStatistics.setDetailedDriverDayStatisticsArray(dayStatisticsArrayList.toArray(new DetailedDriverDayStatistics[dayStatisticsArrayList.size()]));
        return detailedAdminDayStatistics;
    }

    public static DetailedAdminDayStatistics insertOrUpdateDetailedAdminDayStatistics(DetailedAdminDayStatistics detailedAdminDayStatistics) {
        LocalDate beginDateOfProperWeek = detailedAdminDayStatistics.getDate().minusDays(detailedAdminDayStatistics.getDate().getDayOfWeek().getValue() - 1);
        if (beginDateOfProperWeek.getMonthValue() != detailedAdminDayStatistics.getDate().getMonthValue()){
            beginDateOfProperWeek = LocalDate.of(detailedAdminDayStatistics.getDate().getYear(), detailedAdminDayStatistics.getDate().getMonthValue(), 1);
        }

        for (DetailedDriverDayStatistics detailedDriverDayStatistics : detailedAdminDayStatistics.getDetailedDriverDayStatisticsArray()){
            DriverCalendarWeek driverCalendarWeekInDB = getDriverCalendarWeekFromDB(detailedDriverDayStatistics.getUserId(), beginDateOfProperWeek);
            DriverDayStatistics driverDayStatistics = driverCalendarWeekInDB.getDayStatisticsArray()[detailedAdminDayStatistics.getDate().getDayOfWeek().getValue() - beginDateOfProperWeek.getDayOfWeek().getValue()];
            for (int i = 0; i < driverDayStatistics.getHourStatisticsArray().length; i++){
                driverDayStatistics.getHourStatisticsArray()[i].setSelected(detailedDriverDayStatistics.getHourStatisticsArray()[i].isSelected());
            }
            insertOrUpdateDriverCalendarWeek(driverCalendarWeekInDB);
        }
        detailedAdminDayStatistics.init();
        return detailedAdminDayStatistics;
    }
    //rest
    private static DriverCalendarWeek getDriverCalendarWeekFromDB(String userId, LocalDate beginDateOfWeek) {
        return getDriverCalendarWeekFromDB(getUserById(userId), beginDateOfWeek);
    }
    //rest
    public static ArrayList<User> getActiveDriversForMonth(LocalDate beginDateOfMonth) {
        Collection<User> all = getAllUsers();
        ArrayList<User> result = new ArrayList<User>();
        for (User user : all){
            if (!user.isAdmin() && !user.isDeleted() &&  user.wasActive(beginDateOfMonth)){
                setDoneHours(user, beginDateOfMonth);
                result.add(user);
            }
        }
        return result;
    }
    //rest
    public static ArrayList<User> getActiveDrivers() {
        Collection<User> all = getAllUsers();
        ArrayList<User> result = new ArrayList<User>();
        for (User user : all){
            if (!user.isAdmin() && !user.isDeleted()){
                result.add(user);
            }
        }
        return result;
    }
}
