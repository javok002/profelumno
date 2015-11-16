package ua.dirproy.profelumno.calendar.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ASUS on 09/11/2015.
 */

public class Day {

    Date day;
    List<Range> rangeList;

    public Day(){
        rangeList = new ArrayList<>();
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public List<Range> getRangeList() {
        return rangeList;
    }

    public void setRangeList(List<Range> rangeList) {
        this.rangeList = rangeList;
    }

    public void addRange(Range range){
        rangeList.add(range);
    }

}
