package ua.dirproy.profelumno.subscription.controllers;

import org.apache.commons.validator.routines.CreditCardValidator;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.subscription.models.Card;
import ua.dirproy.profelumno.subscription.views.html.subscription;
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
        CreditCardValidator validator = new CreditCardValidator(CreditCardValidator.NONE);
        Card card = Form.form(Card.class).bindFromRequest().get();
        if(validator.isValid(card.getNumber())) return ok();
        else return ok();
    }

    public static Result subscriptionForm(){ return ok(subscription.render());}
}
