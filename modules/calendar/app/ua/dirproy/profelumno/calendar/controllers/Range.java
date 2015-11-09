package ua.dirproy.profelumno.calendar.controllers;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by ASUS on 09/11/2015.
 */
@Entity
public class Range {
    @Id
    private Long id;
    private int start;
    private int end;
}
