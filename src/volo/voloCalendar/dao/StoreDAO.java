package volo.voloCalendar.dao;

/**
 * Created by Emin Guliyev on 04/08/2014.
 */

import org.springframework.data.repository.CrudRepository;
import volo.voloCalendar.entity.DayStatistics;
import volo.voloCalendar.entity.Store;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public interface StoreDAO extends CrudRepository<Store, String>, Serializable {
    public Store findById(String id);
}
