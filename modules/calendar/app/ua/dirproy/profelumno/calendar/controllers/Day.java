package ua.dirproy.profelumno.calendar.controllers;

import com.avaje.ebean.Model;
import ua.dirproy.profelumno.common.models.DayEnum;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

/**
 * Created by ASUS on 09/11/2015.
 */
@Entity
public class Day extends Model {
    @Id
    private Long id;
    DayEnum dayEnum;
    List<Range> rangeList;
}
