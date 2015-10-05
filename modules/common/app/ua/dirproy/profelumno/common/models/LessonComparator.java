package ua.dirproy.profelumno.common.models;

import java.util.Comparator;

/**
 * Created by juan
 * on 03/10/15.
 */
public class LessonComparator implements Comparator<Lesson>{

    @Override
    public int compare(Lesson o1, Lesson o2) {
        return o1.getDateTime().compareTo(o2.getDateTime());
    }
}
