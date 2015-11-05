package ua.dirproy.profelumno.common.models;

import com.avaje.ebean.Model;
import ua.dirproy.profelumno.user.models.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by Alvaro Gaita on 30/10/2015.
 * Universidad Austral.
 * Facultad Ingenieria 2015.
 */
@Entity
public class Message extends Model {

    @Id
    private Long id;

    @ManyToOne
    private User author;

    private String msg;
    private Date date;

    public static Finder<Long, Message> finder = new Finder<>(Message.class);

    public Message() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
