package volo.voloCalendar.util;

import volo.voloCalendar.viewModel.common.CalendarMonth;
import volo.voloCalendar.viewModel.common.CalendarViewModel;
import volo.voloCalendar.viewModel.common.CalendarWeekLight;

import java.sql.Date;
import org.joda.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Emin Guliyev on 28/01/2015.
 */
public class CalendarUtilMethods {
    public static CalendarViewModel getCalendarViewModel(int calendarMonthsCount, LocalDate beginDate) {
        CalendarViewModel calendarViewModel = new CalendarViewModel();
        calendarViewModel.setCalendarMonths(new CalendarMonth[calendarMonthsCount]);
        LocalDate[] monthBeginDates = getMonthBeginLocalDatesForCalendar(calendarMonthsCount, beginDate);
        for (int i = 0; i < monthBeginDates.length; i++) {
            LocalDate monthBeginDate = monthBeginDates[i];
            CalendarMonth calendarMonth = getCalendarMonth(monthBeginDate);
            calendarViewModel.getCalendarMonths()[i] = calendarMonth;
        }
        return calendarViewModel;
    }

    private static CalendarMonth getCalendarMonth(LocalDate monthBeginDate) {
        CalendarMonth calendarMonth = new CalendarMonth(monthBeginDate);
        LocalDate[] weekBeginDates = getWeekBeginLocalDatesForMonth(monthBeginDate);
        CalendarWeekLight[] calendarWeekLights = new CalendarWeekLight[weekBeginDates.length];
        for (int j = 0; j < weekBeginDates.length; j++) {
            calendarWeekLights[j] = new CalendarWeekLight(weekBeginDates[j]);
        }
        calendarMonth.setCalendarWeekLights(calendarWeekLights);
        return calendarMonth;
    }

    public static LocalDate[] getMonthBeginLocalDatesForCalendar(int monthCount, LocalDate beginDate) {
        ArrayList<LocalDate> list = new ArrayList<LocalDate>();
        list.add(beginDate);
        for (int i = 1; i < monthCount; i++) {
            beginDate = beginDate.plusMonths(1);
            list.add(beginDate);
        }
        return list.toArray(new LocalDate[list.size()]);
    }

    public static LocalDate getBeginLocalDateOfCurrentMonth() {
        return new LocalDate(LocalDate.now().getYear(), LocalDate.now().getMonthOfYear(), 1);
    }

    public static LocalDate[] getWeekBeginLocalDatesForMonth(LocalDate monthBeginDate) {
        ArrayList<LocalDate> list = new ArrayList<LocalDate>();
        LocalDate date = monthBeginDate;
        int month = date.getMonthOfYear();
        do {
            list.add(date);
            date = date.plusDays(8 - date.getDayOfWeek());
        } while (date.getMonthOfYear() == month);
        return list.toArray(new LocalDate[list.size()]);
    }

    public static ArrayList<Date> getWeekBeginDatesForMonth(LocalDate monthBeginDate) {
        ArrayList<Date> list = new ArrayList<Date>();
        LocalDate localDate = monthBeginDate;
        int month = localDate.getMonthOfYear();
        do {
            list.add(new Date(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth()));
            localDate = localDate.plusDays(8 - localDate.getDayOfWeek());
        } while (localDate.getMonthOfYear() == month);
        return list;
    }
}
