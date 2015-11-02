package ua.dirproy.profelumno.subscription.controllers;

import authenticate.Authenticate;
import com.avaje.ebean.Ebean;
import org.apache.commons.validator.CreditCardValidator;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.subscription.models.Card;
import ua.dirproy.profelumno.subscription.views.html.endTrial;
import ua.dirproy.profelumno.subscription.views.html.subscription;
import ua.dirproy.profelumno.subscription.views.html.subscriptionTopBar;
import ua.dirproy.profelumno.user.models.User;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * <p>
 * User: federuiz
 * Date: 11/9/15
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
@Authenticate({Teacher.class})
public class TeacherSubscription extends Controller{

    static boolean test = true;

    public static Result validateForm(){
        final long userId= Long.parseLong(session("id"));
        User user = Ebean.find(User.class, userId);
        Teacher teacher = Teacher.finder.where().eq("user", user).findUnique();
        teacher.save();
        Card card = Form.form(Card.class).bindFromRequest().get();
        CreditCardValidator validator;

        switch (card.getType()){
            case "VISA":
                validator = new CreditCardValidator(CreditCardValidator.VISA);
                break;
            case "AMEX": validator = new CreditCardValidator(CreditCardValidator.AMEX);
                break;
            case "MASTER": validator = new CreditCardValidator(CreditCardValidator.MASTERCARD);
                break;
            case "DISCOVER": validator = new CreditCardValidator(CreditCardValidator.DISCOVER);
                break;
            default: validator = new CreditCardValidator();
        }

        //If the teacher doesn't has a card, start the trial
        if(!teacher.hasCard()){
            setSubscriptionEndDate(teacher);
        }

        teacher.save();

        //validate teacher's credit card
        if(!validator.isValid(card.getNumber())) return badRequest();
        else {
            teacher.setSubscription(card.getNumber());
            teacher.setHasCard(true);
            teacher.save();

            //todo mandar a perfiles
            return ok("/");
        }
    }

    public static Result subscriptionForm(){ return ok(subscription.render());}

    public static Result endTrialForm(){return ok(endTrial.render());}

    public static Result charge(){
        final long userId= Long.parseLong(session("id"));
        User user = Ebean.find(User.class, userId);
        Teacher teacher = Teacher.finder.where().eq("user", user).findUnique();

        teacher.setIsInTrial(false);
        teacher.save();
        ChargeTask charger= new ChargeTask(test);
        charger.charge(userId);
        System.out.println("Teacher trial: " + teacher.isInTrial());
        return ok("/");
    }

    public static void setSubscriptionEndDate(Teacher teacher){
        if(test){
            Date dateNow = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateNow);
            cal.add(Calendar.MINUTE, 4);
            Date date = cal.getTime();
            teacher.setRenewalDate(date);
            NotifyTask notifier = new NotifyTask(test);
            notifier.notify(teacher.getUser().getId());
        }
        else{
            //set the teacher's trial to 1 month if the teacher is registering
            Date dateNow = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateNow);
            cal.add(Calendar.MONTH, 1);
            Date date = cal.getTime();
            teacher.setRenewalDate(date);
            NotifyTask notifier = new NotifyTask(test);
            notifier.notify(teacher.getUser().getId());
        }

    }

    public static Result getCreditCardNumber(){
        final long userId= Long.parseLong(session("id"));
        User user = Ebean.find(User.class, userId);
        Teacher teacher = Teacher.finder.where().eq("user", user).findUnique();
        if (teacher.hasCard()) {
            String number = teacher.getSubscription();
            int len = number.length();
            String hiddenNumber = "XXXXXXXXXXXX";
            hiddenNumber = hiddenNumber + number.substring(len - 4, len);
            return ok("Tu numero de tarjeta actual es: "+hiddenNumber);
        }else return badRequest();
    }

    public static Result subscriptionWTopBar(){
        return ok(subscriptionTopBar.render());
    }
}