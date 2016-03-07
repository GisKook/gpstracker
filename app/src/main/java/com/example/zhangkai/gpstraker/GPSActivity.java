package com.example.zhangkai.gpstraker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

public class GPSActivity extends AppCompatActivity {
    private PendingIntent pi=null;
    private AlarmManager mgr=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);


        long starttime = SystemClock.elapsedRealtime();
        starttime += 1000;
//
//        startLocationAssitant(starttime, GPSAssitantBroadcastReceiver.class);
//        startLocationAssitant(starttime+Constants.LOCATIONGPSASSITANT, GPSAssitantBroadcastReceiver2.class);
//        startLocationUpService(starttime + Constants.LOCATIONGPSASSITANT*2);
        startLocationUpService(starttime);
        MqttConn.getInstance(this, "zhangkai").connect();
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

    private void startLocationUpService(long starttime){
//        MqttConnection connection = MqttConnection.createMqttConnection(Constants.MQTTTOPIC, Constants.MQTTBROKERHOST,Constants.MQTTBROKERPORT,this.getApplicationContext(),false);
//        connection.connect(0);
//        MqttConnections.getInstance().addConnection(connection);

        Intent i=new Intent(this, LocationPoller.class);

        Bundle bundle = new Bundle();
        LocationPollerParameter parameter = new LocationPollerParameter(bundle);
        Intent extraIntent = new Intent(this, AlarmReceiver.class);
        parameter.setIntentToBroadcastOnCompletion(extraIntent);
        // try GPS and fall back to NETWORK_PROVIDER
        parameter.setProviders(new String[] {LocationManager.GPS_PROVIDER});
        parameter.setTimeout(Constants.LOCATIONGPSTIMEOUT);
        i.putExtras(bundle);

        pi= PendingIntent.getBroadcast(this, 0, i, 0);
        mgr=(AlarmManager)getSystemService(ALARM_SERVICE);
         mgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                 starttime,
                 Constants.LOCATIONPERIOD,
                 pi);

//        long firstMillis = System.currentTimeMillis();

//        mgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,firstMillis,Constants.LOCATIONPERIOD,pi);
//        mgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,firstMillis+Constants.LOCATIONGPSTIMEOUT,Constants.LOCATIONPERIOD,pi);
    }

    void startLocationAssitant(long starttime,  Class<?> cls ){
        Intent i=new Intent(this, cls);

        int _id = (int)System.currentTimeMillis();
        pi= PendingIntent.getBroadcast(this, _id, i, 0);

        mgr=(AlarmManager)getSystemService(ALARM_SERVICE);
        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                starttime,
                Constants.LOCATIONPERIOD,
                pi);
    }

}
