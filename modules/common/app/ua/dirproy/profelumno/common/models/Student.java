package ua.dirproy.profelumno.common.models;

import com.avaje.ebean.Model;
import org.omg.CORBA.PRIVATE_MEMBER;
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


    public static Finder<Long, Student> finder = new Finder<>(Student.class);

    public Student() {
    }

    public Student(Long id, User user) {
        this.id = id;
        this.user = user;
    }

    public static Student getStudent(Long id) { return finder.byId(id);}

    public static List<Student> list() { return finder.all(); }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }
}
