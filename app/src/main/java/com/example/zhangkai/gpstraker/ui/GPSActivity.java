package com.example.zhangkai.gpstraker.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import com.commonsware.cwac.locpoll.LocationPoller;
import com.example.zhangkai.gpstraker.BuildConfig;
import com.example.zhangkai.gpstraker.Constants;
import com.example.zhangkai.gpstraker.Location.LocationReceiver;
import com.example.zhangkai.gpstraker.NetWork.MqttConn;
import com.example.zhangkai.gpstraker.R;

public class GPSActivity extends AppCompatActivity {
    private PendingIntent pi = null;
    private AlarmManager mgr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        startLocationUpService();
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
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startLocationUpService() {
//        MqttConnection connection = MqttConnection.createMqttConnection(Constants.MQTTTOPIC, Constants.MQTTBROKERHOST,Constants.MQTTBROKERPORT,this.getApplicationContext(),false);
//        connection.connect(0);
//        MqttConnections.getInstance().addConnection(connection);

        AlarmManager mgr=(AlarmManager)getSystemService(ALARM_SERVICE);

        Intent i=new Intent(this, LocationPoller.class);

        Intent extraIntent = new Intent(this, LocationReceiver.class);
        i.putExtra(LocationPoller.EXTRA_INTENT,extraIntent);
        i.putExtra(LocationPoller.EXTRA_PROVIDER,LocationManager.GPS_PROVIDER);
        i.putExtra(LocationPoller.EXTRA_TIMEOUT, Constants.LOCATION_GPS_TIMEOUT);


        PendingIntent pi=PendingIntent.getBroadcast(this, 0, i, 0);
        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                Constants.LOCATION_PERIOD,
                pi);
    }
}
