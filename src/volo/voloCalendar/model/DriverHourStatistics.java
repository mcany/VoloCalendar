package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 07/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverHourStatistics extends HourStatistics implements Serializable {
    private boolean selected;
    private DriverDayStatistics dayStatistics;

    public DriverHourStatistics() {
    }

    public DriverHourStatistics(DriverDayStatistics dayStatistics, int index) {
        this.dayStatistics = dayStatistics;
        this.index = index;
    }

    public DriverHourStatistics( DriverDayStatistics dayStatistics, int requiredDriverCount, int index) {
        this(dayStatistics, index);
        this.planningHours = requiredDriverCount;
    }

    public void init(DriverDayStatistics dayStatistics) {
        this.dayStatistics = dayStatistics;
    }

    public boolean isEnabled() {
        return dayStatistics.isActive() && planningHours > 0;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
