package com.example.zhangkai.gpstraker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.MqttException;

public class GPSActivity extends AppCompatActivity {
    private static final int PERIOD=1000*30;  // 30 seconds
    private static final int GPSTIMEOUT=1000*10;  // 10 seconds
    private PendingIntent pi=null;
    private AlarmManager mgr=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

//        MqttConnection connection = MqttConnection.createMqttConnection("zhangkai", "test.mosquitto.org",1883,this.getApplicationContext(),false);
//        try {
//            connection.connect();
//            MqttConnections.getInstance().addConnection(connection);
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }

        // Construct an intent that will execute the AlarmReceiver
//        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
//        final PendingIntent pIntent = PendingIntent.getBroadcast(this, 0,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        final PendingIntent pIntent = PendingIntent.getBroadcast(this, 0,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        // Setup periodic alarm every 5 seconds
//        long firstMillis = System.currentTimeMillis(); // alarm is set right away
//        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
//        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
//        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
//                AlarmManager.INTERVAL_FIFTEEN_MINUTES/30, pIntent);

//        Intent i=new Intent(this.getApplicationContext(), LocationPoller.class);
//
//        Bundle bundle = new Bundle();
//        LocationPollerParameter parameter = new LocationPollerParameter(bundle);
//        Intent extraIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
//        //extraIntent.putExtra("mqtt", connection.handle().toString());
//        parameter.setIntentToBroadcastOnCompletion(extraIntent);
//        // try GPS and fall back to NETWORK_PROVIDER
//        parameter.setProviders(new String[] {LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER});
//        parameter.setTimeout(GPSTIMEOUT);
//        i.putExtras(bundle);
//
//        pi= PendingIntent.getBroadcast(this, 0, i, 0);
//        mgr=(AlarmManager)getSystemService(ALARM_SERVICE);
//        mgr.setRepeating(AlarmManager.RTC_WAKEUP,
//                SystemClock.elapsedRealtime(),
//                PERIOD,
//                pi);
        //long firstMillis = System.currentTimeMillis();

        //        mgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,firstMillis,AlarmManager.INTERVAL_HALF_HOUR/60,pi);
        //mgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,firstMillis,PERIOD,pi);

        //this.finish();

        Intent i = new Intent(this, LocationUpService.class);
        startService(i);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
