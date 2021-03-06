package ua.dirproy.profelumno.common.controllers;

import com.avaje.ebean.Ebean;
import play.mvc.Controller;
import play.mvc.Result;

import play.twirl.api.Html;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.common.views.html.sidebar;
import ua.dirproy.profelumno.common.views.html.topbar;
import ua.dirproy.profelumno.common.views.html.teacherSidebarContent;
import ua.dirproy.profelumno.common.views.html.studentSidebarContent;
import ua.dirproy.profelumno.user.models.User;

/**
 * Created by Nicolás Burroni
 * Date: 28/09/15
 */
public class Common extends Controller {

    public static Result sidebar(){
        return ok(sidebar.render());
    }

    public static Result topbar(){
        return ok(topbar.render());
    }

    public static Result userImage() {
        byte[] picture = Ebean.find(User.class, Long.parseLong(session("id"))).getProfilePicture();
        return picture != null ? ok(picture) : badRequest();
    }

    public static String editProfileUrl() {
        return userIsStudent() ? "/modify-student/edit" : "/modify-teacher";
    }

    public static String dashboardUrl() {
        return userIsStudent() ? "/student-profile/student-dashboard" : "/teacher-profile";
    }

    public static String calendarUrl() {
        return "/calendar";
    }

    public static Html sidebarContent() {
        return userIsTeacher() ? teacherSidebarContent.render() : studentSidebarContent.render();
    }

    public static boolean userIsTeacher() {
        return Teacher.finder.where().eq("user", Ebean.find(User.class, Long.parseLong(session("id")))).findUnique() != null;
    }

    public static boolean userIsStudent() {
        return Student.finder.where().eq("user", Ebean.find(User.class, Long.parseLong(session("id")))).findUnique() != null;
    }

    public static String getUsername(){
        return Ebean.find(User.class, Long.parseLong(session("id"))).getName();
    }


    // get the ws.js script
    public static Result wsJs() {
        return ok(ua.dirproy.profelumno.common.views.js.ws.render());
    }
}
