package ua.dirproy.profelumno.subscription.controllers;

import com.avaje.ebean.Ebean;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.mailsender.models.MailSenderUtil;
import ua.dirproy.profelumno.user.models.User;

import javax.mail.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MINUTES;


/**
 * Created by federuiz on 10/26/15.
 */
public class NotifyTask {

    boolean test;

    public NotifyTask(boolean test){
        this.test = test;
    }
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public void notify(long userId) {
        final Runnable charger = new Runnable() {
            public void run() {
                User user = Ebean.find(User.class, userId);
                Teacher teacher = Teacher.finder.where().eq("user", user).findUnique();
                if(teacher != null){
                    String[] to = new String[1];
                    to[0] = teacher.getUser().getEmail();
                    String subject = "ProfeLumno: Vencimiento de periodo de prueba";
                    String message = "<body style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;margin: 0;font-family: &quot;Helvetica Neue&quot;,Helvetica,Arial,sans-serif;font-size: 14px;line-height: 1.428571429;color: #333;background-color: #fff;\">\n" +
                            "<div style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;\">\n" +
                            "    <h1 align=\"center\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;margin: .67em 0;font-size: 36px;font-family: &quot;Helvetica Neue&quot;,Helvetica,Arial,sans-serif;font-weight: 500;line-height: 1.1;color: inherit;margin-top: 20px;margin-bottom: 10px;\"><b style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;font-weight: bold;\">Profe</b><span class=\"thin\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;\">Lumno</span></h1>\n" +
                            "    <div class=\"row\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;margin-right: -15px;margin-left: -15px;\">\n" +
                            "        <div class=\"col-md-4 col-sm-3\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;position: relative;min-height: 1px;padding-right: 15px;padding-left: 15px;float: left;width: 33.33333333333333%;\"></div>\n" +
                            "        <div class=\"col-md-4 col-sm-6\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;position: relative;min-height: 1px;padding-right: 15px;padding-left: 15px;float: left;width: 33.33333333333333%;\">\n" +
                            "            <div class=\"panel panel-primary\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;margin-bottom: 20px;background-color: #fff;border: 1px solid transparent;border-radius: 4px;-webkit-box-shadow: 0 1px 1px rgba(0,0,0,0.05);box-shadow: 0 1px 1px rgba(0,0,0,0.05);border-color: #428bca;\">\n" +
                            "                <div class=\"panel-heading\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 10px 15px;border-bottom: 1px solid transparent;border-top-right-radius: 3px;border-top-left-radius: 3px;color: #fff;background-color: #428bca;border-color: #428bca;\">\n" +
                            "                    <h3 class=\"panel-title\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;orphans: 3;widows: 3;page-break-after: avoid;font-family: &quot;Helvetica Neue&quot;,Helvetica,Arial,sans-serif;font-weight: 500;line-height: 1.1;color: inherit;margin-top: 0;margin-bottom: 0;font-size: 16px;\">Advertencia periodo de prueba</h3>\n" +
                            "                </div>\n" +
                            "                <div class=\"panel-body\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 15px;\">\n" +
                            "                    El tiempo de prueba se acabara en una semana. A partir del "+teacher.getRenewalDate() +" se le empezara a debitar mensualmente de su cuenta el monto de $50.00 por el servicio. Si no desea mantener su servicio, puede cerrar su cuenta.\n" +
                            "                </div>\n" +
                            "                <div class=\"panel-footer\" align=\"right\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 10px 15px;background-color: #f5f5f5;border-top: 1px solid #ddd;border-bottom-right-radius: 3px;border-bottom-left-radius: 3px;\"><b style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;font-weight: bold;\">Profe</b><span class=\"thin\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;\">Lumno </span></div>\n" +
                            "            </div>\n" +
                            "        </div>\n" +
                            "        <div class=\"col-md-4 col-sm-3\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;position: relative;min-height: 1px;padding-right: 15px;padding-left: 15px;float: left;width: 33.33333333333333%;\"></div>\n" +
                            "    </div>\n" +
                            "</div>\n" +
                            "</body>";
                    System.out.println("Sending mail...");
                    try {
                        MailSenderUtil.send(to, subject, message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        System.out.println("Error sending mail");
                    }
                    System.out.println("Mail sent.");
                    ultimatum(userId);

                }
                else{
                    System.out.println();
                }
            }
        };
        if (test){
            final ScheduledFuture<?> beeperHandle =
                    scheduler.schedule(charger,2, MINUTES);

        }else{
            final ScheduledFuture<?> beeperHandle =
                    scheduler.schedule(charger,23, DAYS);

        }

    }
    public void ultimatum(long userId){
        final Runnable charger = new Runnable() {
            public void run() {
                User user = Ebean.find(User.class, userId);
                Teacher teacher = Teacher.finder.where().eq("user", user).findUnique();
                if(teacher != null){
                    String[] to = new String[1];
                    to[0] = teacher.getUser().getEmail();
                    String subject = "ProfeLumno: Periodo de prueba caducado";
                    String message = "<body style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;margin: 0;font-family: &quot;Helvetica Neue&quot;,Helvetica,Arial,sans-serif;font-size: 14px;line-height: 1.428571429;color: #333;background-color: #fff;\">\n" +
                            "<div style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;\">\n" +
                            "    <h1 align=\"center\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;margin: .67em 0;font-size: 36px;font-family: &quot;Helvetica Neue&quot;,Helvetica,Arial,sans-serif;font-weight: 500;line-height: 1.1;color: inherit;margin-top: 20px;margin-bottom: 10px;\"><b style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;font-weight: bold;\">Profe</b><span class=\"thin\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;\">Lumno</span></h1>\n" +
                            "    <div class=\"row\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;margin-right: -15px;margin-left: -15px;\">\n" +
                            "        <div class=\"col-md-4 col-sm-3\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;position: relative;min-height: 1px;padding-right: 15px;padding-left: 15px;float: left;width: 33.33333333333333%;\"></div>\n" +
                            "        <div class=\"col-md-4 col-sm-6\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;position: relative;min-height: 1px;padding-right: 15px;padding-left: 15px;float: left;width: 33.33333333333333%;\">\n" +
                            "            <div class=\"panel panel-primary\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;margin-bottom: 20px;background-color: #fff;border: 1px solid transparent;border-radius: 4px;-webkit-box-shadow: 0 1px 1px rgba(0,0,0,0.05);box-shadow: 0 1px 1px rgba(0,0,0,0.05);border-color: #428bca;\">\n" +
                            "                <div class=\"panel-heading\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 10px 15px;border-bottom: 1px solid transparent;border-top-right-radius: 3px;border-top-left-radius: 3px;color: #fff;background-color: #428bca;border-color: #428bca;\">\n" +
                            "                    <h3 class=\"panel-title\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;orphans: 3;widows: 3;page-break-after: avoid;font-family: &quot;Helvetica Neue&quot;,Helvetica,Arial,sans-serif;font-weight: 500;line-height: 1.1;color: inherit;margin-top: 0;margin-bottom: 0;font-size: 16px;\">Periodo de prueba caducado</h3>\n" +
                            "                </div>\n" +
                            "                <div class=\"panel-body\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 15px;\">\n" +
                            "                    Su periodo de prueba ha terminado. Para poder seguir disfrutando de su servicio, debera <a href=\"http://localhost:9000/subscription/endTrial\">confirmar su metodo de pago.</a>\n" +
                            "                </div>\n" +
                            "                <div class=\"panel-footer\" align=\"right\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 10px 15px;background-color: #f5f5f5;border-top: 1px solid #ddd;border-bottom-right-radius: 3px;border-bottom-left-radius: 3px;\"><b style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;font-weight: bold;\">Profe</b><span class=\"thin\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;\">Lumno </span></div>\n" +
                            "            </div>\n" +
                            "        </div>\n" +
                            "        <div class=\"col-md-4 col-sm-3\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;position: relative;min-height: 1px;padding-right: 15px;padding-left: 15px;float: left;width: 33.33333333333333%;\"></div>\n" +
                            "    </div>\n" +
                            "</div>\n" +
                            "</body>";
                    System.out.println("Sending mail...");
                    try {
                        MailSenderUtil.send(to, subject, message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        System.out.println("Sending mail failed");
                    }
                    System.out.println("Mail sent");
                }
            }
        };
        if (test){
            final ScheduledFuture<?> beeperHandle =
                    scheduler.schedule(charger,2, MINUTES);
        }else {
            final ScheduledFuture<?> beeperHandle =
                    scheduler.schedule(charger,7, DAYS);

        }
    }
}
