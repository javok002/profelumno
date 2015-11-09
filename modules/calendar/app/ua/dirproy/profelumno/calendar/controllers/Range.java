package ua.dirproy.profelumno.calendar.controllers;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by ASUS on 09/11/2015.
 */
@Entity
public class Range extends Model {

    @Id
    private Long id;
    private int from;
    private int to;

    public Range(){

    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
}
