package authenticate;

import com.avaje.ebean.Ebean;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.user.models.User;

import java.util.Date;

import static play.mvc.Controller.session;

/**
 * Created by francisco on 30/09/15.
 */
public class AuthenticateAction extends Action<Authenticate> {

    @Override
    public F.Promise<Result> call(Http.Context context) throws Throwable {
        final long userId;
        User user;
        Teacher teacher;
        Student student;
        String path=context.request().path();

        try {
            userId= Long.parseLong(session("id"));
            user = Ebean.find(User.class, userId);
            teacher = Teacher.finder.where().eq("user", user).findUnique();
            student = Student.finder.where().eq("user", user).findUnique();
        }catch (NumberFormatException E){
            teacher=null;
            student=null;
        }
        if (teacher != null) {
            if (check(Teacher.class)) {
                Date date=new Date();
                if(teacher.getRenewalDate() != null && (!(path.equals("/subscription/endTrial"))) && !(path.equals("/subscription/charge"))){
                    if (date.after(teacher.getRenewalDate()) && teacher.isInTrial()){
                        if(path.equals("/subscription/cardNumber")){
                            return delegate.call(context);
                        }
                        return F.Promise.pure(redirect("/subscription/endTrial"));
                    }
                }
                if (!teacher.hasCard() &&(!(path.equals("/subscription")))){
                    if(path.equals("/subscription/cardNumber")||path.equals("/subscription/validate")){
                        return delegate.call(context);
                    }
                    return F.Promise.pure(redirect("/subscription"));
                }
                    return delegate.call(context);
            }
            return F.Promise.pure(redirect("/teacher-profile"));
        } else if (student != null) {
            if (check(Student.class)) {
                return delegate.call(context);
            }
            return F.Promise.pure(redirect("student-profile/student-dashboard"));
        }
        return F.Promise.pure(redirect("/institutional"));


    }
    private boolean check(Class user){
        Class[] users=configuration.value();
        for (int i=0;i<users.length;i++){
            if (users[i]==(user)) return true;
        }
        return false;
    }
}

