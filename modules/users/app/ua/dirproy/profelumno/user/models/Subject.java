package ua.dirproy.profelumno.user.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas Moreno on 21/09/2015
 */
//Serializer y Deserializer evitan un loop infinito al pasar el objeto a json, por la lista de users
@JsonDeserialize(using=SubjectDeserializer.class)
@JsonSerialize(using=SubjectSerializer.class)
@Entity
public class Subject extends Model{

    @Id
    private Long id;
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> users;

    public static Finder<Long, Subject> finder = new Finder<>(Subject.class);

    public Subject() {
        users = new ArrayList<>();
    }
    public Subject(String name){
        this.name = name;
    }
    public Subject(String name,Long id){
        this.name=name;
        this.id=id;
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

    public static Subject getStudent(Long id) { return finder.byId(id);}
}
