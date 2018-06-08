package ModelForecast;

import java.util.*;

/**
 * Created by andre on 8.6.2018.
 */

public class OpenWeatherMapForecast {
    //  Variables
    private int cod;
    private String message;
    private int cnt;
    private java.util.List<List> list;
    private City city;

    //  Empty constructor
    public OpenWeatherMapForecast() {
    }

    //  Full constructor
    public OpenWeatherMapForecast(int cod, String message, int cnt, java.util.List<List> list, City city) {
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
        this.list = list;
        this.city = city;
    }

    //  Getters & Setters
    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public java.util.List<List> getList() {
        return list;
    }

    public void setList(java.util.List<List> list) {
        this.list = list;
    }
}
