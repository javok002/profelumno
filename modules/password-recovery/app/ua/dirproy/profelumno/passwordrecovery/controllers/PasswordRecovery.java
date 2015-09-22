package ua.dirproy.profelumno.passwordrecovery.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.mailsender.models.MailSenderUtil;
import ua.dirproy.profelumno.passwordrecovery.views.html.passwordRecovery;
import ua.dirproy.profelumno.user.models.User;

import java.util.Date;
import java.util.Random;


/**
 * Created by Alvaro Gaita on 13/09/2015.
 * Universidad Austral.
 * Facultad Ingenieria 2015.
 */
public class PasswordRecovery extends Controller {

    public static Result show() {
        return ok(passwordRecovery.render());
    }

    public static Result validateMail(String email) {
        final User user = User.finder.where().eq("email", email).findUnique();
        final ObjectNode result = Json.newObject();

        if (user != null) {
            result.put("mail", email);
            result.put("question", user.getSecureQuestion());
        } else {
            result.put("mail", email);
            result.put("question", "");
        }

        return ok(result);
    }

    public static Result validatePersonalInfo(String email, String answer) {
        final User user = User.finder.where().eq("email", email).findUnique();
        final boolean validation = user.getSecureAnswer().equals(answer);
        final ObjectNode result = Json.newObject();
        result.put("ok", validation);

        if (validation) {
            final String pw = randomPw(8);
            try {
                MailSenderUtil.send(new String[]{user.getEmail()}, "Password changed!", "New password: " + pw);
                user.setPassword(pw);
                user.save();
            } catch (Exception e) {
                return badRequest();
            }
        }

        return ok(result);
    }

    //Testing method.
    public static Result createMockUser() {
        final User user = new User();
        user.setName("Mock");
        user.setSurname("User");
        user.setEmail("noreply.profelumno@gmail.com");
        user.setPassword("mockuserlab2");
        user.setBirthday(new Date());
        user.setGender("Undefined");
        user.setSecureQuestion("El nombre de tu primer mascota.");
        user.setSecureAnswer("Pepe");
        user.save();

        return redirect(routes.PasswordRecovery.show());
    }

    //Testing method.
    public static Result viewMockUserPassword() {
        final User user = User.finder.where().eq("email", "noreply.profelumno@gmail.com").findUnique();
        return ok(user.getPassword());
    }

    private static String randomPw(int length) {
        final char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*()-+?".toCharArray();
        final StringBuilder sb = new StringBuilder();
        final Random random = new Random();

        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }

        return sb.toString();
    }

}
