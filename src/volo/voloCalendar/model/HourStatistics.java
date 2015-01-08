package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 20/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HourStatistics implements Serializable {
    protected int requiredDriverCount;
    protected DriverDayStatistics dayStatistics;

    public HourStatistics() {
    }

    protected HourStatistics(DriverDayStatistics dayStatistics) {
        this.dayStatistics = dayStatistics;
    }

    protected HourStatistics(int requiredDriverCount, DriverDayStatistics dayStatistics) {
        this(dayStatistics);
        this.requiredDriverCount = requiredDriverCount;
    }

    public int getRequiredDriverCount() {
        return requiredDriverCount;
    }

    public void setRequiredDriverCount(int requiredDriverCount) {
        this.requiredDriverCount = requiredDriverCount;
    }

    public void decreaseRequiredDriverCount() {
        requiredDriverCount--;
    }

    public void increaseRequiredHours(int count) {
        requiredDriverCount += count;
    }

    public void init(DriverDayStatistics dayStatistics) {
        this.dayStatistics = dayStatistics;
    }
}
