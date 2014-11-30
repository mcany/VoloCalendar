package volo.voloCalendar.util;

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
}
