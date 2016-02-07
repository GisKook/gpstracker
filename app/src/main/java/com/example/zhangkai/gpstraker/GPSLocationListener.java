package com.example.zhangkai.gpstraker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Created by zhangkai on 2016/2/5.
 */
public class GPSLocationListener implements LocationListener {
   public GPSLocationListener(){

    }

    @Override
    public void onLocationChanged(Location location) {
        String message = String.format(
                "New Location  Longitude: %1$s  Latitude: %2$s",
                location.getLongitude(), location.getLatitude()
        );
                File log =
                new File(Environment.getExternalStorageDirectory(),
                        "LocationLog.txt");

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(log.getAbsolutePath(), log.exists()));

            out.write(new Date().toString());
            out.write(" : ");



            out.write(message);
            out.write("\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
