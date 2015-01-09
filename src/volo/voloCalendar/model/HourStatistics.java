package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 20/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HourStatistics implements Serializable {
    protected int requiredDriverCount;
    protected int index;

    public HourStatistics() {
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
