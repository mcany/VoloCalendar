package volo.voloCalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import volo.voloCalendar.viewModel.driver.DriverCalendarWeek;
import volo.voloCalendar.service.CalendarLogic;
import volo.voloCalendar.util.CalendarUtilMethods;
import volo.voloCalendar.viewModel.common.CalendarViewModel;
import volo.voloCalendar.viewModel.common.MonthStatistics;

import java.time.LocalDate;

/**
 * Created by Emin Guliyev on 20/12/2014.
 */
@RestController
@RequestMapping("/driver")
@Secured({"ROLE_DRIVER"})
public class DriverCalendarController {
    @Autowired
    public CalendarLogic calendarLogic;
    private static final int calendarMonthsCount = 3;

    @RequestMapping(value = "/calendar", method = RequestMethod.GET, produces = "application/json")
    public CalendarViewModel calendar() {
        return CalendarUtilMethods.getCalendarViewModel(calendarMonthsCount, CalendarUtilMethods.getBeginLocalDateOfCurrentMonth());
    }

    @RequestMapping(value = "/month/{userId}/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public MonthStatistics month(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate monthBeginDate = LocalDate.of(year, month, day);
        return calendarLogic.getMonthStatisticsForDriverUser(userId, monthBeginDate);
    }

    @RequestMapping(value = "/week/{userId}/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public DriverCalendarWeek week(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate weekBeginDate = LocalDate.of(year, month, day);
        return calendarLogic.getDriverCalendarWeek(userId, weekBeginDate);
    }

    @RequestMapping(value = "/week", method = RequestMethod.POST, produces = "application/json")
    public DriverCalendarWeek addDriverCalendarWeek(@RequestBody DriverCalendarWeek driverCalendarWeek) {
        driverCalendarWeek.init();
        return calendarLogic.insertOrUpdateDriverCalendarWeek(driverCalendarWeek);
    }

    @RequestMapping(value = "/setNextWeek/{userId}/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public DriverCalendarWeek setNextWeek(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate weekBeginDate = LocalDate.of(year, month, day);
        return calendarLogic.setNextWeekCalendarForDriver(userId, weekBeginDate);
    }

    @RequestMapping(value = "/setMonth/{userId}/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public DriverCalendarWeek setMonth(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate weekBeginDate = LocalDate.of(year, month, day);
        return calendarLogic.setMonthlyCalendarForDriver(userId, weekBeginDate);
    }

    @RequestMapping(value = "/setYear/{userId}/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public DriverCalendarWeek setYear(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate weekBeginDate = LocalDate.of(year, month, day);
        return calendarLogic.setAnnualCalendarForDriver(userId, weekBeginDate);
    }
}
