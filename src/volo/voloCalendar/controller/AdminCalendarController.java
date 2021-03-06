package volo.voloCalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import volo.voloCalendar.service.AdminCalendarLogic;
import volo.voloCalendar.util.CalendarUtilMethods;
import volo.voloCalendar.viewModel.admin.AdminCalendarWeek;
import volo.voloCalendar.viewModel.admin.DetailedAdminDayStatistics;
import volo.voloCalendar.viewModel.admin.DetailedDriverDayStatistics;
import volo.voloCalendar.viewModel.common.CalendarViewModel;
import volo.voloCalendar.viewModel.common.MonthStatistics;
import volo.voloCalendar.viewModel.driver.DriverDayStatistics;

import org.joda.time.LocalDate;
/**
 * Created by Emin Guliyev on 07/01/2015.
 */
@RestController
@RequestMapping("/admin/schedule")
@Secured({"ROLE_ADMIN"})
public class AdminCalendarController {

    @Autowired
    public AdminCalendarLogic adminCalendarLogic;

    private static final int calendarMonthsCount = 6;

    @RequestMapping(value = "/calendar", method = RequestMethod.GET, produces = "application/json")
    public CalendarViewModel calendar() {
        LocalDate threeMonthBefore = new LocalDate(LocalDate.now().getYear(), LocalDate.now().getMonthOfYear(), 1);
        threeMonthBefore = threeMonthBefore.minusMonths(3);
        return CalendarUtilMethods.getCalendarViewModel(calendarMonthsCount, threeMonthBefore);
    }

    @RequestMapping(value = "/month/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public MonthStatistics month(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate monthBeginDate = new LocalDate(year, month, day);
        return adminCalendarLogic.getMonthStatisticsForAdminUser(monthBeginDate);
    }

    @RequestMapping(value = "/week/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public AdminCalendarWeek week(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate monthBeginDate = new LocalDate(year, month, day);
        return adminCalendarLogic.getAdminCalendarWeek(monthBeginDate);
    }

    @RequestMapping(value = "/day/{year}-{month}-{day}", method = RequestMethod.GET, produces = "application/json")
    public DetailedAdminDayStatistics day(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate date = new LocalDate(year, month, day);
        return adminCalendarLogic.getDetailedAdminDayStatistics(date);
    }

    @RequestMapping(value = "/day/{userId}/{year}-{month}-{day}", method = RequestMethod.POST, produces = "application/json")
    public DetailedDriverDayStatistics addDriverDayStatistics(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate date = new LocalDate(year, month, day);
        return adminCalendarLogic.insertDriverDayStatistics(date, userId);
    }

    @RequestMapping(value = "/day/{userId}/{year}-{month}-{day}", method = RequestMethod.DELETE, produces = "application/json")
    public DriverDayStatistics deleteDriverDayStatistics(@PathVariable String userId, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        LocalDate date = new LocalDate(year, month, day);
        return adminCalendarLogic.deleteDriverDayStatistics(date, userId);
    }

    @RequestMapping(value = "/detailedAdminDay", method = RequestMethod.POST, produces = "application/json")
    public DetailedAdminDayStatistics saveDetailedAdminDayStatistics(@RequestBody DetailedAdminDayStatistics detailedAdminDayStatistics) {
        return adminCalendarLogic.insertOrUpdateDetailedAdminDayStatistics(detailedAdminDayStatistics);
    }
}
