package com.example.zhangkai.gpstraker;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.commonsware.cwac.locpoll.LocationPoller;
import com.commonsware.cwac.locpoll.LocationPollerParameter;

import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by zhangkai on 2016/2/16.
 */
public class LocationUpService extends Service {
    private static final int PERIOD=1000*30;  // 30 seconds
    private static final int GPSTIMEOUT=1000*10;  // 10 seconds
    private PendingIntent pi=null;
    private AlarmManager mgr=null;

    @Override
    public void onCreate() {
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
        Intent noteintent = new Intent(this,GPSActivity.class);
        noteintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingintent = PendingIntent.getActivity(this,0,noteintent,0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("content tile")
                .setContentText("content text")
                .setContentIntent(pendingintent).build();
        startForeground(1235,notification);

        MqttConnection connection = MqttConnection.createMqttConnection("zhangkai", "test.mosquitto.org",1883,this.getApplicationContext(),false);
        try {
            connection.connect();
            MqttConnections.getInstance().addConnection(connection);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        Intent i=new Intent(this, LocationPoller.class);

        Bundle bundle = new Bundle();
        LocationPollerParameter parameter = new LocationPollerParameter(bundle);
        Intent extraIntent = new Intent(this, AlarmReceiver.class);
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
