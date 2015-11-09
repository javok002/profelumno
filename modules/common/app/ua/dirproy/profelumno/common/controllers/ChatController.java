package ua.dirproy.profelumno.common.controllers;

import authenticate.Authenticate;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import ua.dirproy.profelumno.common.models.Chat;
import ua.dirproy.profelumno.common.models.ChatManager;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.user.models.User;

@Authenticate({Teacher.class, Student.class})
public class ChatController extends Controller {

    public static WebSocket<JsonNode> wsInterface() {
        final Long id = Long.parseLong(session().get("id"));
        return new WebSocket<JsonNode>() {
            public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out) {
                ChatManager.start(id, in, out);
            }
        };
    }

    public static Result getUserInSession(){
        ObjectNode node = Json.newObject();
        final long userId=Long.parseLong(session("id"));
        User user = Ebean.find(User.class, userId);
        JsonNode json= Json.toJson(user);
        //node.put("user", Long.parseLong(session().get("id")));
        return ok(json);
    }

    public static Result getChat(Long userId) {
        User user = Ebean.find(User.class, userId);
        Teacher teacher = Teacher.finder.where().eq("user", user).findUnique();
        Student student =  Student.finder.where().eq("user", user).findUnique();
        ObjectNode node = Json.newObject();
        Chat chat;

        if (teacher != null) {
            teacher.setUser(user);
            final long userId2=Long.parseLong(session("id"));
            User user2 = Ebean.find(User.class, userId2);
            student =  Student.finder.where().eq("user", user2).findUnique();
            student.setUser(user2);

            chat = Chat.finder.where().and(Expr.eq("teacher", teacher),
                    Expr.eq("student", student)).findUnique();

            if (chat == null) {
                chat = new Chat();
                chat.setStudent(student);
                chat.setTeacher(teacher);
                chat.save();
            }
            chat.setStudent(student);
            chat.setTeacher(teacher);
        } else {
            student.setUser(user);
            final long userId2=Long.parseLong(session("id"));
            User user2 = Ebean.find(User.class, userId2);
            teacher =  Teacher.finder.where().eq("user", user2).findUnique();
            teacher.setUser(user2);

            chat = Chat.finder.where().and(Expr.eq("student", student),
                    Expr.eq("teacher", teacher)).findUnique();

            if (chat == null) {
                chat = new Chat();
                chat.setTeacher(teacher);
                chat.setStudent(student);
                chat.save();
            }
            chat.setTeacher(teacher);
            chat.setStudent(student);
        }
        //Modifique un poco el metodo, hasta aca funciona bien igual que como estaba antes
        JsonNode jsonNode=Json.toJson(chat); //aca salta un error no pude identificar
        node.put("chat", jsonNode);
        return ok(node);
    }
}
