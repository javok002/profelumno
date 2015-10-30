package ua.dirproy.profelumno.subscription.controllers;

import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.mailsender.models.MailSenderUtil;
import javax.mail.*;


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

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public void charge() {
        final Runnable charger = new Runnable() {
            public void run() {
                //charge teacher
            }
        };
        final ScheduledFuture<?> beeperHandle =
                scheduler.schedule(charger,30, DAYS);
    }
}
