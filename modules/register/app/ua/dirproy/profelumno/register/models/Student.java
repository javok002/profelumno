package ua.dirproy.profelumno.register.models;

import com.avaje.ebean.Model;
import ua.dirproy.profelumno.user.models.User;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by javier
 * Date: 9/7/15
 * Project profelumno
 */

@Entity
public class Student extends Model {

    @Id private Long id;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
