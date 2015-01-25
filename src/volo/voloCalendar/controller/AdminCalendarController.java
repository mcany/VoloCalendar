package volo.voloCalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import volo.voloCalendar.model.*;
import volo.voloCalendar.service.CalendarLogic;
import volo.voloCalendar.util.UtilMethods;
import volo.voloCalendar.viewModel.*;

import java.time.LocalDate;

/**
 * Created by Emin Guliyev on 07/01/2015.
 */
@RestController
@RequestMapping("/admin/schedule")
@Secured({"ROLE_ADMIN"})
public class AdminCalendarController {

    @Autowired
    public CalendarLogic calendarLogic;

    private static final int calendarMonthsCount = 6;

    @RequestMapping(value = "/calendar", method = RequestMethod.GET, produces = "application/json")
    public CalendarViewModel calendar() {
        LocalDate threeMonthBefore = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
        threeMonthBefore = threeMonthBefore.minusMonths(3);
        return UtilMethods.getCalendarViewModel(calendarMonthsCount, threeMonthBefore);
    }

    @RequestMapping(value = "/month/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public MonthStatistics month(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate monthBeginDate = LocalDate.of(year, month, day);
        return calendarLogic.getMonthStatisticsForAdminUser(monthBeginDate);
    }

    @RequestMapping(value = "/week/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public AdminCalendarWeek week(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate monthBeginDate = LocalDate.of(year, month, day);
        return calendarLogic.getAdminCalendarWeek(monthBeginDate);
    }

    @RequestMapping(value = "/day/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public DetailedAdminDayStatistics day(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return calendarLogic.getDetailedAdminDayStatistics(date);
    }

    @RequestMapping(value = "/day/{userId}/{year}-{month}-{day}", method = RequestMethod.POST, produces = "application/json")
    public DetailedDriverDayStatistics addDriverDayStatistics(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return calendarLogic.insertDriverDayStatistics(date, userId);
    }

    @RequestMapping(value = "/day/{userId}/{year}-{month}-{day}", method = RequestMethod.DELETE, produces = "application/json")
    public DriverDayStatistics deleteDriverDayStatistics(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return calendarLogic.deleteDriverDayStatistics(date, userId);
    }

    @RequestMapping(value = "/detailedAdminDay", method = RequestMethod.POST, produces = "application/json")
    public DetailedAdminDayStatistics saveDetailedAdminDayStatistics(@RequestBody DetailedAdminDayStatistics detailedAdminDayStatistics) {
        return calendarLogic.insertOrUpdateDetailedAdminDayStatistics(detailedAdminDayStatistics);
    }
}
