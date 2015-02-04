package volo.voloCalendar.dao;

/**
 * Created by Emin Guliyev on 04/08/2014.
 */
import org.springframework.data.repository.CrudRepository;
import volo.voloCalendar.entity.OrderDayStatistics;
import volo.voloCalendar.entity.Store;

import java.io.Serializable;
import java.sql.Date;

public interface OrderDayStatisticsDAO extends CrudRepository<OrderDayStatistics, Long>, Serializable{
    public Object getWeekForecastingByDateLowerBound(Date lowerBoundDate);
    public int deleteOutlierDays(double avg0, double stddev0, double avg1, double stddev1, double avg2, double stddev2
            , double avg3, double stddev3, double avg4, double stddev4, double avg5, double stddev5, double avg6, double stddev6
            , double avg7, double stddev7, double avg8, double stddev8, double avg9, double stddev9, double avg10, double stddev10
            , double avg11, double stddev11, double avg12, double stddev12, double avg13, double stddev13, double avg14
            , double stddev14, double avg15, double stddev15, double avg16, double stddev16, double avg17, double stddev17
            , double avg18, double stddev18, double avg19, double stddev19, double avg20, double stddev20, double avg21
            , double stddev21, double avg22, double stddev22, double avg23, double stddev23, short weekDayIndex);
    public Object getAverageAndStandardDeviationsWeek(Date lowerBoundDate);
}
