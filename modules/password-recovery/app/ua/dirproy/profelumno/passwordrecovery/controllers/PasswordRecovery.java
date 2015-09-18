package ua.dirproy.profelumno.passwordrecovery.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.user.models.User;


/**
 * Created by Alvaro Gaita on 13/09/2015.
 * Universidad Austral.
 * Facultad Ingenieria 2015.
 */
public class PasswordRecovery extends Controller {

    public static Result validateMail(String mail){
        final User user = User.finder.where().eq("email", mail).findUnique();
        final ObjectNode result = Json.newObject();

        if (user != null) {
            result.put("question", user.getSecureQuestion());
        } else {
            result.put("question", "");
        }

        return ok(result);
    }

    public static Result validatePersonalInfo(String mail, String answer){
        final User user = User.finder.where().eq("email", mail).findUnique();
        final boolean validation = user.getSecureAnswer().equals(answer);
        final ObjectNode result = Json.newObject();
        result.put("ok", validation);

        return ok(result);
    }

}
