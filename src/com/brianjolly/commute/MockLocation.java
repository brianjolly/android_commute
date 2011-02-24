package com.brianjolly.commute;

import java.util.Timer;
import java.util.TimerTask;
import android.location.Location;
import android.location.LocationManager;

public class MockLocation {

  Timer timer;
  LocationManager locMan;

  public MockLocation(LocationManager mocLocManager) {
    locMan = mocLocManager;

    timer = new Timer();
    timer.schedule(new RemindTask(), 0, //initial delay
        1 * 100); //subsequent rate

    System.out.println(":::::::::: timer: "+timer.toString());
  }

  private Location setTestLocation(double lat, double lng) { 
    Location location = new Location(LocationManager.GPS_PROVIDER); 
    location.setLatitude(lat); 
    location.setLongitude(lng); 
    location.setSpeed(32f);
    location.setAccuracy(3f);
    location.setAltitude(3d);
    location.setTime(System.currentTimeMillis()); 
    return location;
  } 

  class RemindTask extends TimerTask {
    int numWarningBeeps = 15;

    public void run() {
      if (numWarningBeeps > 0) {

        Location location = setTestLocation( (40 + numWarningBeeps), (-73 + numWarningBeeps) ); 
        locMan.setTestProviderLocation(LocationManager.GPS_PROVIDER, location); 
        /*
        String testProviderName = LocationManager.GPS_PROVIDER;
        locMan.addTestProvider(
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
          */

        System.out.println("Beep!");
        numWarningBeeps--;
      } else {
        //numWarningBeeps = 15;
        System.out.println("Time's up!");
        timer.cancel(); //Not necessary because we call System.exit
        System.exit(0); //Stops the AWT thread (and everything else)
      }
    }
  }

  public static void main(String args[]) {
    System.out.println("About to schedule task.");
    System.out.println("Task scheduled.");
  }
}

