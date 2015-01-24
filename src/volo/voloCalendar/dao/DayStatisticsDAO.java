package volo.voloCalendar.dao;

/**
 * Created by Emin Guliyev on 04/08/2014.
 */
import org.springframework.data.repository.CrudRepository;
import volo.voloCalendar.entity.DayStatistics;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public interface DayStatisticsDAO extends CrudRepository<DayStatistics, String>, Serializable{
    public DayStatistics findById(String id);
    public List<DayStatistics> getWeekByUserIdAndWeekBeginDate(String userId, Date weekBeginDate);
}
