package com.example.zhangkai.gpstraker;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;
//import android.widget.Toast;

/**
 * Created by zhangkai on 2016/2/4.
 */
public class ReportLocationDataService extends IntentService {
    public ReportLocationDataService() {
        super("ReportLocationDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        Toast.makeText(getApplicationContext(), "handle intent", Toast.LENGTH_SHORT).show();

        LocationManager LocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener LocListener = new GPSLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("------- ","+++++++++++++++++++++++++++++++++++++");
            return;
        }
        LocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000, 0, LocListener);
        Log.i("----------->>", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

    }
}
