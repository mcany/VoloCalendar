package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 20/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HourStatistics  implements Serializable {
    private int requiredDriverCount;
    private boolean selected;

    public HourStatistics() {
        selected = false;
    }

    public HourStatistics(int requiredDriverCount) {
        this();
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
}
