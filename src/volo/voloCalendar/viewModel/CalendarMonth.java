package volo.voloCalendar.viewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Emin Guliyev on 22/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarMonth implements Serializable{
    private LocalDate beginDate;
    private DriverCalendarWeekLight[] driverCalendarWeekLights;

    public CalendarMonth(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public DriverCalendarWeekLight[] getDriverCalendarWeekLights() {
        return driverCalendarWeekLights;
    }

    public void setDriverCalendarWeekLights(DriverCalendarWeekLight[] driverCalendarWeekLights) {
        this.driverCalendarWeekLights = driverCalendarWeekLights;
    }
}
