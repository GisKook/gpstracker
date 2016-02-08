package com.example.zhangkai.gpstraker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import com.commonsware.cwac.locpoll.LocationPoller;
import com.commonsware.cwac.locpoll.LocationPollerParameter;
import com.commonsware.cwac.locpoll.LocationPollerResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Created by zhangkai on 2016/2/4.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        File log = new File(Environment.getExternalStorageDirectory(),"LocationLog.txt");

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(log.getAbsolutePath(), log.exists()));

            out.write(new Date().toString());
            out.write(" : ");

            Bundle b = intent.getExtras();

            LocationPollerResult locationResult = new LocationPollerResult(b);

            Location loc=locationResult.getLocation();
            String msg;

            if (loc==null) {
                loc=locationResult.getLastKnownLocation();

                if (loc==null) {
                    msg=locationResult.getError();
                }
                else {
                    msg="TIMEOUT, lastKnown="+loc.toString();
                }
            }
            else {
                msg=loc.toString();
            }

            if (msg==null) {
                msg="Invalid broadcast received!";
            }

            out.write(msg);
            out.write("\n");
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
