package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 20/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HourStatistics implements Serializable {
    private int requiredDriverCount;
    private boolean selected;
    private DayStatistics dayStatistics;

    public HourStatistics() {
    }

    public HourStatistics(DayStatistics dayStatistics) {
        this.dayStatistics = dayStatistics;
    }

    public HourStatistics(int requiredDriverCount, DayStatistics dayStatistics) {
        this(dayStatistics);
        this.requiredDriverCount = requiredDriverCount;
    }

    public int getRequiredDriverCount() {
        return requiredDriverCount;
    }

    public void setRequiredDriverCount(int requiredDriverCount) {
        this.requiredDriverCount = requiredDriverCount;
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

    public void decreaseRequiredDriverCount() {
        requiredDriverCount--;
    }

    public void init(DayStatistics dayStatistics) {
        this.dayStatistics = dayStatistics;
    }
}
