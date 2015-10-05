package ua.dirproy.profelumno.subscription.controllers;

import authenticate.Authenticate;
import org.apache.commons.validator.CreditCardValidator;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.subscription.models.Card;
import ua.dirproy.profelumno.subscription.views.html.subscription;

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

    public static Result validateForm(){
        final long userId = Long.parseLong(session("id"));
        //final long userId = 1;
        Teacher teacher = Teacher.finder.byId(userId);

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

    public static void setSubscriptionEndDate(Teacher teacher){

        //set the teacher's trial to 1 month if the teacher is registering
        Date dateNow = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateNow);
        cal.add(Calendar.MONTH, 1);
        Date date = cal.getTime();
        teacher.setRenewalDate(date);

        ChargeTask charger = new ChargeTask();
        charger.charge();
    }

    public static Result getCreditCardNumber(){
        //final long userID=Long.parseLong(session().get("id"));
        final long userID = 1;
        Teacher teacher= Teacher.finder.byId(userID);
        teacher.setHasCard(true);
        if (teacher.hasCard()) {
            String number = teacher.getSubscription();
            int len = number.length();
            String hiddenNumber = "XXXXXXXXXXXX";
            hiddenNumber = hiddenNumber + number.substring(len - 4, len);
            return ok("Tu numero de tarjeta actual es: "+hiddenNumber);
        }else return badRequest();
    }
}
