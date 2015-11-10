package ua.dirproy.profelumno.common.models;

import com.avaje.ebean.Model;
import ua.dirproy.profelumno.user.models.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Alvaro Gaita on 30/10/2015.
 * Universidad Austral.
 * Facultad Ingenieria 2015.
 */
@Entity
public class Chat extends Model{

    @Id
    private Long id;

    @ManyToOne
    private Teacher teacher;

    @ManyToOne
    private Student student;

    @OneToMany
    private List<Message> messages;

    public static Finder<Long, Chat> finder = new Finder<>(Chat.class);

    public Chat(){
        this.messages = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<Message> getMessages(){
        return messages;
    }

    public void setMessages(List<Message> messages){
        this.messages = messages;
    }

    public void startMessages(){
        this.messages = new ArrayList<>();
    }

    public void addMessage(String msg, User user){
        Message temp = new Message();
        temp.setAuthor(user);
        temp.setDate(new Date());
        temp.setMsg(msg);
        temp.setChat(this);
        temp.save();

        messages.add(temp);
        this.save();
    }

}
