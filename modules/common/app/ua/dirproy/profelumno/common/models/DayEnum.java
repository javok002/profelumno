package ua.dirproy.profelumno.common.models;

import com.avaje.ebean.Model;

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
}
