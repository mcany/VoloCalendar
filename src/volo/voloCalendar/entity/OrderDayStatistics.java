package volo.voloCalendar.entity;

import org.springframework.data.jpa.repository.Modifying;

import javax.persistence.*;
import java.sql.Date;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Emin Guliyev on 04/02/2015.
 */
@Entity
@Table(name = "\"OrderDayStatistics\"")
@NamedQuery(name = "OrderDayStatistics.getWeekForecastingByDateLowerBound",
        query = "SELECT day.weekDayIndex, avg(day.hour0), avg(day.hour1), avg(day.hour2), avg(day.hour3)" +
                ", avg(day.hour4), avg(day.hour5), avg(day.hour6), avg(day.hour7), avg(day.hour8)" +
                ", avg(day.hour9), avg(day.hour10), avg(day.hour11), avg(day.hour12), avg(day.hour13)" +
                ", avg(day.hour14), avg(day.hour15), avg(day.hour16), avg(day.hour17), avg(day.hour18)" +
                ", avg(day.hour19), avg(day.hour20), avg(day.hour21), avg(day.hour22), avg(day.hour23)" +
                " FROM OrderDayStatistics day WHERE  day.date >= ?1 GROUP BY day.weekDayIndex ORDER BY day.weekDayIndex")
@NamedNativeQuery(name = "OrderDayStatistics.getAverageAndStandardDeviationsWeek",
        query = "SELECT day.\"weekDayIndex\", avg(day.hour0), stddev_samp(day.hour0), avg(day.hour1), stddev_samp(day.hour1)" +
                ", avg(day.hour2), stddev_samp(day.hour2), avg(day.hour3), stddev_samp(day.hour3)" +
                ", avg(day.hour4), stddev_samp(day.hour4), avg(day.hour5), stddev_samp(day.hour5)" +
                ", avg(day.hour6), stddev_samp(day.hour6), avg(day.hour7), stddev_samp(day.hour7)" +
                ", avg(day.hour8), stddev_samp(day.hour8), avg(day.hour9), stddev_samp(day.hour9)" +
                ", avg(day.hour10), stddev_samp(day.hour10), avg(day.hour11), stddev_samp(day.hour11)" +
                ", avg(day.hour12), stddev_samp(day.hour12), avg(day.hour13), stddev_samp(day.hour13)" +
                ", avg(day.hour14), stddev_samp(day.hour14), avg(day.hour15), stddev_samp(day.hour15)" +
                ", avg(day.hour16), stddev_samp(day.hour16), avg(day.hour17), stddev_samp(day.hour17)" +
                ", avg(day.hour18), stddev_samp(day.hour18), avg(day.hour19), stddev_samp(day.hour19)" +
                ", avg(day.hour20), stddev_samp(day.hour20), avg(day.hour21), stddev_samp(day.hour21)" +
                ", avg(day.hour22), stddev_samp(day.hour22), avg(day.hour23), stddev_samp(day.hour23)" +
                " FROM \"OrderDayStatistics\" day WHERE  day.date >= ?1 GROUP BY day.\"weekDayIndex\" ORDER BY day.\"weekDayIndex\"")
public class OrderDayStatistics {
    private String id;
    private static Calendar calendar = Calendar.getInstance();

    public OrderDayStatistics() {

    }

    public OrderDayStatistics(java.util.Date date) {
        this.id = UUID.randomUUID().toString();
        setDate(new Date(date.getYear(), date.getMonth(), date.getDate()));
        calendar.setTime(date);
        short dayOfWeek = (short) calendar.get(Calendar.DAY_OF_WEEK);
        setWeekDayIndex(dayOfWeek);
    }

    @Id
    @Column(name = "\"id\"")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private Date date;

    @Basic
    @Column(name = "\"date\"")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    //from 1 to 7
    private short weekDayIndex;

    @Basic
    @Column(name = "\"weekDayIndex\"")
    public short getWeekDayIndex() {
        return weekDayIndex;
    }

    public void setWeekDayIndex(short weekDayIndex) {
        this.weekDayIndex = weekDayIndex;
    }

    private short hour0;

    @Basic
    @Column(name = "\"hour0\"")
    public short getHour0() {
        return hour0;
    }

    public void setHour0(short hour0) {
        this.hour0 = hour0;
    }

    private short hour1;

    @Basic
    @Column(name = "\"hour1\"")
    public short getHour1() {
        return hour1;
    }

    public void setHour1(short hour1) {
        this.hour1 = hour1;
    }

    private short hour2;

    @Basic
    @Column(name = "\"hour2\"")
    public short getHour2() {
        return hour2;
    }

    public void setHour2(short hour2) {
        this.hour2 = hour2;
    }

    private short hour3;

    @Basic
    @Column(name = "\"hour3\"")
    public short getHour3() {
        return hour3;
    }

    public void setHour3(short hour3) {
        this.hour3 = hour3;
    }

    private short hour4;

    @Basic
    @Column(name = "\"hour4\"")
    public short getHour4() {
        return hour4;
    }

    public void setHour4(short hour4) {
        this.hour4 = hour4;
    }

    private short hour5;

    @Basic
    @Column(name = "\"hour5\"")
    public short getHour5() {
        return hour5;
    }

    public void setHour5(short hour5) {
        this.hour5 = hour5;
    }

    private short hour6;

    @Basic
    @Column(name = "\"hour6\"")
    public short getHour6() {
        return hour6;
    }

    public void setHour6(short hour6) {
        this.hour6 = hour6;
    }

    private short hour7;

    @Basic
    @Column(name = "\"hour7\"")
    public short getHour7() {
        return hour7;
    }

    public void setHour7(short hour7) {
        this.hour7 = hour7;
    }

    private short hour8;

    @Basic
    @Column(name = "\"hour8\"")
    public short getHour8() {
        return hour8;
    }

    public void setHour8(short hour8) {
        this.hour8 = hour8;
    }

    private short hour9;

    @Basic
    @Column(name = "\"hour9\"")
    public short getHour9() {
        return hour9;
    }

    public void setHour9(short hour9) {
        this.hour9 = hour9;
    }

    private short hour10;

    @Basic
    @Column(name = "\"hour10\"")
    public short getHour10() {
        return hour10;
    }

    public void setHour10(short hour10) {
        this.hour10 = hour10;
    }

    private short hour11;

    @Basic
    @Column(name = "\"hour11\"")
    public short getHour11() {
        return hour11;
    }

    public void setHour11(short hour11) {
        this.hour11 = hour11;
    }

    private short hour12;

    @Basic
    @Column(name = "\"hour12\"")
    public short getHour12() {
        return hour12;
    }

    public void setHour12(short hour12) {
        this.hour12 = hour12;
    }

    private short hour13;

    @Basic
    @Column(name = "\"hour13\"")
    public short getHour13() {
        return hour13;
    }

    public void setHour13(short hour13) {
        this.hour13 = hour13;
    }

    private short hour14;

    @Basic
    @Column(name = "\"hour14\"")
    public short getHour14() {
        return hour14;
    }

    public void setHour14(short hour14) {
        this.hour14 = hour14;
    }

    private short hour15;

    @Basic
    @Column(name = "\"hour15\"")
    public short getHour15() {
        return hour15;
    }

    public void setHour15(short hour15) {
        this.hour15 = hour15;
    }

    private short hour16;

    @Basic
    @Column(name = "\"hour16\"")
    public short getHour16() {
        return hour16;
    }

    public void setHour16(short hour16) {
        this.hour16 = hour16;
    }

    private short hour17;

    @Basic
    @Column(name = "\"hour17\"")
    public short getHour17() {
        return hour17;
    }

    public void setHour17(short hour17) {
        this.hour17 = hour17;
    }

    private short hour18;

    @Basic
    @Column(name = "\"hour18\"")
    public short getHour18() {
        return hour18;
    }

    public void setHour18(short hour18) {
        this.hour18 = hour18;
    }

    private short hour19;

    @Basic
    @Column(name = "\"hour19\"")
    public short getHour19() {
        return hour19;
    }

    public void setHour19(short hour19) {
        this.hour19 = hour19;
    }

    private short hour20;

    @Basic
    @Column(name = "\"hour20\"")
    public short getHour20() {
        return hour20;
    }

    public void setHour20(short hour20) {
        this.hour20 = hour20;
    }

    private short hour21;

    @Basic
    @Column(name = "\"hour21\"")
    public short getHour21() {
        return hour21;
    }

    public void setHour21(short hour21) {
        this.hour21 = hour21;
    }

    private short hour22;

    @Basic
    @Column(name = "\"hour22\"")
    public short getHour22() {
        return hour22;
    }

    public void setHour22(short hour22) {
        this.hour22 = hour22;
    }

    private short hour23;

    @Basic
    @Column(name = "\"hour23\"")
    public short getHour23() {
        return hour23;
    }

    public void setHour23(short hour23) {
        this.hour23 = hour23;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderDayStatistics that = (OrderDayStatistics) o;

        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) id.hashCode();
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    public void addStatistics(java.util.Date date) {
        int hour = date.getHours();
        if (hour == 0) {
            hour0++;
        }
        if (hour == 1) {
            hour1++;
        }
        if (hour == 2) {
            hour2++;
        }
        if (hour == 3) {
            hour3++;
        }
        if (hour == 4) {
            hour4++;
        }
        if (hour == 5) {
            hour5++;
        }
        if (hour == 6) {
            hour6++;
        }
        if (hour == 7) {
            hour7++;
        }
        if (hour == 8) {
            hour8++;
        }
        if (hour == 9) {
            hour9++;
        }
        if (hour == 10) {
            hour10++;
        }
        if (hour == 11) {
            hour11++;
        }
        if (hour == 12) {
            hour12++;
        }
        if (hour == 13) {
            hour13++;
        }
        if (hour == 14) {
            hour14++;
        }
        if (hour == 15) {
            hour15++;
        }
        if (hour == 16) {
            hour16++;
        }
        if (hour == 17) {
            hour17++;
        }
        if (hour == 18) {
            hour18++;
        }
        if (hour == 19) {
            hour19++;
        }
        if (hour == 20) {
            hour20++;
        }
        if (hour == 21) {
            hour21++;
        }
        if (hour == 22) {
            hour22++;
        }
        if (hour == 23) {
            hour23++;
        }
    }

    public void randomize() {
        Random random = new Random();
        hour0 = (short) random.nextInt(5);
        hour1 = (short) random.nextInt(5);
        hour2 = (short) random.nextInt(5);
        hour3 = (short) random.nextInt(5);
        hour4 = (short) random.nextInt(5);
        hour5 = (short) random.nextInt(5);
        hour6 = (short) random.nextInt(5);
        hour7 = (short) random.nextInt(5);
        hour8 = (short) random.nextInt(5);
        hour9 = (short) random.nextInt(5);
        hour10 = (short) random.nextInt(5);
        hour11 = (short) random.nextInt(5);
        hour12 = (short) random.nextInt(5);
        hour13 = (short) random.nextInt(5);
        hour14 = (short) random.nextInt(5);
        hour15 = (short) random.nextInt(5);
        hour16 = (short) random.nextInt(5);
        hour17 = (short) random.nextInt(5);
        hour18 = (short) random.nextInt(5);
        hour19 = (short) random.nextInt(5);
        hour20 = (short) random.nextInt(5);
        hour21 = (short) random.nextInt(5);
        hour22 = (short) random.nextInt(5);
        hour23 = (short) random.nextInt(5);
    }
}
