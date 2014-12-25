package volo.voloCalendar.service;

import volo.voloCalendar.model.*;
import volo.voloCalendar.util.UtilMethods;
import volo.voloCalendar.viewModel.MonthStatistics;
import volo.voloCalendar.viewModel.UserTableViewModel;

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
        User emin = new User("456", "mcanyigin@gmail.com", "sindibad1987", "Mertcan Yigin");
        User mertcan = new User("123", "eminguliyev1987@gmail.com", "sindibad1987", "Emin Guliyev"
                , "Christ-Probst.", "10/142", "80805", "Munich", "017678947235", TransportType.bicycle, TelephoneType.ios
                , "DE64IRCE92222212345678", "BINFFDDDXXX", ContractType.minijob);
        usersByEmail.put(emin.getEmail(), emin);
        usersById.put(emin.getId(), emin);
        usersByEmail.put(mertcan.getEmail(), mertcan);
        usersById.put(mertcan.getId(), mertcan);
        for (int i=0; i < 35; i++){
            emin = new User(emin);
            emin.setId(UUID.randomUUID().toString());
            emin.setEmail(UUID.randomUUID().toString() + "@gmail.com");
            usersByEmail.put(emin.getEmail(), emin);
            usersById.put(emin.getId(), emin);
        }

        for (int i=0; i < 35; i++){
            mertcan = new User(mertcan);
            mertcan.setId(UUID.randomUUID().toString());
            mertcan.setEmail(UUID.randomUUID().toString() + "@gmail.com");
            usersByEmail.put(mertcan.getEmail(), mertcan);
            usersById.put(mertcan.getId(), mertcan);
        }
    }
    //rest
    public static User getUserByEmail(String email){
        return usersByEmail.get(email);
    }
    //rest
    public static User getUserById(String id){
        return usersById.get(id);
    }
    //rest
    public static Collection<User> getAllUsers(){
        return usersByEmail.values();
    }
    //rest
    public static User insertOrUpdateUser(User user){
        User userInDb = getUserById(user.getId());
        if (userInDb != null && !userInDb.isAdmin() && user.isAdmin()){
            userInDb.setDriverCalendarWeekHashMap(new HashMap<LocalDate, DriverCalendarWeek>());
        }
        usersByEmail.put(user.getEmail(), user);
        usersById.put(user.getId(), user);
        return user;
    }
    //rest
    public static User deleteUser(String id){
        User user = usersById.get(id);
        usersByEmail.remove(user.getEmail());
        usersById.remove(user.getId());
        return user;
    }

    public static User[] getUsers(UserTableViewModel userTableViewModel){
        User[] userArray = getSortedFilteredPagedUsersWithoutStatistics(userTableViewModel);

        for (User user : userArray){
            LocalDate beginDateOfCurrentMonth = UtilMethods.getBeginDateOfCurrentMonth();
            MonthStatistics monthStatistics = getMonthStatistics(user, beginDateOfCurrentMonth);
            if (monthStatistics != null) {
                user.setDoneHours(monthStatistics.getDoneHours());
            }else{
                user.setDoneHours(0);
            }
            monthStatistics = getMonthStatistics(user, beginDateOfCurrentMonth.minusMonths(1));
            if (monthStatistics != null) {
                user.setDiffPrevHours(monthStatistics.getDiffHours());
            }else{
                user.setDiffPrevHours(0);
            }
        }

        return userArray;
    }
    //rest
    private static User[] getSortedFilteredPagedUsersWithoutStatistics(UserTableViewModel userTableViewModel) {
        Collection<User> users = Backend.getAllUsers();
        User[] userArray = new User[users.size()];
        userArray = users.toArray(userArray);
        int end = userTableViewModel.getBeginIndex() - 1 + userTableViewModel.getMaxNumber();
        if (end >= userArray.length){
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
    public static void setManualForecasting(ManualForecasting manualForecasting){
        Backend.manualForecasting = manualForecasting;
    }

    public static DriverCalendarWeek getDriverCalendarWeek(String userId, LocalDate beginDate){
        User user = getUserById(userId);
        return getDriverCalendarWeek(user, beginDate);
    }

    public static DriverCalendarWeek getDriverCalendarWeek(User user, LocalDate beginDate) {
        if (user == null || user.isAdmin()){
            return null;
        }
        DriverCalendarWeek driverCalendarWeekInDB = getDriverCalendarWeekFromDB(user, beginDate);
        if (driverCalendarWeekInDB == null){
            driverCalendarWeekInDB = new DriverCalendarWeek(user.getId(), beginDate);
        }

        driverCalendarWeekInDB = getStatisticsForDriverWeek(driverCalendarWeekInDB);
        return driverCalendarWeekInDB;
    }
    //rest
    private static DriverCalendarWeek getStatisticsForDriverWeek(DriverCalendarWeek driverCalendarWeekInDB) {
        driverCalendarWeekInDB.setRequiredDriverCountStatistics(getManualForecasting());
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
        if (user == null || user.isAdmin()){
            return null;
        }
        user.getDriverCalendarWeekHashMap().put(driverCalendarWeek.getBeginDate(), driverCalendarWeek);
        return driverCalendarWeek;
    }

    public static DriverCalendarWeek setAnnualCalendarForDriver(String userId, LocalDate weekBeginDate){
        return setPeriodicalCalendarForDriver(userId, weekBeginDate, false);
    }

    public static DriverCalendarWeek setMonthlyCalendarForDriver(String userId, LocalDate weekBeginDate) {
        return setPeriodicalCalendarForDriver(userId, weekBeginDate, true);
    }

    private static DriverCalendarWeek setPeriodicalCalendarForDriver(String userId, LocalDate weekBeginDate, boolean isForMonthlyOperation) {
        User user = getUserById(userId);
        if (user == null || user.isAdmin()){
            return null;
        }
        DriverCalendarWeek driverCalendarWeek = getDriverCalendarWeekFromDB(user, weekBeginDate);
        if (driverCalendarWeek == null){
            return null;
        }
        LocalDate date = driverCalendarWeek.getBeginDate();
        Month month = date.getMonth();
        int year = date.getYear();
        LocalDate nextWeekBeginDate = date.plusDays(8 - date.getDayOfWeek().getValue());
        while (isForMonthlyOperation ? (month == nextWeekBeginDate.getMonth()): (year == nextWeekBeginDate.getYear())){
            generateNewDriverCalendarWeek(user, driverCalendarWeek, nextWeekBeginDate);
            nextWeekBeginDate = nextWeekBeginDate.plusDays(7);
        }
        return driverCalendarWeek;
    }

    public static DriverCalendarWeek setNextWeekCalendarForDriver(String userId, LocalDate weekBeginDate) {
        User user = getUserById(userId);
        if (user == null || user.isAdmin()){
            return null;
        }
        DriverCalendarWeek driverCalendarWeek = getDriverCalendarWeekFromDB(user, weekBeginDate);
        if (driverCalendarWeek == null){
            return null;
        }
        LocalDate date = driverCalendarWeek.getBeginDate();
        Month month = date.getMonth();
        LocalDate nextWeekBeginDate = date.plusDays(8 - date.getDayOfWeek().getValue());
        if (month == nextWeekBeginDate.getMonth()){
            generateNewDriverCalendarWeek(user, driverCalendarWeek, nextWeekBeginDate);
        }
        return driverCalendarWeek;
    }

    public static void generateNewDriverCalendarWeek(User user, DriverCalendarWeek driverCalendarWeekTemplate, LocalDate newWeekBeginDate) {
        DriverCalendarWeek nextDriverCalendarWeek = getDriverCalendarWeekFromDB(user, newWeekBeginDate);
        if (nextDriverCalendarWeek == null){
            nextDriverCalendarWeek = new DriverCalendarWeek(driverCalendarWeekTemplate, newWeekBeginDate);
        }else{
            nextDriverCalendarWeek.fillDayStatisticsArray(driverCalendarWeekTemplate);
        }
        nextDriverCalendarWeek.setUserId(user.getId());
        insertOrUpdateDriverCalendarWeek(nextDriverCalendarWeek);
    }

    public static MonthStatistics getMonthStatistics(String userId, LocalDate monthBeginDate){
        User user = getUserById(userId);
        if (user == null){
            return null;
        }
        return getMonthStatistics(user, monthBeginDate);
    }
    //rest
    public static MonthStatistics getMonthStatistics(User user, LocalDate monthBeginDate) {
        if (user.isAdmin()){
            return null;
        }
        int plannedHours = (user.getContractType() == ContractType.minijob)? User.minijobPlan :0;
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

}
