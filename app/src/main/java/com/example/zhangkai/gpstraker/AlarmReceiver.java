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

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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
//        String clientHandle = intent.getStringExtra("mqtt");
//        MqttConnections.getInstance().getConnection(clientHandle).Publish("zhangkai","hello");
        String time = String.valueOf(new Date().getTime());
        File log = new File(Environment.getExternalStorageDirectory(),"LocationLog"+time+".txt");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(log.getAbsolutePath(), log.exists()));
            out.write("---");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        File log = new File(Environment.getExternalStorageDirectory(),"LocationLog.txt");
//
//        try {
//            BufferedWriter out = new BufferedWriter(new FileWriter(log.getAbsolutePath(), log.exists()));
//
//            out.write(new Date().toString());
//            out.write(" : ");
//            Bundle b = intent.getExtras();
//
//            LocationPollerResult locationResult = new LocationPollerResult(b);
//
//            String clientHandle = intent.getStringExtra("mqtt");
//            MqttConnections.getInstance().getConnection(clientHandle).Publish("zhangkai","hello");
//
//            Location loc=locationResult.getLocation();
//            String msg;
//
//            if (loc==null) {
//                loc=locationResult.getLastKnownLocation();
//
//                if (loc==null) {
//                    msg=locationResult.getError();
//                }
//                else {
//                    msg="TIMEOUT, lastKnown="+loc.toString();
//                }
//            }
//            else {
//                msg=loc.toString();
//            }
//
//            if (msg==null) {
//                msg="Invalid broadcast received!";
//            }
//
//            out.write(msg);
//            out.write("\n");
//            out.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
