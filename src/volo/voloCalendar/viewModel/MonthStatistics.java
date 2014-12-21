package volo.voloCalendar.viewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 21/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthStatistics implements Serializable{
    private int plannedHours;
    private int doneHours;

    public MonthStatistics(int plannedHours, int doneHours) {
        this.plannedHours = plannedHours;
        this.doneHours = doneHours;
    }

    public int getPlannedHours() {
        return plannedHours;
    }

    public void setPlannedHours(int plannedHours) {
        this.plannedHours = plannedHours;
    }

    public int getDoneHours() {
        return doneHours;
    }

    public void setDoneHours(int doneHours) {
        this.doneHours = doneHours;
    }

    public int getDiffHours() {
        return plannedHours - doneHours;
    }
}
