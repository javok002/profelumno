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
        Teacher teacher = Teacher.finder.where().eq("user.id", userId).findUnique();
        ObjectNode node = Json.newObject();
        Chat chat;

        if (teacher != null) {
            chat = Chat.finder.where().and(Expr.eq("teacher", teacher),
                    Expr.eq("student.user.id", Long.parseLong(session().get("id")))).findUnique();

            if (chat == null) {
                chat = new Chat();
                final long userId2=Long.parseLong(session("id"));
                User user2 = Ebean.find(User.class, userId2);
                chat.setStudent(Student.finder.where().eq("user", user2).findUnique());
                chat.save();
            }
        } else {
            Student student = Student.finder.where().eq("user.id", userId).findUnique();
            chat = Chat.finder.where().and(Expr.eq("student", student),
                    Expr.eq("teacher.user.id", Long.parseLong(session().get("id")))).findUnique();

            if (chat == null) {
                chat = new Chat();
                final long userId2=Long.parseLong(session("id"));
                User user2 = Ebean.find(User.class, userId2);
                chat.setTeacher(Teacher.finder.where().eq("user", user2).findUnique());
                chat.save();
            }
        }

        node.put("chat", Json.toJson(chat));
        return ok(node);
    }
}
