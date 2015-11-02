package ua.dirproy.profelumno.chat.controllers;

import authenticate.Authenticate;
import com.avaje.ebean.Expr;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import ua.dirproy.profelumno.chat.models.Chat;
import ua.dirproy.profelumno.chat.models.ChatManager;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;

@Authenticate({Teacher.class, Student.class})
public class ChatController extends Controller {

    public static WebSocket<JsonNode> wsInterface() {
        Long userId = Long.parseLong(session().get("id"));
        WebSocket<JsonNode> socket = ChatManager.userSocket(userId);

        if (socket != null) return socket;

        else return new WebSocket<JsonNode>() {
            public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out) {
                ChatManager.start(Long.parseLong(session().get("id")), in, out, this);
            }
        };
    }

    public static Result getUserInSession(){
        ObjectNode node = Json.newObject();
        node.put("id", Long.parseLong(session().get("id")));
        return ok(node);
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
                chat.setTeacher(teacher);
                chat.setStudent(Student.finder.where().eq("user.id", Long.parseLong(session().get("id"))).findUnique());
                chat.save();
            }
        } else {
            Student student = Student.finder.where().eq("user.id", userId).findUnique();
            chat = Chat.finder.where().and(Expr.eq("student", student),
                    Expr.eq("teacher.user.id", Long.parseLong(session().get("id")))).findUnique();

            if (chat == null) {
                chat = new Chat();
                chat.setTeacher(Teacher.finder.where().eq("user.id", Long.parseLong(session().get("id"))).findUnique());
                chat.setStudent(student);
                chat.save();
            }
        }

        node.put("chat", Json.toJson(chat));
        return ok(node);
    }
}
