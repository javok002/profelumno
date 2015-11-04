package ua.dirproy.profelumno.common.models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fede on 04/11/2015.
 */
@Entity
public class DayRange extends Model {
    @Id
    private Long id;
    DayEnum dayEnum;
    @OneToMany(cascade = CascadeType.ALL)
    List<Range> rangeList;

    public DayRange() {
        rangeList = new ArrayList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayEnum getDayEnum() {
        return dayEnum;
    }

    public void setDayEnum(DayEnum dayEnum) {
        this.dayEnum = dayEnum;
    }

    public List<Range> getRangeList() {
        return rangeList;
    }

    public void setRangeList(List<Range> rangeList) {
        this.rangeList = rangeList;
    }
}
