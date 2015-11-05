package ua.dirproy.profelumno.loginout.models;

import com.avaje.ebean.Model;

/**
 * Created by facundo on 3/11/15.
 */
public class FacebookLogger extends Model {
    private String fb_email;
    private String fb_name;
    private String fb_surname;




    public String getFb_email() {
            return fb_email;
        }

        public void setFb_email(String fb_email) {
            this.fb_email = fb_email;
        }

        public String getFb_name() {
            return fb_name;
        }

        public void setFb_name(String fb_name) {
            this.fb_name = fb_name;
        }

        public String getFb_surname() {
            return fb_surname;
        }

        public void setFb_surname(String fb_surname) {
            this.fb_surname = fb_surname;
        }
}
