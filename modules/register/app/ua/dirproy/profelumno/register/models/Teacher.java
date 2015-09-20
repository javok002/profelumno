package ua.dirproy.profelumno.register.models;

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

    @Lob
    private byte[] profilePicture;

    private String description;

    private String homeClasses;

    public Teacher(){}

    public Teacher(long id, String description, String homeClasses, User user){
        this.id=id;
        this.description=description;
        this.homeClasses=homeClasses;
        this.user=user;
    }

    public static Finder<Long, Teacher> finder = new Finder<>(Teacher.class);

    public static List<Teacher> list() { return finder.all(); }

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

    public String getHomeClasses() {
        return homeClasses;
    }

    public void setHomeClasses(String homeClasses) {
        this.homeClasses = homeClasses;
    }
}
