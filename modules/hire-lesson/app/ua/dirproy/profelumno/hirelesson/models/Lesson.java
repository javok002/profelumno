package ua.dirproy.profelumno.hirelesson.models;

import com.avaje.ebean.Model;
import com.avaje.ebean.config.JsonConfig;
import ua.dirproy.profelumno.register.models.Student;
import ua.dirproy.profelumno.register.models.Teacher;

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
    private Time duration;
    private Teacher teacher;
    private Student student;
    private String place;
    private String description;
    private Float price;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

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

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
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
