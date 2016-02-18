package com.example.zhangkai.gpstraker;

import android.app.AlarmManager;
import android.app.Notification;
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
import android.util.Log;
import android.widget.Toast;


import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zhangkai on 2016/2/16.
 */
public class LocationUpService extends Service {
    private static final int PERIOD=1000*30;  // 30 seconds
    private static final int GPSTIMEOUT=1000*10;  // 10 seconds
    private PendingIntent pi=null;
    private AlarmManager mgr=null;

    private final BroadcastReceiver locatinReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location loc=(Location)intent.getExtras().get(LocationPoller.EXTRA_LOCATION);
            Long time = loc.getTime();
            Locale currentlocale = getResources().getConfiguration().locale;

            String msg;

            if (loc==null) {
                msg=intent.getStringExtra(LocationPoller.EXTRA_ERROR);
            }
            else {
                msg=loc.toString();
            }

            if (msg==null) {
                msg="Invalid broadcast received!";
            }

            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
//            String time = String.valueOf(new Date().getTime());
//            File log = new File(Environment.getExternalStorageDirectory(),"LocationLog"+time+".txt");
//            try {
//                BufferedWriter out = new BufferedWriter(new FileWriter(log.getAbsolutePath(), log.exists()));
//                out.write("---");
//                out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            MqttConnection c = MqttConnections.getInstance().getConnection("zhangkai");
            if(c != null){
                c.Publish("zhangkai", "hello world");
            }
        }
    };

    @Override
    public void onCreate() {
        Log.i("giskook", "LocationUp onCreate");
        super.onCreate();
        HandlerThread handlerThread = new HandlerThread("ht");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        Handler handler = new Handler(looper);

        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(Constants.LOCATIONACTION);
        registerReceiver(locatinReceiver, theFilter, null, handler);

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

    private void startLocationUpService(){
        MqttConnection connection = MqttConnection.createMqttConnection("zhangkai", "test.mosquitto.org",1883,this.getApplicationContext(),false);
        try {
            connection.connect();
            MqttConnections.getInstance().addConnection(connection);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        Intent i=new Intent(this, LocationPoller.class);
//        i.setAction(Constants.LOCATIONACTION);

        Bundle bundle = new Bundle();
        LocationPollerParameter parameter = new LocationPollerParameter(bundle);
        Intent extraIntent = new Intent(Constants.LOCATIONACTION);
        //extraIntent.putExtra("mqtt", connection.handle().toString());
        parameter.setIntentToBroadcastOnCompletion(extraIntent);
        // try GPS and fall back to NETWORK_PROVIDER
        parameter.setProviders(new String[] {LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER});
        parameter.setTimeout(GPSTIMEOUT);
        i.putExtras(bundle);

        pi= PendingIntent.getBroadcast(this, 0, i, 0);
        mgr=(AlarmManager)getSystemService(ALARM_SERVICE);
        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                PERIOD,
                pi);
//        long firstMillis = System.currentTimeMillis();
//
//        //        mgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,firstMillis,AlarmManager.INTERVAL_HALF_HOUR/60,pi);
//        mgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,firstMillis,PERIOD,pi);


    }
}
