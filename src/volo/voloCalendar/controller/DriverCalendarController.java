package volo.voloCalendar.controller;

import org.springframework.web.bind.annotation.*;
import volo.voloCalendar.model.DriverCalendarWeek;
import volo.voloCalendar.service.Backend;
import volo.voloCalendar.util.UtilMethods;
import volo.voloCalendar.viewModel.CalendarMonth;
import volo.voloCalendar.viewModel.DriverCalendarViewModel;
import volo.voloCalendar.viewModel.DriverCalendarWeekLight;
import volo.voloCalendar.viewModel.MonthStatistics;

import java.time.LocalDate;

/**
 * Created by Emin Guliyev on 20/12/2014.
 */
@RestController
@RequestMapping("/driver")
public class DriverCalendarController {

    private static final int calendarMonthsCount = 3;

    @RequestMapping(value = "/calendar/{userId}", method = RequestMethod.GET, produces = "application/json")
    public DriverCalendarViewModel calendar(@PathVariable String userId){
        DriverCalendarViewModel driverCalendarViewModel = new DriverCalendarViewModel();
        driverCalendarViewModel.setCalendarMonths(new CalendarMonth[calendarMonthsCount]);
        LocalDate[] monthBeginDates = UtilMethods.getMonthBeginDatesForCalendar(calendarMonthsCount);
        for(int i = 0; i < monthBeginDates.length; i++){
            LocalDate monthBeginDate = monthBeginDates[i];
            CalendarMonth calendarMonth = getCalendarMonth(monthBeginDate);
            driverCalendarViewModel.getCalendarMonths()[i] = calendarMonth;
        }
        return driverCalendarViewModel;
    }

    private CalendarMonth getCalendarMonth(LocalDate monthBeginDate) {
        CalendarMonth calendarMonth = new CalendarMonth(monthBeginDate);
        LocalDate[] weekBeginDates = UtilMethods.getWeekBeginDatesForMonth(monthBeginDate);
        DriverCalendarWeekLight[] driverCalendarWeekLights = new DriverCalendarWeekLight[weekBeginDates.length];
        for (int j = 0; j < weekBeginDates.length; j++){
            driverCalendarWeekLights[j] = new DriverCalendarWeekLight(weekBeginDates[j]);
        }
        calendarMonth.setDriverCalendarWeekLights(driverCalendarWeekLights);
        return calendarMonth;
    }

    @RequestMapping(value = "/month/{userId}/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public MonthStatistics month(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day){
        LocalDate monthBeginDate = LocalDate.of(year,month,day);
        return Backend.getMonthStatistics(userId, monthBeginDate);
    }

    @RequestMapping(value = "/week/{userId}/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public DriverCalendarWeek week(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day){
        LocalDate monthBeginDate = LocalDate.of(year,month,day);
        return Backend.getDriverCalendarWeek(userId, monthBeginDate);
    }

    @RequestMapping(value = "/week", method = RequestMethod.POST, produces = "application/json")
    public DriverCalendarWeek week(@RequestBody DriverCalendarWeek driverCalendarWeek){
        return Backend.insertOrUpdateDriverCalendarWeek(driverCalendarWeek);
    }

    @RequestMapping(value = "/setNextWeek/{userId}/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public DriverCalendarWeek setNextWeek(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day){
        LocalDate weekBeginDate = LocalDate.of(year,month,day);
        return Backend.setNextWeekCalendarForDriver(userId, weekBeginDate);
    }

    @RequestMapping(value = "/setNextMonth/{userId}/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public DriverCalendarWeek setNextMonth(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day){
        LocalDate weekBeginDate = LocalDate.of(year,month,day);
        return Backend.setMonthlyCalendarForDriver(userId, weekBeginDate);
    }

    @RequestMapping(value = "/setForever/{userId}/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public DriverCalendarWeek setForever(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day){
        LocalDate weekBeginDate = LocalDate.of(year,month,day);
        return Backend.setForeverCalendarPatternForDriver(userId, weekBeginDate);
    }
}
