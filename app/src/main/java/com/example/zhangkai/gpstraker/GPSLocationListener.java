package com.example.zhangkai.gpstraker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by zhangkai on 2016/2/5.
 */
public class GPSLocationListener implements LocationListener {
   public GPSLocationListener(){

    }

    @Override
    public void onLocationChanged(Location location) {
        String message = String.format(
                "------------------------------New Location \n Longitude: %1$s \n Latitude: %2$s",
                location.getLongitude(), location.getLatitude()
        );
        Log.i("----------",message);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("---------","++++++++++++++++++++");

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
