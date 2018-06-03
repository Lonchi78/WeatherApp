package Model;

/**
 * Created by andre on 3.6.2018.
 */

public class Sys {
    //  Variables
    private String country;
    private double message;
    private double sunrise;
    private double sunset;

    //  Constructor
    public Sys(String country, double message, double sunrise, double sunset) {
        this.country = country;
        this.message = message;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    //  Getters & Setters
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public double getSunrise() {
        return sunrise;
    }

    public void setSunrise(double sunrise) {
        this.sunrise = sunrise;
    }

    public double getSunset() {
        return sunset;
    }

    public void setSunset(double sunset) {
        this.sunset = sunset;
    }
}
