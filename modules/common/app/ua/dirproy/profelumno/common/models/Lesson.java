package ua.dirproy.profelumno.common.models;

import com.avaje.ebean.Model;
import ua.dirproy.profelumno.user.models.Subject;
import ua.dirproy.profelumno.user.models.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.sql.Time;
import java.time.Duration;
import java.util.Date;
import java.util.List;

/**
 * Created by Paca on 9/12/15.
 */
@Entity
public class Lesson extends Model {
    @Id private Long id;
    private String dateString;
    private Date dateTime;
    private Duration duration;
    private String address;
    private String comment;
    private Float price;

    @ManyToOne
    private Subject subject;
    private int lessonState = 0;//0 pendiente, 1 aceptado, 2 rechazada

    @ManyToOne
    private Teacher teacher;
    @OneToOne
    private Review teacherReview;

    @ManyToOne
    private Student student;
    @OneToOne
    private Review studentReview;

    public static Finder<Long, Lesson> finder = new Finder<>(Lesson.class);

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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public static List<Lesson> list() { return finder.all(); }

    public Review getTeacherReview() {
        return teacherReview;
    }

    public void setTeacherReview(Review teacherReview) {
        this.teacherReview = teacherReview;
    }

    public Review getStudentReview() {
        return studentReview;
    }

    public int getLessonState() {
        return lessonState;
    }

    public void setLessonState(Integer lessonState) {
        this.lessonState = lessonState;
    }
    public void setStudentReview(Review studentReview) {
        this.studentReview = studentReview;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }


    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
}
