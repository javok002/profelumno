package ua.dirproy.profelumno.subscription.controllers;

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
public class TeacherSubscription extends Controller{

    public static Result validateForm(){
        //final long userId = Long.parseLong(session("id"));
        final long userId = 1;
        Teacher teacher = Teacher.finder.byId(userId);

        CreditCardValidator validator = new CreditCardValidator(CreditCardValidator.AMEX);
        Card card = setCreditCard(teacher);

        //If the teacher doesn't has a card, start the trial
        if(!teacher.hasCard()) setSubscriptionEndDate(teacher);

        teacher.save();

        //validate teacher's credit card
        if(validator.isValid(card.getNumber())) return badRequest();
        else return ok();
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
    }
    public static Card setCreditCard(Teacher teacher) {
        Card card = Form.form(Card.class).bindFromRequest().get();
        teacher.setSubscription(card.getNumber());
        return card;
    }
}
