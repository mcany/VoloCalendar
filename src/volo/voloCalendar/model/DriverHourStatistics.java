package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 07/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverHourStatistics extends HourStatistics implements Serializable {
    private boolean selected;

    public DriverHourStatistics() {
    }

    public DriverHourStatistics(DriverDayStatistics dayStatistics) {
        super(dayStatistics);
    }

    public DriverHourStatistics(int requiredDriverCount, DriverDayStatistics dayStatistics) {
        super(requiredDriverCount, dayStatistics);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isEnabled() {
        return dayStatistics.isActive() && requiredDriverCount > 0;
    }
}
