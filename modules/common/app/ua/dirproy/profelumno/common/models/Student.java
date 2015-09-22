package ua.dirproy.profelumno.common.models;

import com.avaje.ebean.Model;
import ua.dirproy.profelumno.user.models.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * Created by javier
 * Date: 9/7/15
 * Project profelumno
 */

@Entity
public class Student extends Model {

    @Id private Long id;

    @OneToOne
    private User user;

    @Lob
    private byte[] profilePicture;

    public static Finder<Long, Student> finder = new Finder<>(Student.class);

    public Student() {
    }

    public Student(Long id, User user) {
        this.id = id;
        this.user = user;
    }

    public static List<Student> list() { return finder.all(); }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Long getId() {
        return id;
    }
}
