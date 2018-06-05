package com.lonchi.andrej.weatherapp;

import android.*;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lonchi.andrej.weatherapp.Common.Common;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;

import Helper.Helper;
import Model.OpenWeatherMap;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final String TAG = "Location";

    TextView tvCity, tvLastUpdate, tvTemp, tvTempMin, tvTempMax, tvHumidity, tvTime, tvDescription;
    ImageView ivIcon;

    LocationManager locationManager, mLocationManager;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 20 * 1000;  /* 2 secs */
    private long FASTEST_INTERVAL = 10 * 1000; /* 1   sec */
    String provider;
    String iconName;
    static double lat, lng;
    OpenWeatherMap openWeatherMap = new OpenWeatherMap();

    int MY_PERMISSION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate");

        //  Get UI elements
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvLastUpdate = (TextView) findViewById(R.id.tv_lastupdate);
        tvHumidity = (TextView) findViewById(R.id.tv_humidity);
        tvTemp = (TextView) findViewById(R.id.tv_temp);
        tvTempMin = (TextView) findViewById(R.id.tv_temp_min);
        tvTempMax = (TextView) findViewById(R.id.tv_temp_max);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);

        //  Setup API client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //  Check if is GPS and Internet available
        //checkLocation();
        //checkInternet();
        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        //  Setup an API client
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
        //  Disconnect API client
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocationChanged");

        lat = location.getLatitude();
        lng = location.getLongitude();


        Toast.makeText(this, "LAT: " + lat + " ,LNG: " + lng, Toast.LENGTH_SHORT).show();
        tvCity.setText(Double.toString(lat) + "\n" + Double.toString(lng));


        new GetWeather().execute(Common.apiRequest(String.valueOf(lat), String.valueOf(lng)));
        Toast.makeText(this, "Icon: " + iconName, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "onConnected");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(TAG, "onConnected - Permissions");
            return;
        }

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        startLocationUpdates();

        if (mLocation == null) {
            Log.e(TAG, "onConnected -> mLocation == null -> ");
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    protected void startLocationUpdates() {
        Log.e(TAG, "Start Location Updates");
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
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
            tvTempMin.setText( String.format( "Temp Min: %.2f °C", openWeatherMap.getMain().getTemp_min() ) );
            tvTempMax.setText( String.format( "Temp Max: %.2f °C", openWeatherMap.getMain().getTemp_max() ) );
            /*Picasso.with(MainActivity.this)
                    .load(Common.getImage(openWeatherMap.getWeather().get(0).getIcon()))
                    .into(ivIcon);
            */
            iconName = openWeatherMap.getWeather().get(0).getIcon();
            if( !iconName.equals("02d") ){
                ivIcon.setImageResource(R.drawable.ic_cloud);
            }else {
                ivIcon.setImageResource(R.drawable.ic_sunny);
            }
        }
    }
}
