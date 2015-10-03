package ua.dirproy.profelumno.teacherprofile.controllers;

import ua.dirproy.profelumno.common.models.Lesson;

import java.util.Comparator;

/**
 * Created by fede on 27/05/2015.
 */
public class LessonComparator implements Comparator<Lesson> {

    @Override
    public int compare(Lesson o1, Lesson o2) {
        if (o1.getStudentReview().getStars() < o2.getStudentReview().getStars()){
            return 1;
        }else if (o1.getStudentReview().getStars() > o2.getStudentReview().getStars()){
            return -1;
        }
        else
            return 0;
    }
}
