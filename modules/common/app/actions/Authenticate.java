package actions;

import play.mvc.With;

import java.lang.annotation.*;

/**
 * Created by francisco on 30/09/15.
 */
@With(AuthenticateAction.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface Authenticate
{
    Class[] value() default {};
}