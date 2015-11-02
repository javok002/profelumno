package ua.dirproy.profelumno.common.models;

import play.db.ebean.Model;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Fede on 02/11/2015.
 */
public class Range extends Model {

    public Date fromHour;
    public Date toHour;

    public Range(){}

    public Range(@NotNull Date fromHour,@NotNull Date toHour){
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
}
