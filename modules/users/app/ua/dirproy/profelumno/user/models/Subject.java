package ua.dirproy.profelumno.user.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.avaje.ebean.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas Moreno on 21/09/2015
 */
@Entity
public class Subject extends Model{

    @Id
    private Long id;
    private String name;

    @ManyToMany
    private List<User> users;

    public Subject() {
        users = new ArrayList<>();
    }
    public Subject(String name){
        this.name = name;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
