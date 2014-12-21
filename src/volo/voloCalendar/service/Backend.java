package volo.voloCalendar.service;

import volo.voloCalendar.model.*;
import volo.voloCalendar.util.UtilMethods;
import volo.voloCalendar.viewModel.MonthStatistics;
import volo.voloCalendar.viewModel.UserTableLogic;

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
        User emin = new User("123", "eminguliyev1987@gmail.com", "sindibad1987", "Emin Guliyev");
        User mertcan = new User("456", "mcanyigin@gmail.com", "sindibad1987", "Mertcan Yigin"
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

    public static User getUserByEmail(String email){
        return usersByEmail.get(email);
    }

    public static User getUserById(String id){
        return usersById.get(id);
    }

    public static Collection<User> getAllUsers(){
        return usersByEmail.values();
    }

    public static User insertOrUpdateUser(User user){
        usersByEmail.put(user.getEmail(), user);
        usersById.put(user.getId(), user);
        return user;
    }

    public static User deleteUser(String id){
        User user = usersById.get(id);
        usersByEmail.remove(user.getEmail());
        usersById.remove(user.getId());
        return user;
    }

    public static User[] getUsers(UserTableLogic userTableLogic){
        Collection<User> users = Backend.getAllUsers();
        User[] userArray = new User[users.size()];
        userArray = users.toArray(userArray);
        int end = userTableLogic.getBeginIndex() - 1 + userTableLogic.getMaxNumber();
        if (end >= userArray.length){
            end = userArray.length;
        }
        userArray = Arrays.copyOfRange(userArray, userTableLogic.getBeginIndex() - 1, end);

        for (User user : userArray){
            LocalDate beginDateOfCurrentMonth = UtilMethods.getBeginDateOfCurrentMonth();
            MonthStatistics monthStatistics = getMonthStatistics(user, beginDateOfCurrentMonth);
            user.setDoneHours(monthStatistics.getDoneHours());
            monthStatistics = getMonthStatistics(user, beginDateOfCurrentMonth.minusMonths(1));
            user.setDiffPrevHours(monthStatistics.getDiffHours());
        }

        return userArray;
    }

    public static int getAllUsersCount() {
        return getAllUsers().size();
    }

    public static ManualForecasting getManualForecasting() {
        return manualForecasting;
    }

    public static void setManualForecasting(ManualForecasting manualForecasting){
        Backend.manualForecasting = manualForecasting;
    }

    public static DriverCalendarWeek getDriverCalendarWeek(String userId, LocalDate beginDate){
        User user = getUserById(userId);
        if (user == null){
            return null;
        }
        DriverCalendarWeek driverCalendarWeekInDB = getDriverCalendarWeekFromDB(user, beginDate);
        if (driverCalendarWeekInDB == null && getDriverCalendarWeekForever(user) != null){
            driverCalendarWeekInDB = new DriverCalendarWeek(getDriverCalendarWeekForever(user), beginDate);
        }
        if (driverCalendarWeekInDB == null){
            driverCalendarWeekInDB = new DriverCalendarWeek(userId, beginDate);
        }

        driverCalendarWeekInDB.setRequiredDriverCountStatistics(getManualForecasting());
        for (User userInDb : getAllUsers()) {
            DriverCalendarWeek driverCalendarWeekForDbUser = getDriverCalendarWeekFromDB(userInDb, driverCalendarWeekInDB.getBeginDate());
            driverCalendarWeekInDB.subtractStatistics(driverCalendarWeekForDbUser);
        }

        return driverCalendarWeekInDB;
    }

    private static DriverCalendarWeek getDriverCalendarWeekFromDB(User user, LocalDate beginDate) {
        return user.getDriverCalendarWeekHashMap().get(beginDate);
    }

    public static DriverCalendarWeek getDriverCalendarWeekForever(User user) {
        return user.getDriverCalendarWeekForever();
    }

    public static DriverCalendarWeek insertOrUpdateDriverCalendarWeek(DriverCalendarWeek driverCalendarWeek) {
        User user = getUserById(driverCalendarWeek.getUserId());
        if (user == null){
            return null;
        }
        user.getDriverCalendarWeekHashMap().put(driverCalendarWeek.getBeginDate(), driverCalendarWeek);
        return driverCalendarWeek;
    }

    public static DriverCalendarWeek setForeverCalendarPatternForDriver(String userId, DriverCalendarWeek driverCalendarWeek){
        User user = getUserById(userId);
        if (user == null){
            return null;
        }
        driverCalendarWeek.setUserId(userId);
        user.setDriverCalendarWeekForever(driverCalendarWeek);
        return driverCalendarWeek;
    }

    public static DriverCalendarWeek setMonthlyCalendarForDriver(String userId, DriverCalendarWeek driverCalendarWeek) {
        User user = getUserById(userId);
        if (user == null){
            return null;
        }
        LocalDate date = driverCalendarWeek.getBeginDate();
        Month month = date.getMonth();
        LocalDate nextWeekBeginDate = date.plusDays(7);
        while (month == nextWeekBeginDate.getMonth()){
            DriverCalendarWeek newDriverCalendarWeek = new DriverCalendarWeek(driverCalendarWeek, nextWeekBeginDate);
            newDriverCalendarWeek.setUserId(userId);
            insertOrUpdateDriverCalendarWeek(newDriverCalendarWeek);
            nextWeekBeginDate = date.plusDays(7);
        }
        return driverCalendarWeek;
    }

    public static DriverCalendarWeek setNextWeekCalendarForDriver(String userId, DriverCalendarWeek driverCalendarWeek) {
        User user = getUserById(userId);
        if (user == null){
            return null;
        }
        LocalDate date = driverCalendarWeek.getBeginDate();
        Month month = date.getMonth();
        LocalDate nextWeekBeginDate = date.plusDays(7);
        if (month == nextWeekBeginDate.getMonth()){
            DriverCalendarWeek newDriverCalendarWeek = new DriverCalendarWeek(driverCalendarWeek, nextWeekBeginDate);
            newDriverCalendarWeek.setUserId(userId);
            insertOrUpdateDriverCalendarWeek(newDriverCalendarWeek);
        }
        return driverCalendarWeek;
    }

    public static MonthStatistics getMonthStatistics(String userId, LocalDate monthBeginDate){
        User user = getUserById(userId);
        if (user == null){
            return null;
        }
        return getMonthStatistics(user, monthBeginDate);
    }

    public static MonthStatistics getMonthStatistics(User user, LocalDate monthBeginDate) {
        int plannedHours = (user.getContractType() == ContractType.minijob)? User.minijobPlan :0;
        int doneHours = 0;
        LocalDate[] weekBeginDates = UtilMethods.getWeekBeginDatesForMonth(monthBeginDate);
        for (LocalDate date : weekBeginDates) {
            DriverCalendarWeek driverCalendarWeek = getDriverCalendarWeekFromDB(user, date);
            if (driverCalendarWeek != null) {
                doneHours += driverCalendarWeek.getDoneHours();
            }
        }
        return new MonthStatistics(plannedHours, doneHours);
    }
}
