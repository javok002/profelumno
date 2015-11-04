package ua.dirproy.profelumno.common.models;

import com.avaje.ebean.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fede on 02/11/2015.
 */
public enum DayEnum {

    SUNDAY("SUNDAY"), MONDAY("MONDAY"), TUESDAY("TUESDAY"), WEDNESDAY("WEDNESDAY"),
    THURSDAY("THURSDAY"), FRIDAY("FRIDAY"), SATURDAY("SATURDAY");

    String dayName;

    DayEnum(String dayName) {
        this.dayName = dayName;
    }

    public String getDayName() {
        return dayName;
    }

    public static List<DayEnum> listDays(){
        ArrayList<DayEnum> list = new ArrayList();
        list.add(SUNDAY);
        list.add(MONDAY);
        list.add(TUESDAY);
        list.add(WEDNESDAY);
        list.add(THURSDAY);
        list.add(FRIDAY);
        list.add(SATURDAY);
        return list;
    }
}
