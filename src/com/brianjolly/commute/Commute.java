package com.brianjolly.commute;

import android.app.Activity;
import android.os.Bundle;

import android.content.Context;
import android.widget.TextView;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

import java.lang.reflect.Method;

public class Commute extends Activity
{
  private TextView locationText;
  private Float maxSpeed;

  /** Called when the activity is first created. */
  @Override
    public void onCreate(Bundle savedInstanceState)
    {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      maxSpeed = 0.0f;

      locationText = (TextView)findViewById(R.id.location_text);

      // Define a listener that responds to location updates
      LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
          // Called when a new location is found by the network location provider.
          makeUseOfNewLocation(location);
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
      };

      //locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
      LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

      // Use network location data:
      //LocationProvider locationProvider = LocationManager.NETWORK_PROVIDER;
      // Or, use GPS location data:
      //LocationProvider locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);

      // Register the listener with the Location Manager to receive location updates
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
      //String lm = locationManager.toString();
      
      /*
      Class lm2 = LocationManager.class;
      Method[] methods = lm2.getDeclaredMethods();
      String methout = "";
      for (Method method : methods) {
        methout += method.getName() + "\n";
      }
      */
        

      //locationText.setText(methout);

      //Location location = locationManager.getCurrentLocation("gps");
    }

  //public void onLocationChanged(Location location) {
  public void makeUseOfNewLocation(Location location) {
    if (location.getSpeed() > maxSpeed) {
      maxSpeed = location.getSpeed();
    }
    // at caltrain 4th&king 
    // @37.775930,-122.394997 +/- 12.000000m
    Location latestLocation = location;
    String locationString = String.format(
        "@ %f, %f \naccuracy +/- %fm \nspeed: %f m/s\nmax speed: %f m/s",
        location.getLatitude(),
        location.getLongitude(),
        location.getAccuracy(),
        location.getSpeed(),
        maxSpeed
        );
    locationText.setText(locationString);
  }

}
