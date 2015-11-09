package ua.dirproy.profelumno.recommend.controllers;


import play.libs.Akka;
import play.mvc.Controller;
import scala.concurrent.duration.Duration;
import ua.dirproy.profelumno.mailsender.models.MailSenderUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by Nash
 * Date: 10/26/15
 * Project profelumno
 */

public class Recommend extends Controller {


    public static void weMissyou(){
        String[] to = {"nicolas.moreno@ing.austral.edu.ar"};


        /*Akka.system().scheduler().scheduleOnce(
                Duration.create(5000, TimeUnit.MILLISECONDS),
                new Runnable() {
                    public void run() {
                        MailSender.mockSend(to[0],"Prueba");
                },

        );*/

        Akka.system().scheduler().schedule(
                Duration.create(1, TimeUnit.MILLISECONDS), //Initial delay 0 milliseconds
                Duration.create(5, TimeUnit.SECONDS),     //Frequency 30 minutes
                new Runnable() {
                    public void run() {
                        try {
                            MailSenderUtil.send(to, "prueba", "Prueba");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                Akka.system().dispatcher());

    }
}
