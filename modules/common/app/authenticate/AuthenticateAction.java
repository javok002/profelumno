package authenticate;

import com.avaje.ebean.Ebean;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.user.models.User;
import static play.mvc.Controller.session;

/**
 * Created by francisco on 30/09/15.
 */
public class AuthenticateAction extends Action<Authenticate> {

    @Override
    public F.Promise<Result> call(Http.Context context) throws Throwable {
        final long userId = Long.parseLong(session("id"));
        User user = Ebean.find(User.class, userId);
        Teacher teacher = Teacher.finder.where().eq("user", user).findUnique();
        Student student = Student.finder.where().eq("user", user).findUnique();
        if (teacher != null) {
            if (check(Teacher.class)) {
                return delegate.call(context);
            }
            return F.Promise.pure(redirect("/teacher-profile"));
        } else if (student != null) {
            if (check(Student.class)) {
                return delegate.call(context);
            }
            return F.Promise.pure(redirect("student-profile"));
        }if (check(User.class)){
            return delegate.call(context);
        }
        return F.Promise.pure(redirect("/register"));


    }
    private boolean check(Class user){
        Class[] users=configuration.value();
        for (int i=0;i<users.length;i++){
            if (users[i]==(user)) return true;
        }
        return false;
    }
}

