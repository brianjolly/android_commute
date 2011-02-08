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
import android.util.Log;

public class Commute extends Activity
{
  private TextView locationText;
  private Float maxSpeed;
  private Float bestAccuracy;

  /** Called when the activity is first created. */
  @Override
    public void onCreate(Bundle savedInstanceState)
    {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      Log.i("Commute", "Heading home!");

      maxSpeed = 0.0f;
      bestAccuracy = 999999999999.0f;

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

      // Register the listener with the Location Manager to receive location updates
      //commented out for mock 
      //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


      //
      //
      // Mock Location
      //
      //

      //LocationManager locman = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
      LocationManager locman = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

      String testProviderName = LocationManager.GPS_PROVIDER;
      locman.addTestProvider(
          testProviderName,
          "requiresNetwork" == "",
          "requiresSatellite" == "",
          "requiresCell" == "",
          "hasMonetaryCost" == "",
          "supportsAltitude" == "",
          "supportsSpeed" == "",
          "supportsBearing" == "",
          android.location.Criteria.POWER_LOW,
          android.location.Criteria.ACCURACY_FINE);

      locman.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

      Location location = setTestLocation(40.738412973944534,-73.98468017578125); 
      locman.setTestProviderLocation(LocationManager.GPS_PROVIDER, location); 

    }

  private Location setTestLocation(double lat, double lng) { 
    Location location = new Location(LocationManager.GPS_PROVIDER); 
    location.setLatitude(lat); 
    location.setLongitude(lng); 
    location.setSpeed(32f);
    location.setAccuracy(3f);
    location.setTime(System.currentTimeMillis()); 
    return location;
    //locationManager.setTestProviderLocation("gps", location); 
  } 

  //public void onLocationChanged(Location location) {
  public void makeUseOfNewLocation(Location location) {

    if (location.getSpeed() > maxSpeed) {
      maxSpeed = location.getSpeed();
      Log.i("Commute", "Commute.makeUseOfNewLocation() - new max speed - " + maxSpeed);
    }
    if (location.getAccuracy() < bestAccuracy) {
      bestAccuracy = location.getAccuracy();
      Log.i("Commute", "Commute.makeUseOfNewLocation() - new best accuracy - " + bestAccuracy);
    }
    // at caltrain 4th&king 
    // @37.775930,-122.394997 +/- 12.000000m
    //
    // at palo alto
    // @ 37.443702, -122.165406
    // best accuracy 3.0000m
    // max speed 32.461445 m/s
    //
    //
    Location latestLocation = location;
    String locationString = String.format(
        "@ %f, %f \n\naccuracy +/- %fm \nbest accuracy %fm\n\nspeed: %f m/s\nspeed: %f mph\nmax speed: %f m/s\nmax speed: %f mph",
        location.getLatitude(),
        location.getLongitude(),
        location.getAccuracy(),
        bestAccuracy,
        location.getSpeed(),
        speedInMPH(location.getSpeed()),
        maxSpeed,
        speedInMPH(maxSpeed)
        );
    locationText.setText(locationString);

  }

  public Float speedInMPH( Float speedInMetersPerSec ) {
    //1 meter per second = 2.23693629 miles per hour
    return speedInMetersPerSec * 2.23693629f;
  }

}
