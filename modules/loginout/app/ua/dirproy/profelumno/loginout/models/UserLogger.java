package ua.dirproy.profelumno.loginout.models;

/**
 * Created by facundo on 14/9/15.
 */

import com.avaje.ebean.Model;

import javax.persistence.Entity;


public class UserLogger extends Model {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
