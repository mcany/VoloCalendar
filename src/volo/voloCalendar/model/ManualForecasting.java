package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 15/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManualForecasting implements Serializable{
    private HourForecast[] monday;
    private HourForecast[] tuesday;
    private HourForecast[] wednesday;
    private HourForecast[] thursday;
    private HourForecast[] friday;
    private HourForecast[] saturday;
    private HourForecast[] sunday;

    public ManualForecasting() {
        monday = new HourForecast[24];
        setZeroToAll(monday);
        tuesday = new HourForecast[24];
        setZeroToAll(tuesday);
        wednesday = new HourForecast[24];
        setZeroToAll(wednesday);
        thursday = new HourForecast[24];
        setZeroToAll(thursday);
        friday = new HourForecast[24];
        setZeroToAll(friday);
        saturday = new HourForecast[24];
        setZeroToAll(saturday);
        sunday = new HourForecast[24];
        setZeroToAll(sunday);
    }

    private void setZeroToAll(HourForecast[] day) {
        for (int i = 0; i < day.length; i++){
            day[i] = new HourForecast();
        }
    }

    public HourForecast[] getMonday() {
        return monday;
    }

    public void setMonday(HourForecast[] monday) {
        this.monday = monday;
    }

    public HourForecast[] getTuesday() {
        return tuesday;
    }

    public void setTuesday(HourForecast[] tuesday) {
        this.tuesday = tuesday;
    }

    public HourForecast[] getWednesday() {
        return wednesday;
    }

    public void setWednesday(HourForecast[] wednesday) {
        this.wednesday = wednesday;
    }

    public HourForecast[] getThursday() {
        return thursday;
    }

    public void setThursday(HourForecast[] thursday) {
        this.thursday = thursday;
    }

    public HourForecast[] getFriday() {
        return friday;
    }

    public void setFriday(HourForecast[] friday) {
        this.friday = friday;
    }

    public HourForecast[] getSaturday() {
        return saturday;
    }

    public void setSaturday(HourForecast[] saturday) {
        this.saturday = saturday;
    }

    public HourForecast[] getSunday() {
        return sunday;
    }

    public void setSunday(HourForecast[] sunday) {
        this.sunday = sunday;
    }
}
