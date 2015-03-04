package volo.voloCalendar.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Emin Guliyev on 29/11/2014.
 */
public class UtilMethods {
    public final static String tokenVariableName = "token";
    //TODO 4: delete these two lines and usages
    public static String temp;
    public static boolean isTestingRestApi = false;
    public static String userIdVariableName = "userId";

    public static Object[] getArray(Object obj) {
        Object[] result;
        if (obj != null) {
            result = new Object[1];
            result[0] = obj;
        } else {
            result = new Object[0];
        }
        return result;
    }

    public static <T extends Object> HashMap<String, T> getHashMap(T obj) {
        HashMap<String, T> result = new HashMap<String, T>();
        if (obj != null) {
            String key = obj.getClass().getSimpleName();
            key = Character.toLowerCase(key.charAt(0)) + key.substring(1);
            result.put(key, obj);
        }
        return result;
    }

    public static <T> T convertJsonToObject(Class<T> objectType, String jsonString) throws IOException {
        T obj = new ObjectMapper().readValue(jsonString, objectType);
        return obj;
    }

    public static String convertObjectToJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    public static <T> HttpEntity<T> getAuthenticatedObjectHttpEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        String token;
        if (!isTestingRestApi) {
            token = (String) RequestContextHolder.currentRequestAttributes().getAttribute(tokenVariableName, RequestAttributes.SCOPE_SESSION);
        } else {
            token = temp;
        }
        headers.set("Authorization", token);
        return new HttpEntity<T>(body, headers);
    }


    public static java.sql.Date getSqlDate(Date date) {
        return new java.sql.Date(date.getYear(), date.getMonth(), date.getDate());
    }

    public static Date getLastDayOfMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

        Date date = calendar.getTime();
        return date;
    }
}
