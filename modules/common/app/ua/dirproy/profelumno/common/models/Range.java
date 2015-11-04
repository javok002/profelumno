package ua.dirproy.profelumno.common.models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Fede on 02/11/2015.
 */
@Entity
public class Range extends Model {

    @Id
    private Long id;

    public Date fromHour;
    public Date toHour;

    public Range(){}

    public Range(Date fromHour,Date toHour){
        this.fromHour = fromHour;
        this.toHour = toHour;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
