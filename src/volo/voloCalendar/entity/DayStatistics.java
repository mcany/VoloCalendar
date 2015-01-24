package volo.voloCalendar.entity;

import javax.persistence.*;

import java.sql.Date;

/**
 * Created by Emin Guliyev on 24/01/2015.
 */
@Entity
@Table(name = "\"DayStatistics\"")
@NamedQueries(
    @NamedQuery(name = "DayStatistics.getWeekByUserIdAndWeekBeginDate",
            query = "SELECT day FROM DayStatistics day WHERE  day.userId = ?1 AND day.weekBeginDate = ?2 ORDER BY day.date")
)
public class DayStatistics {
    private String id;

    public DayStatistics() {
        
    }

    @Id
    @Column(name = "\"id\"")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String userId;

    @Basic
    @Column(name = "\"userId\"")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private Date weekBeginDate;

    @Basic
    @Column(name = "\"weekBeginDate\"")
    public Date getWeekBeginDate() {
        return weekBeginDate;
    }

    public void setWeekBeginDate(Date weekBeginDate) {
        this.weekBeginDate = weekBeginDate;
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

        DayStatistics that = (DayStatistics) o;

        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (weekBeginDate != null ? !weekBeginDate.equals(that.weekBeginDate) : that.weekBeginDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (weekBeginDate != null ? weekBeginDate.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
