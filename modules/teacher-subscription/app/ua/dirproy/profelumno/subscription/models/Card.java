package ua.dirproy.profelumno.subscription.models;

/**
 * Created with IntelliJ IDEA.
 * <p>
 * User: federuiz
 * Date: 14/9/15
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Card {
    private String type;
    private String name;
    private String number;
    private String expirationMonth;
    private String expirationYear;
    private String security;

    public Card(String type, String name, String number, String expirationMonth, String expirationYear, String security) {
        this.type = type;
        this.name = name;
        this.number = number;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
        this.security = security;
    }
    public Card(){

    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }
}
