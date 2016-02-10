package com.example.zhangkai.gpstraker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.commonsware.cwac.locpoll.LocationPoller;
import com.commonsware.cwac.locpoll.LocationPollerParameter;

public class GPSActivity extends AppCompatActivity {

    private static final int PERIOD=1000*10;  // 30 minutes
    private PendingIntent pi=null;
    private AlarmManager mgr=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        scheduleAlarm();
    }

    // Setup a recurring alarm every half hour
    public void scheduleAlarm() {
        Intent i=new Intent(this, LocationPoller.class);

        Bundle bundle = new Bundle();
        LocationPollerParameter parameter = new LocationPollerParameter(bundle);
        parameter.setIntentToBroadcastOnCompletion(new Intent(this, AlarmReceiver.class));
       // try GPS and fall back to NETWORK_PROVIDER
        parameter.setProviders(new String[] {LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER});
        parameter.setTimeout(10000);
        i.putExtras(bundle);

        pi=PendingIntent.getBroadcast(this, 0, i, 0);
//        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime(),
//                PERIOD,
//                pi);
        long firstMillis = System.currentTimeMillis();
        mgr=(AlarmManager)getSystemService(ALARM_SERVICE);
        mgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,firstMillis,AlarmManager.INTERVAL_HALF_HOUR/10,pi);
    }

}
