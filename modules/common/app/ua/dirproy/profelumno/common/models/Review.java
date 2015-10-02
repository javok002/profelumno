package ua.dirproy.profelumno.common.models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

/**
 * Created by Alvaro Gaita on 29/09/2015.
 * Universidad Austral.
 * Facultad Ingenieria 2015.
 */

@Entity
public class Review extends Model{

    @Id
    private Long id;

    private String comment;
    private Long stars;
    private Date date;

    public static Model.Finder<Long, Review> finder = new Model.Finder<>(Review.class);

    public Review(){}

    public static List<Review> list() {
        return finder.all();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getStars() {
        return stars;
    }

    public void setStars(Long stars) {
        this.stars = stars;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
