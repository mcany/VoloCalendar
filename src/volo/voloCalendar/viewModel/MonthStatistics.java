package volo.voloCalendar.viewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Emin Guliyev on 21/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthStatistics implements Serializable{
    private int plannedHours;
    private int doneHours;
    private LocalDate beginDate;

    public MonthStatistics(){}

    public MonthStatistics(int plannedHours, int doneHours, LocalDate beginDate) {
        this.plannedHours = plannedHours;
        this.doneHours = doneHours;
        this.beginDate = beginDate;
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

    public LocalDate getBeginDate() {
        return beginDate;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }
}
