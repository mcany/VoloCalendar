package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 15/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HourForecast implements Serializable {
    private int count;

    public HourForecast() {
        count = 5;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
