package com.brianjolly.commute.model;

import android.os.Bundle;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.location.Criteria;
import java.util.Date;

public class MyLocation {

    private Location mocLocation;
    private Location currentLocation;
    private LocationManager locationManager;
    private String mocLocationProvider;
    private Date date;

    public MyLocation(LocationManager locMan) {

        locationManager = locMan;

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
        mocLocationProvider = locationManager.GPS_PROVIDER;

        // Add a test provider using the mocLocationProvider name
        locationManager.addTestProvider(mocLocationProvider, false, true, false, false, true, true, true,
                Criteria.POWER_LOW, Criteria.ACCURACY_COARSE);

        // Sets a mock enabled value for the provider
        locationManager.setTestProviderEnabled(mocLocationProvider, true);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        date = new Date();
        // Set up a moc location
        mocLocation = new Location(mocLocationProvider);
        mocLocation.setLatitude(32.64480);
        mocLocation.setLongitude(-16.90967);
        mocLocation.setTime(date.getTime());

        // Set a moc location
        locationManager.setTestProviderLocation(mocLocationProvider, mocLocation);

        MyTimer timer = new MyTimer(0,1);
        timer.setTimerChangeListener(new MyTimer.TimerChangeListener() {
            @Override public void onTimerChange(float ticks) {
                setNewMocLocation(ticks);
            }
        });
    }

    private void setNewMocLocation(float ticks) {
        mocLocation.setLatitude(37.00+(ticks/100));
        mocLocation.setLongitude(-122.00+(ticks/100));
        mocLocation.setTime(new Date().getTime());
        //mocLocation.setTime(date.getTime());
        locationManager.setTestProviderLocation(mocLocationProvider, mocLocation);
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
		 // TODO: replace null check with exception
        if (null != locationChangeListener) {
            locationChangeListener.onLocationChange(currentLocation);
        }
    }

    public double getLat() {
		// TODO: Find out why currentLocation aometimes isn't available when getLat() is called
		// TODO: Replace null check with exception
        return (null != currentLocation) ? currentLocation.getLatitude() : 0.0;
    }

    public double getLon() {
		// TODO: Find out why currentLocation aometimes isn't available when getLon() is called
		// TODO: replace null check with exception
        return (null != currentLocation) ? currentLocation.getLongitude() : 0.0;
    }

}
