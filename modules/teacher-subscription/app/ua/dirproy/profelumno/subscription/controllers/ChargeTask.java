package ua.dirproy.profelumno.subscription.controllers;

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
            public void run() { System.out.println("charge"); }
        };
        final ScheduledFuture<?> beeperHandle =
                scheduler.scheduleAtFixedRate(charger, 0, 30, DAYS);
    }
}
