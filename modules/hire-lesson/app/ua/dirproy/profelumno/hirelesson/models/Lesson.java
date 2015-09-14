package ua.dirproy.profelumno.hirelesson.models;

import com.avaje.ebean.Model;
import com.avaje.ebean.config.JsonConfig;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Time;
import java.util.Date;

/**
 * Created by Paca on 9/12/15.
 */
@Entity
public class Lesson extends Model {
    @Id private Long id;

    private Date dateTime;
    private Time time;
    //private Teacher teacher;
    //private Student student;
    private String place;
    private String description;
    private Float price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
