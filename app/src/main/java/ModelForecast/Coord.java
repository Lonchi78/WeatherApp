package ModelForecast;

/**
 * Created by andre on 8.6.2018.
 */

public class Coord {
    //  Variables
    private double lat;
    private double lon;

    //  Constructor
    public Coord(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    //  Getters & Setters
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
