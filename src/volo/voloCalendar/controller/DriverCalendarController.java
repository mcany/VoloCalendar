package volo.voloCalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import volo.voloCalendar.model.DriverCalendarWeek;
import volo.voloCalendar.service.Logic;
import volo.voloCalendar.util.UtilMethods;
import volo.voloCalendar.viewModel.CalendarViewModel;
import volo.voloCalendar.viewModel.MonthStatistics;

import java.time.LocalDate;

/**
 * Created by Emin Guliyev on 20/12/2014.
 */
@RestController
@RequestMapping("/driver")
@Secured({"ROLE_DRIVER"})
public class DriverCalendarController {
    @Autowired
    public Logic logic;
    private static final int calendarMonthsCount = 3;

    @RequestMapping(value = "/calendar", method = RequestMethod.GET, produces = "application/json")
    public CalendarViewModel calendar() {
        return UtilMethods.getCalendarViewModel(calendarMonthsCount, UtilMethods.getBeginDateOfCurrentMonth());
    }

    @RequestMapping(value = "/month/{userId}/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public MonthStatistics month(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate monthBeginDate = LocalDate.of(year, month, day);
        return logic.getMonthStatisticsForDriverUser(userId, monthBeginDate);
    }

    @RequestMapping(value = "/week/{userId}/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public DriverCalendarWeek week(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate weekBeginDate = LocalDate.of(year, month, day);
        return logic.getDriverCalendarWeek(userId, weekBeginDate);
    }

    @RequestMapping(value = "/week", method = RequestMethod.POST, produces = "application/json")
    public DriverCalendarWeek addDriverCalendarWeek(@RequestBody DriverCalendarWeek driverCalendarWeek) {
        driverCalendarWeek.init();
        return logic.insertOrUpdateDriverCalendarWeek(driverCalendarWeek);
    }

    @RequestMapping(value = "/setNextWeek/{userId}/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public DriverCalendarWeek setNextWeek(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate weekBeginDate = LocalDate.of(year, month, day);
        return logic.setNextWeekCalendarForDriver(userId, weekBeginDate);
    }

    @RequestMapping(value = "/setMonth/{userId}/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public DriverCalendarWeek setMonth(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate weekBeginDate = LocalDate.of(year, month, day);
        return logic.setMonthlyCalendarForDriver(userId, weekBeginDate);
    }

    @RequestMapping(value = "/setYear/{userId}/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public DriverCalendarWeek setYear(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate weekBeginDate = LocalDate.of(year, month, day);
        return logic.setAnnualCalendarForDriver(userId, weekBeginDate);
    }
}
