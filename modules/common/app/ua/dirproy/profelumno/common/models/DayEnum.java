package ua.dirproy.profelumno.common.models;

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

    public static List<DayEnum> getDayEnums(){
        ArrayList<DayEnum> dayEnums = new ArrayList<>();
        dayEnums.add(MONDAY);
        dayEnums.add(TUESDAY);
        dayEnums.add(WEDNESDAY);
        dayEnums.add(THURSDAY);
        dayEnums.add(FRIDAY);
        dayEnums.add(SATURDAY);
        dayEnums.add(SUNDAY);
        return dayEnums;
    }

}
