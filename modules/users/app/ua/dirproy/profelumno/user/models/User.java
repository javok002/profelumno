package ua.dirproy.profelumno.user.models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * Created by javier
 * Date: 9/7/15
 * Project profelumno
 */

@Entity
public class User extends Model {

    @Id private Long id;

    private String name;
    private String surname;
    private String email;
    private String password;
    private Date birthday;
    private String gender;
    private String secureQuestion;
    private String secureAnswer;

    public static Finder<Long, User> finder = new Finder<>(User.class);

    public static List<User> list() { return finder.all(); }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }

    public void setEmail(@NotNull String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(@NotNull String password) { this.password = password; }

    public static User validateEmail(String email, String password){
        return finder.where().eq("email", email).eq("password", password).findUnique();
    }

    public static boolean validateEmailUnique(String email){
        return finder.where().eq("email", email).findUnique() == null ;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(@NotNull String surname) {
        this.surname = surname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(@NotNull Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(@NotNull String gender) {
        this.gender = gender;
    }

    public String getSecureQuestion() {
        return secureQuestion;
    }

    public void setSecureQuestion(@NotNull String secureQuestion) {
        this.secureQuestion = secureQuestion;
    }

    public String getSecureAnswer() {
        return secureAnswer;
    }

    public void setSecureAnswer(@NotNull String secureAnswer) {
        this.secureAnswer = secureAnswer;
    }
}
