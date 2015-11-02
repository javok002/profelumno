package ua.dirproy.profelumno.common.models;

import com.avaje.ebean.Model;
import ua.dirproy.profelumno.user.models.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by javier
 * Date: 9/7/15
 * Project profelumno
 */

@Entity
public class Teacher extends Model {

    @Id private Long id;

    @OneToOne
    private User user;

    private String subscription;

    private Date renewalDate;

    private boolean isInTrial;

    private boolean hasCard;

    private String description;

    private boolean homeClasses;

    private float ranking;

    private int lessonsDictated;

    private double price;

    public Teacher(){}

    public Teacher(long id, String description, boolean homeClasses, User user){
        this.id=id;
        this.description=description;
        this.homeClasses=homeClasses;
        this.user=user;
    }

    public static Finder<Long, Teacher> finder = new Finder<>(Teacher.class);

    public static List<Teacher> list() { return finder.all(); }

    public static Teacher getTeacher(Long id) { return finder.byId(id);}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public Date getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(Date renewalDate) {
        this.renewalDate = renewalDate;
    }

    public boolean isInTrial() {
        return isInTrial;
    }

    public void setIsInTrial(boolean isInTrial) {
        this.isInTrial = isInTrial;
    }

    public boolean hasCard() {
        return hasCard;
    }

    public void setHasCard(boolean hasCard) {
        this.hasCard = hasCard;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRanking(float ranking) {
        this.ranking = ranking;
    }

    public void setLessonsDictated(int lessonsDictated) {
        this.lessonsDictated = lessonsDictated;
    }

    public int getLessonsDictated() {
        return lessonsDictated;
    }

    public float getRanking() {
        return ranking;
    }

    public boolean getHomeClasses() {
        return homeClasses;
    }

    public void setHomeClasses(boolean homeClasses) {
        this.homeClasses = homeClasses;
    }

    public boolean isHomeClasses() {
        return homeClasses;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static void updateLessonsDictated(Teacher teacher){
        Date today = new Date();
        int lessonsDictated = 0;
        for (Lesson lesson : Lesson.list()) {
            if(Objects.equals(lesson.getTeacher().getId(), teacher.getId()) && lesson.getLessonState() == 1 && lesson.getDateTime().before(today)){
                lessonsDictated++;
            }
        }
        teacher.setLessonsDictated(lessonsDictated);
        teacher.save();
    }

    public static void updateRating(Teacher teacher){
        float lessonsRated = 0;
        float totalScore = 0;
        for (Lesson lesson : Lesson.list()) {
            if(Objects.equals(lesson.getTeacher().getId(), teacher.getId()) && lesson.getTeacherReview() != null){
                lessonsRated++;
                totalScore+= lesson.getTeacherReview().getStars();
            }
        }
        float temp = lessonsRated == 0 ? 0: (((float)((long) (((totalScore / lessonsRated) * 100) + 0.5))) / 100);
        teacher.setRanking(temp);
        teacher.save();
    }
}
