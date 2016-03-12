package com.example.zhangkai.gpstraker;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Environment;
import android.util.Log;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

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

import static com.example.zhangkai.gpstraker.DataBase.*;

/**
 * Created by zhangkai on 2016/2/4.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Constants.location_result_receiver++;
        if(Constants.location_result_receiver != Constants.location_result_sender){
            Constants.location_result_receiver++;
        }
        String err = (String) intent.getExtras().get(LocationPoller.EXTRA_ERROR);
        if (err.equals(Constants.LOCATIONPOLLER_TIMEOUT)) {
            util.recordLog(Constants.LOGFILE, Constants.LOCATIONPOLLER_TIMEOUT);
            AMapLocClient.getInstance(context).start();
        } else if (err.equals(Constants.LOCATIONPOLLER_SUCCESS)) {
            Location loc = (Location) intent.getExtras().get(LocationPoller.EXTRA_LOCATION);
            String locationprotocol = EncodeProtocol.encodeLocationProtocol("123456", loc);
            util.recordLog(Constants.LOGFILE, "GPS " + loc.getLatitude() + " " + loc.getLongitude());
        }

//        if(MqttConn.getInstance(context,"zhangkai").isConnect()){
//            MqttConn.getInstance(context, "zhangkai").publish(Constants.MQTTLOCATIOINTOPIC, locationprotocol);
//        }else{
// //           MqttConn.getInstance(context, "zhangkai").connect();
//            DataBase dbHelper = new DataBase(context);
//            SQLiteDatabase db = dbHelper.getWritableDatabase();
//            // Create a new map of values, where column names are the keys
//            ContentValues values = new ContentValues();
//            values.put(COLUMN_NAME_LOCATION, locationprotocol);
//
//            // Insert the new row, returning the primary key value of the new row
//            db.insert(TABLE_NAME,null,values);
//        }
    }
}
