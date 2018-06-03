package com.lonchi.andrej.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lonchi.andrej.weatherapp.Common.Common;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;

import Helper.Helper;
import Model.OpenWeatherMap;

public class MainActivity extends AppCompatActivity implements LocationListener {

    TextView tvCity, tvLastUpdate, tvTemp, tvHumidity, tvTime, tvDescription;
    ImageView ivIcon;

    LocationManager locationManager;
    String provider;
    static double lat, lng;
    OpenWeatherMap openWeatherMap = new OpenWeatherMap();

    int MY_PERMISSION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //  Get UI elements
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvLastUpdate = (TextView) findViewById(R.id.tv_lastupdate);
        tvHumidity = (TextView) findViewById(R.id.tv_humidity);
        tvTemp = (TextView) findViewById(R.id.tv_temp);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);


        //  Get Coordinates
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,

            }, MY_PERMISSION);
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null) {
            Log.e("TAG", "No Location");
            Toast.makeText(this, "No Location", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  Remove location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,

            }, MY_PERMISSION);
        }
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  Get location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,

            }, MY_PERMISSION);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

        new GetWeather().execute(Common.apiRequest(String.valueOf(lat), String.valueOf(lng)));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @SuppressLint("StaticFieldLeak")
    private class GetWeather extends AsyncTask<String,Void,String>{
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String stream;
            String urlString = strings[0];

            Helper http = new Helper();
            stream = http.getHTTTPData( urlString );
            return stream;
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.contains("Error: Not found city!")){
                progressDialog.dismiss();
                return;
            }
            Gson gson = new Gson();
            Type mType = new TypeToken<OpenWeatherMap>(){}.getType();
            openWeatherMap = gson.fromJson(s, mType);
            progressDialog.dismiss();

            //  Set UI elements
            tvCity.setText( String.format( "%s, %s", openWeatherMap.getName(), openWeatherMap.getSys().getCountry() ) );
            tvLastUpdate.setText( String.format( "Last Updated: %s", Common.getDateNow() ) );
            tvDescription.setText( String.format( "%s", openWeatherMap.getWeather().get(0).getDescription() ) );
            tvHumidity.setText( String.format( "Humidity: %d%%", openWeatherMap.getMain().getHumidity() ) );
            tvTime.setText( String.format( "Time: %s/%s", Common.unixTimeStampToDateTime( openWeatherMap.getSys().getSunrise() ) , Common.unixTimeStampToDateTime( openWeatherMap.getSys().getSunset() ) ) );
            tvTemp.setText( String.format( "Temp: %.2f °C", openWeatherMap.getMain().getTemp() ) );
            Picasso.with(MainActivity.this)
                    .load(Common.getImage(openWeatherMap.getWeather().get(0).getIcon()))
                    .into(ivIcon);
        }
    }
}
