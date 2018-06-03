package com.lonchi.andrej.weatherapp.Common;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andre on 2.6.2018.
 */

public class Common {
    public static String API_KEY = "a8444bdb6a3d66c63173043e098776cf";
    public static String API_LINK = "http://api.openweathermap.org/data/2.5/weather";

    @NonNull
    public static String apiRequest(String lat, String lng ){
        StringBuilder sb = new StringBuilder( API_LINK );
        sb.append(String.format( "?lat=%s&lon=%s&appid=%s", lat, lng, API_KEY ));
        return sb.toString();
    }

    public static String unixTimeStampToDateTime( double unixTimeStamp ){
        DateFormat mDateFormat = new SimpleDateFormat( "HH:mm" );
        Date mDate = new Date();
        mDate.setTime( (long) unixTimeStamp*1000 );
        return mDateFormat.format( mDate );
    }

    public static String getImage( String icon ){
        return String.format( "http://openweathermap.org/img/w/%s.png", icon );
    }

    public static String getDateNow() {
        DateFormat mDateFormat = new SimpleDateFormat( "dd MMMM yyyy HH:mm" );
        Date now = new Date();
        return mDateFormat.format( now );
    }
}

