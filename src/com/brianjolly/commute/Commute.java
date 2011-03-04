package com.brianjolly.commute;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import com.brianjolly.commute.model.MyLocation;
import com.brianjolly.commute.view.BreadCrumbTrail;

public class Commute extends Activity
{
private TextView locationText;
private Float maxSpeed;
private Float bestAccuracy;
private BreadCrumbTrail breadCrumbTrail;

/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState)
{
  super.onCreate(savedInstanceState);

  setContentView(R.layout.main);

  maxSpeed = 0.0f;
  bestAccuracy = 999999999999.0f;

  LocationManager locman = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

  MyLocation myLoc = new MyLocation(locman);

  locationText = (TextView)findViewById(R.id.location_text);

  System.out.println("about to get bc");
	//breadCrumbTrail = (BreadCrumbTrail)findViewById(R.id.bread_crumb_trail);
      breadCrumbTrail = new BreadCrumbTrail(this, myLoc);

      System.out.println("setting model on bc");
      //breadCrumbTrail.setModel(myLoc);

      myLoc.setLocationChangeListener(new MyLocation.LocationChangeListener() {
          @Override public void onLocationChange(Location l) {
              makeUseOfNewLocation( l );
          } 
      });

    }

  //public void onLocationChanged(Location location) {
  public void makeUseOfNewLocation(Location location) {

    // refresh bread crumb view
    breadCrumbTrail.invalidate();

    // refresh text view
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
    //time: %tm/%td/%ty %tH:%tM
    Location latestLocation = location;
    String locationString = String.format(
        "%tD %tr \n@ %f, %f \n\naccuracy +/- %fm \nbest accuracy %fm\n\naltitude %f\n\nspeed: %f m/s\nspeed: %f mph\nmax speed: %f m/s\nmax speed: %f mph",
        location.getTime(),
        location.getTime(),
        location.getLatitude(),
        location.getLongitude(),
        location.getAccuracy(),
        bestAccuracy,
        location.getAltitude(),
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
