package ua.dirproy.profelumno.mailsender.controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created with IntelliJ IDEA.
 * <p>
 * User: federuiz
 * Date: 11/9/15
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class MailSender extends Controller{
    public static Result send(){
        return ok();
    }
}
