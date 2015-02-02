package volo.voloCalendar.viewModel.forecasting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 15/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManualForecasting implements Serializable { // defines manual forecasting for all weeks
    private HourForecast[][] days; // 7*24 array for hour forecasting within week

    public ManualForecasting() {
        days = new HourForecast[7][];
        for (int i = 0; i < 7; i++) {
            days[i] = new HourForecast[24];
            setZeroToAll(days[i]);
        }
    }

    private void setZeroToAll(HourForecast[] day) {
        for (int i = 0; i < day.length; i++) {
            day[i] = new HourForecast();
        }
    }

    public HourForecast[][] getDays() {
        return days;
    }

    public void setDays(HourForecast[][] days) {
        this.days = days;
    }
}
