package volo.voloCalendar.util;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Emin Guliyev on 29/11/2014.
 */
public class UtilMethods {
    public static Object[] getArray(Object obj){
        Object[] result;
        if (obj != null){
            result = new Object[1];
            result[0] = obj;
        }else {
            result = new Object[0];
        }
        return result;
    }

    public static <T extends Object> HashMap<String ,T> getHashMap(T obj){
        HashMap<String, T> result = new HashMap<String, T>();
        if (obj != null) {
            String key = obj.getClass().getSimpleName();
            key = Character.toLowerCase(key.charAt(0)) + key.substring(1);
            result.put(key, obj);
        }
        return result;
    }


    public static LocalDate[] getMonthBeginDatesForCalendar(int monthCount){
        ArrayList<LocalDate> list = new ArrayList<LocalDate>();
        LocalDate monthBeginDate = getBeginDateOfCurrentMonth();
        list.add(monthBeginDate);
        for (int i = 1; i < monthCount; i++) {
            monthBeginDate = monthBeginDate.plusMonths(1);
            list.add(monthBeginDate);
        }
        return list.toArray(new LocalDate[list.size()]);
    }

    public static LocalDate getBeginDateOfCurrentMonth() {
        return LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
    }

    public static LocalDate[] getWeekBeginDatesForMonth(LocalDate monthBeginDate){
        ArrayList<LocalDate> list = new ArrayList<LocalDate>();
        LocalDate date = monthBeginDate;
        Month month = date.getMonth();
        do{
            list.add(date);
            date = date.plusDays(8 - date.getDayOfWeek().getValue());
        }while (date.getMonth() == month);
        return list.toArray(new LocalDate[list.size()]);
    }

}
