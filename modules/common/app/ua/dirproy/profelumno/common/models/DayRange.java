package ua.dirproy.profelumno.common.models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Duration;
import java.util.Date;

/**
 * Created by Fede on 04/11/2015.
 */
@Entity
public class DayRange extends Model {
    @Id
    private Long id;
    DayEnum dayEnum;
    public Date fromHour;
    public Date toHour;

    public DayRange() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayEnum getDayEnum() {
        return dayEnum;
    }

    public void setDayEnum(DayEnum dayEnum) {
        this.dayEnum = dayEnum;
    }

    public void setRange(Date fromHour,Date toHour){
        this.fromHour = fromHour;
        this.toHour = toHour;
    }

    public Date getFromHour() {
        return fromHour;
    }

    public void setFromHour(Date fromHour) {
        this.fromHour = fromHour;
    }

    public Date getToHour() {
        return toHour;
    }

    public void setToHour(Date toHour) {
        this.toHour = toHour;
    }

    public Duration getDuration(){
        return Duration.ofSeconds(getToHour().getSeconds() - getFromHour().getSeconds());
    }
}
