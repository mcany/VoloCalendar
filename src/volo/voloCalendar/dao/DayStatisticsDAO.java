package volo.voloCalendar.dao;

/**
 * Created by Emin Guliyev on 04/08/2014.
 */
import org.springframework.data.repository.CrudRepository;
import volo.voloCalendar.entity.DayStatistics;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface DayStatisticsDAO extends CrudRepository<DayStatistics, String>, Serializable{
    public DayStatistics getDayByUserIdAndDate(String userId, Date date);
    public List<DayStatistics> getNotEmptyDayByDate(Date date);
    public List<String> getActiveDriverIdsByWeekBeginDate(Date weekBeginDate);
    public List<DayStatistics> getWeekByUserIdAndWeekBeginDate(String userId, Date weekBeginDate);
    public List<DayStatistics> getWeekStatisticsByWeekBeginDate(Date weekBeginDate);
    public Long getWeekDoneHoursByUserIdAndWeekBeginDate(String userId, Collection<Date> weekBeginDate);
    public Long getWeekDoneHoursByWeekBeginDate(Date weekBeginDate);
}
