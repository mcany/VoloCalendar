package volo.voloCalendar.externalModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Created by Emin Guliyev on 05/02/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements Comparable<Order> {
    private Date dispatched_at;

    public Order() {
    }

    public Date getDispatched_at() {
        return dispatched_at;
    }

    public void setDispatched_at(Date dispatched_at) {
        this.dispatched_at = dispatched_at;
    }

    @Override
    public int compareTo(Order obj) {
        if (obj == null || obj.getDispatched_at() == null) {
            return 1;
        }
        if (this.getDispatched_at() == null) {
            return -1;
        }
        return this.getDispatched_at().compareTo(obj.getDispatched_at());
    }
}
