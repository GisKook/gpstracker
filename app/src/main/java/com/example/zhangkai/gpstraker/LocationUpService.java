package com.example.zhangkai.gpstraker;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zhangkai on 2016/2/16.
 */
public class LocationUpService extends Service {
    private PendingIntent pi=null;
    private AlarmManager mgr=null;

    private final BroadcastReceiver locatinReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.LOCATIONACTION)){
                Location loc=(Location)intent.getExtras().get(LocationPoller.EXTRA_LOCATION);

                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss",Locale.CHINA);
                String strDate = (String) dateformat.format(loc.getTime());
                JSONObject jsondata = new JSONObject();
                try {
                    jsondata.put("date", strDate);
                    jsondata.put("card_no", "123456");
                    if(loc.getProvider().equals( "network")){
                        jsondata.put("type","LBS");
                    }else{
                        jsondata.put("type","gps");
                    }
                    JSONObject jsoncontent = new JSONObject();
                    jsoncontent.put("type","Point");
                    JSONArray coordinates = new JSONArray();
                    coordinates.put(loc.getLongitude());
                    coordinates.put(loc.getLatitude());
                    jsoncontent.put("coordinates",coordinates);
                    jsondata.put("content", jsoncontent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MqttConnection c = MqttConnections.getInstance().getConnection(Constants.MQTTTOPIC);
                if(c != null){
                    c.Publish(Constants.MQTTTOPIC, jsondata.toString());
                }
            }else if(intent.getAction().equals(Constants.STARTLOCATIONPOLLERACTION)){
                LocationPollerService.requestLocation(context, intent);
            }
        }
    };

    @Override
    public void onCreate() {
        Log.i("giskook", "LocationUp onCreate");
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.i("giskook","LocationUp");
            };
        });
//        HandlerThread handlerThread = new HandlerThread("ht");
//        handlerThread.start();
//        Looper looper = handlerThread.getLooper();
//        Handler handler = new Handler(looper);
//
        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(Constants.LOCATIONACTION);
        theFilter.addAction(Constants.STARTLOCATIONPOLLERACTION);
//        registerReceiver(locatinReceiver, theFilter, null, handler);
        registerReceiver(locatinReceiver, theFilter);

        startLocationUpService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i("giskook", "locationuodestroy");
    }

    private void startLocationUpService(){
        //Intent i=new Intent(this, LocationPoller.class);
//        i.setAction(Constants.LOCATIONACTION);
        Intent i=new Intent(Constants.STARTLOCATIONPOLLERACTION);

        Bundle bundle = new Bundle();
        LocationPollerParameter parameter = new LocationPollerParameter(bundle);
        Intent extraIntent = new Intent(Constants.LOCATIONACTION);
        //extraIntent.putExtra("mqtt", connection.handle().toString());
        parameter.setIntentToBroadcastOnCompletion(extraIntent);
        // try GPS and fall back to NETWORK_PROVIDER
        parameter.setProviders(new String[] {LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER});
        parameter.setTimeout(Constants.LOCATIONGPSTIMEOUT);
        i.putExtras(bundle);

        pi= PendingIntent.getBroadcast(this, 0, i, 0);
        mgr=(AlarmManager)getSystemService(ALARM_SERVICE);
   //     mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
   //             SystemClock.elapsedRealtime(),
   //             Constants.LOCATIONPERIOD,
   //             pi);
        long firstMillis = System.currentTimeMillis();

        mgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,firstMillis,Constants.LOCATIONPERIOD,pi);
    }
}
