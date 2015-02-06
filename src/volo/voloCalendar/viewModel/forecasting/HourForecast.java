package volo.voloCalendar.viewModel.forecasting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 15/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HourForecast implements Serializable { // defines manual forecasting for an hour
    private int count; // how many drivers needed for the hour

    public HourForecast() {
        count = 0;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
