package ua.dirproy.profelumno.lessonReview.controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by Gustavo on 28/9/15.
 */
public class ReviewController extends Controller {

    public static Result show(){
        return ok(ua.dirproy.profelumno.lessonReview.views.html.reviews.render());

    }

    public static Result getReviewedLessons(){
        return ok();
        //solo calificadas
    }

    public static Result getNonReviewedLessons(){

        return ok();
        //No calificadas
    }

    public static Result review(String comment, int stars, String from, String to, int idLesson){
        return ok();
    }
}
