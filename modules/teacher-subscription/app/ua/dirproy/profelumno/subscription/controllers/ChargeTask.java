package ua.dirproy.profelumno.subscription.controllers;

import com.avaje.ebean.Ebean;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.mailsender.models.MailSenderUtil;
import ua.dirproy.profelumno.user.models.User;

import javax.mail.*;


import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.*;

/**
 * Created with IntelliJ IDEA.
 * <p>
 * User: federuiz
 * Date: 28/9/15
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChargeTask{

    boolean test;

    public ChargeTask(boolean test){
        this.test = test;
    }
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public void charge(long userId) {
        final Runnable charger = new Runnable() {
            public void run() {
                //charge teacher
                User user = Ebean.find(User.class, userId);
                Teacher teacher = Teacher.finder.where().eq("user", user).findUnique();
                if(teacher != null){
                    //set the teacher's trial to 1 month if the teacher is registering
                    Date dateNow = teacher.getRenewalDate();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateNow);
                    cal.add(Calendar.MONTH, 1);

                    Date date = cal.getTime();
                    teacher.setRenewalDate(date);
                    teacher.save();
                }
            }
        };
        if (test){
            final ScheduledFuture<?> beeperHandle =
                    scheduler.scheduleAtFixedRate(charger, 0, 2, MINUTES);
        }else {
            final ScheduledFuture<?> beeperHandle =
                    scheduler.scheduleAtFixedRate(charger, 0, 30, DAYS);

        }
    }
}
