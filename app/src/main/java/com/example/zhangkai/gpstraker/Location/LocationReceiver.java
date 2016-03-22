package com.example.zhangkai.gpstraker.Location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.commonsware.cwac.locpoll.LocationPoller;
import com.example.zhangkai.gpstraker.Constants;

/**
 * Created by zhangkai on 2016/3/22.
 */
public class LocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Location loc = (Location) intent.getExtras().get(LocationPoller.EXTRA_LOCATION);
        String msg;

        if (loc == null) {
            msg = intent.getStringExtra(LocationPoller.EXTRA_ERROR);
            AMapLocClient.getInstance(context).start();
        } else {
            msg = loc.toString();
        }

        if (msg == null) {
            msg = "Invalid broadcast received!";
        }
    }
}
