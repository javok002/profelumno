package ua.dirproy.profelumno.register.models;

import com.avaje.ebean.Model;
import ua.dirproy.profelumno.user.models.User;

import javax.persistence.Entity;
import javax.persistence.Id;
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

    public static Finder<Long, Student> finder = new Finder<>(Student.class);

    public static List<Student> list() { return finder.all(); }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
