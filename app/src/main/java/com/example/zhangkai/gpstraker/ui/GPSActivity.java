package com.example.zhangkai.gpstraker.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.commonsware.cwac.locpoll.LocationPoller;
import com.example.zhangkai.gpstraker.BuildConfig;
import com.example.zhangkai.gpstraker.Constants;
import com.example.zhangkai.gpstraker.Location.LocationReceiver;
import com.example.zhangkai.gpstraker.NetWork.MqttConn;
import com.example.zhangkai.gpstraker.R;

public class GPSActivity extends AppCompatActivity {
//public class GPSActivity extends Activity {
    private PendingIntent pi = null;
    private AlarmManager mgr = null;
    private Button btnTestANR = null;
    Context cxt = null;

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
        cxt = this;

        this.btnTestANR = (Button)this.findViewById(R.id.button);
        this.btnTestANR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "hello world", Toast.LENGTH_SHORT).show();
                NotificationDialogFragment dialog = new NotificationDialogFragment();
//                dialog.getDialog().show();

                NotificationManager mNotifyManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(cxt);
                mBuilder.setContentTitle("Picture Download")
                        .setContentText("Download in progress")
                        .setSmallIcon(R.drawable.loction);
                mNotifyManager.notify(1222,mBuilder.build());
            }
        });

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

        AlarmManager mgr = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent i = new Intent(this, LocationPoller.class);

        Intent extraIntent = new Intent(this, LocationReceiver.class);
        i.putExtra(LocationPoller.EXTRA_INTENT, extraIntent);
        i.putExtra(LocationPoller.EXTRA_PROVIDER, LocationManager.GPS_PROVIDER);
        Bundle bundle = new Bundle();
        bundle.putLong(LocationPoller.EXTRA_TIMEOUT, Constants.LOCATION_GPS_TIMEOUT);
        i.putExtras(bundle);

        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                Constants.LOCATION_PERIOD,
                pi);
    }
}
