package volo.voloCalendar.dao;

/**
 * Created by Emin Guliyev on 04/08/2014.
 */
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import volo.voloCalendar.entity.OrderDayStatistics;
import volo.voloCalendar.entity.Store;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public interface OrderDayStatisticsDAO extends CrudRepository<OrderDayStatistics, String>, Serializable{
    public List<Object[]> getWeekForecastingByDateLowerBound(Date lowerBoundDate);
    @Transactional
    @Modifying
    @Query("DELETE FROM OrderDayStatistics day WHERE  (day.date >= ?51) AND (day.weekDayIndex = ?49)" +
            " AND (((?2 > 0) AND (abs((day.hour0 - ?1)/?2) > ?50)) OR ((?4 > 0) AND (abs((day.hour1 - ?3)/?4) > ?50)) OR ((?6 > 0) AND (abs((day.hour2 - ?5)/?6) > ?50)) OR ((?8 > 0) AND (abs((day.hour3 - ?7)/?8) > ?50))" +
            " OR ((?10 > 0) AND (abs((day.hour4 - ?9)/?10) > ?50)) OR ((?12 > 0) AND (abs((day.hour5 - ?11)/?12) > ?50)) OR ((?14 > 0) AND (abs((day.hour6 - ?13)/?14) > ?50)) OR ((?16 > 0) AND (abs((day.hour7 - ?15)/?16) > ?50))" +
            " OR ((?18 > 0) AND (abs((day.hour8 - ?17)/?18) > ?50)) OR ((?20 > 0) AND (abs((day.hour9 - ?19)/?20) > ?50)) OR ((?22 > 0) AND (abs((day.hour10 - ?21)/?22) > ?50)) OR ((?24 > 0) AND (abs((day.hour11 - ?23)/?24) > ?50))" +
            " OR ((?26 > 0) AND (abs((day.hour12 - ?25)/?26) > ?50)) OR ((?28 > 0) AND (abs((day.hour13 - ?27)/?28) > ?50)) OR ((?30 > 0) AND (abs((day.hour14 - ?29)/?30) > ?50)) OR ((?32 > 0) AND (abs((day.hour15 - ?31)/?32) > ?50))" +
            " OR ((?34 > 0) AND (abs((day.hour16 - ?33)/?34) > ?50)) OR ((?36 > 0) AND (abs((day.hour17 - ?35)/?36) > ?50)) OR ((?38 > 0) AND (abs((day.hour18 - ?37)/?38) > ?50)) OR ((?40 > 0) AND (abs((day.hour19 - ?39)/?40) > ?50))" +
            " OR ((?42 > 0) AND (abs((day.hour20 - ?41)/?42) > ?50)) OR ((?44 > 0) AND (abs((day.hour21 - ?43)/?44) > ?50)) OR ((?46 > 0) AND (abs((day.hour22 - ?45)/?46) > ?50)) OR ((?48 > 0) AND (abs((day.hour23 - ?47)/?48) > ?50)))")
    public int deleteOutlierDays(double avg0, double stddev0, double avg1, double stddev1, double avg2, double stddev2
            , double avg3, double stddev3, double avg4, double stddev4, double avg5, double stddev5, double avg6, double stddev6
            , double avg7, double stddev7, double avg8, double stddev8, double avg9, double stddev9, double avg10, double stddev10
            , double avg11, double stddev11, double avg12, double stddev12, double avg13, double stddev13, double avg14
            , double stddev14, double avg15, double stddev15, double avg16, double stddev16, double avg17, double stddev17
            , double avg18, double stddev18, double avg19, double stddev19, double avg20, double stddev20, double avg21
            , double stddev21, double avg22, double stddev22, double avg23, double stddev23, short weekDayIndex, double sigma, Date lowerBound);
    public List<Object[]> getAverageAndStandardDeviationsWeek(Date lowerBoundDate);
}
