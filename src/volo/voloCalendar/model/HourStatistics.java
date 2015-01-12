package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 20/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HourStatistics implements Serializable {
    protected int planningHours;
    protected int index;

    public HourStatistics() {
    }

    public int getPlanningHours() {
        return planningHours;
    }

    public void setPlanningHours(int planningHours) {
        this.planningHours = planningHours;
    }

    public void decreasePlanningHours() {
        planningHours--;
    }

    public void increaseRequiredHours(int count) {
        planningHours += count;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void increasePlanningHours() {
        planningHours++;
    }
}
