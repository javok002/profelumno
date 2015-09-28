package ua.dirproy.profelumno.common.models;

import com.avaje.ebean.Model;
import ua.dirproy.profelumno.user.models.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.List;

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

    @Lob
    private byte[] profilePicture;

    private String description;

    private boolean homeClasses;

    private int ranking;

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

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public void setLessonsDictated(int lessonsDictated) {
        this.lessonsDictated = lessonsDictated;
    }

    public int getLessonsDictated() {
        return lessonsDictated;
    }

    public int getRanking() {
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
}
