package com.example.zhangkai.gpstraker;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
//        Intent i = new Intent(this, LocationUpService.class);
//        startService(i);
        startLocationUpService();
        startLocationAssitant();
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

    private void startLocationUpService(){
        MqttConnection connection = MqttConnection.createMqttConnection(Constants.MQTTTOPIC, Constants.MQTTBROKERHOST,Constants.MQTTBROKERPORT,this.getApplicationContext(),false);
        connection.connect(0);
        MqttConnections.getInstance().addConnection(connection);

        Intent i=new Intent(this, LocationPoller.class);
//        i.setAction(Constants.LOCATIONACTION);
//        Intent i=new Intent(Constants.STARTLOCATIONPOLLERACTION);

        Bundle bundle = new Bundle();
        LocationPollerParameter parameter = new LocationPollerParameter(bundle);
//        Intent extraIntent = new Intent(Constants.LOCATIONACTION);
        Intent extraIntent = new Intent(this, AlarmReceiver.class);
        //extraIntent.putExtra("mqtt", connection.handle().toString());
        parameter.setIntentToBroadcastOnCompletion(extraIntent);
        // try GPS and fall back to NETWORK_PROVIDER
        parameter.setProviders(new String[] {LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER});
        parameter.setTimeout(Constants.LOCATIONGPSTIMEOUT);
        i.putExtras(bundle);

        pi= PendingIntent.getBroadcast(this, 0, i, 0);
        mgr=(AlarmManager)getSystemService(ALARM_SERVICE);
         mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                 SystemClock.elapsedRealtime()+Constants.LOCATIONGPSTIMEOUT-1000,
                 Constants.LOCATIONPERIOD,
                 pi);

//        long firstMillis = System.currentTimeMillis();

//        mgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,firstMillis,Constants.LOCATIONPERIOD,pi);
//        mgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,firstMillis+Constants.LOCATIONGPSTIMEOUT,Constants.LOCATIONPERIOD,pi);
    }

    void startLocationAssitant(){
        Intent i=new Intent(this, GPSAssitantBroadcastReceiver.class);

        int _id = (int)System.currentTimeMillis();
        pi= PendingIntent.getBroadcast(this, _id, i, 0);

        mgr=(AlarmManager)getSystemService(ALARM_SERVICE);
        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                Constants.LOCATIONPERIOD,
                pi);

//        long firstMillis = System.currentTimeMillis();

//        mgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,firstMillis,Constants.LOCATIONPERIOD,pi);
//        mgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,firstMillis+Constants.LOCATIONGPSTIMEOUT,Constants.LOCATIONPERIOD,pi);

    }

}
