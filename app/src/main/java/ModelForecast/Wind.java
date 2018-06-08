package ModelForecast;

/**
 * Created by andre on 8.6.2018.
 */

public class Wind {
    //  Variables
    private double speed;
    private double deg;

    //  Constructor
    public Wind(double speed, double deg) {
        this.speed = speed;
        this.deg = deg;
    }

    //  Getters & Setters
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }
}
