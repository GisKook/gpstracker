package com.example.zhangkai.gpstraker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;

import com.commonsware.cwac.locpoll.LocationPoller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Created by zhangkai on 2016/2/4.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent i = new Intent(context, ReportLocationDataService.class);
//        i.putExtra("hello","world");
//        context.startService(i);

        File log =
                new File(Environment.getExternalStorageDirectory(),
                        "LocationLog.txt");

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(log.getAbsolutePath(), log.exists()));

            out.write(new Date().toString());
            out.write(" : ");

            Bundle b = intent.getExtras();
            Location loc = (Location) b.get(LocationPoller.EXTRA_LOCATION);
            String msg;

            if (loc == null) {
                loc = (Location) b.get(LocationPoller.EXTRA_LASTKNOWN);

                if (loc == null) {
                    msg = intent.getStringExtra(LocationPoller.EXTRA_ERROR);
                } else {
                    msg = "TIMEOUT, lastKnown=" + loc.toString();
                }
            } else {
                msg = loc.toString();
            }

            if (msg == null) {
                msg = "Invalid broadcast received!";
            }

            out.write(msg);
            out.write("\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
