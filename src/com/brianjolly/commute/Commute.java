package com.brianjolly.commute;

import android.app.Activity;
import android.os.Bundle;

import android.content.Context;
import android.widget.TextView;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

public class Commute extends Activity
{
	private TextView locationText;
  private LocationManager locationManager;

  /** Called when the activity is first created. */
  @Override
    public void onCreate(Bundle savedInstanceState)
    {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      setUpLocation();
    }

  //public void onLocationChanged(Location location) {
  public void makeUseOfNewLocation(Location location) {
    Location latestLocation = location;
    String locationString = String.format(
        "@ %f, %f +/- %fm",
        location.getLatitude(),
        location.getLongitude(),
        location.getAccuracy());
    locationText.setText(locationString);
  }

  private void setUpLocation() {

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

    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

    // Use network location data:
    //LocationProvider locationProvider = LocationManager.NETWORK_PROVIDER;

    // Or, use GPS location data:
    LocationProvider locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);

    // Register the listener with the Location Manager to receive location updates
    locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
    //Location location = locationManager.getCurrentLocation("gps");
  }

  private void setUpViews() {
    locationText = (TextView)findViewById(R.id.location_text);
  }

}
