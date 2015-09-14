package ua.dirproy.profelumno.register.models;

import com.avaje.ebean.Model;
import ua.dirproy.profelumno.user.models.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by javier
 * Date: 9/7/15
 * Project profelumno
 */

@Entity
public class Teacher extends Model {

    @Id private Long id;

    private User user;

    private String subscription;

    private Date renewalDate;

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public Date getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(Date renewalDate) {
        this.renewalDate = renewalDate;
    }
}
