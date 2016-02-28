package com.example.zhangkai.gpstraker;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Environment;


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
        Location loc=(Location)intent.getExtras().get(LocationPoller.EXTRA_LOCATION);

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss", Locale.CHINA);
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

        util.recordLog("locationRecevier.txt",android.os.Process.myPid() + " " + loc.getLatitude() + " " + loc.getLongitude() + " " + loc.getProvider().toString() );

        MqttConnection c = MqttConnections.getInstance().getConnection(Constants.MQTTTOPIC);
        if(c != null ){
            if(c.isConnnected()){
                c.Publish(Constants.MQTTTOPIC, jsondata.toString());
            }else{
                DataBase dbHelper = new DataBase(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME_LOCATION, jsondata.toString());

                // Insert the new row, returning the primary key value of the new row
                db.insert(TABLE_NAME,null,values);
            }
        }
    }
}
