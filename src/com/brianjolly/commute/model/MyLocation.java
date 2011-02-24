package com.brianjolly.commute.model;

import android.os.Bundle;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.location.Criteria;
import java.util.Date;

public class MyLocation {

    private Location currentLocation;

    public MyLocation(LocationManager locMan) {

        LocationManager locationManager = locMan;

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) { 
                currentLocation = location;
                notifyListener(); 
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };
        
        // Get GPS name from constant
        String mocLocationProvider = locationManager.GPS_PROVIDER;

        // add a test provider using the mocLocationProvider name
        locationManager.addTestProvider(mocLocationProvider, false, true, false, false, true, true, true,
                Criteria.POWER_LOW, Criteria.ACCURACY_COARSE);

        // Sets a mock enabled value for the provider
        locationManager.setTestProviderEnabled(mocLocationProvider, true);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        // set up a moc location
        Location location = new Location(mocLocationProvider);
        location.setLatitude(32.64480);
        location.setLongitude(-16.90967);
        location.setTime(new Date().getTime());

        // Set a moc location
        locationManager.setTestProviderLocation(mocLocationProvider, location);
    }

    // pattern for listener comes from Example 7.6. The Dots Model
    // http://programming-android.labs.oreilly.com/ch07.html
    private LocationChangeListener locationChangeListener;

    public interface LocationChangeListener {
        void onLocationChange(Location location);
    }

    public void setLocationChangeListener(LocationChangeListener l) {
        locationChangeListener = l;
    }

    private void notifyListener() {
        if (null != locationChangeListener) {
            locationChangeListener.onLocationChange(currentLocation);
        }
    }

}
