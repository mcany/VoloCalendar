package volo.voloCalendar.externalModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Emin Guliyev on 30/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Orders implements Serializable {
    private Order[] orders;

    public Orders() {
    }

    public Order[] getOrders() {
        return orders;
    }

    public void setOrders(Order[] orders) {
        this.orders = orders;
    }

    public Date[] convertToSortedDateArray() {
        if (orders == null) {
            return new Date[0];
        }
        ArrayList<Date> result = new ArrayList<Date>();
        for (int i = 0; i < orders.length; i++) {
            Order order = orders[i];
            if (order.getDispatched_at() != null) {
                result.add(order.getDispatched_at());
            }
        }
        Date[] array = result.toArray(new Date[result.size()]);
        Arrays.sort(array);
        return array;
    }
}
