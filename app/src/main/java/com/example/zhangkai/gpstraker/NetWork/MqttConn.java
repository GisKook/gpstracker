package com.example.zhangkai.gpstraker.NetWork;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.zhangkai.gpstraker.Constants;
import com.example.zhangkai.gpstraker.DataBase;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by zhangkai on 2016/3/6.
 */
public class MqttConn {
    private MqttAndroidClient mqttclient = null;
    private Context context = null;
    private String clientid;

    private static MqttConn instance = null;
    private MqttConn( String clientid, Context context){
        this.context =context;
        this.clientid = clientid;
    }

    public synchronized static MqttConn getInstance(Context context, String clientid){
        if(instance == null){
            instance = new MqttConn(clientid, context);
            instance.mqttclient = new MqttAndroidClient(context, Constants.MQTT_BROKER_HOST, clientid);
        }

        return instance;
    }

    public boolean isConnect(){
        return this.mqttclient.isConnected();
    }

    public void connect(){
        String[] actionArgs = new String[0];
        ActionListener acListener = new ActionListener(this.context,ActionListener.Action.CONNECT,this.clientid,actionArgs);
        MqttConnectOptions connOpt = new MqttConnectOptions();
        connOpt.setConnectionTimeout(Constants.MQTT_CONN_IMEOUT);
        connOpt.setKeepAliveInterval(Constants.MQTT_KEEPALIVE_INTERVAL);
        connOpt.setCleanSession(false);
        this.mqttclient.setCallback(new MqttCallbackHandler(this.context,this.clientid));
        this.mqttclient.setTraceCallback(new MqttTraceCallback());
        try{
            this.mqttclient.connect(connOpt,this.context, acListener);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    public void publish(String topic, String value){
        MqttMessage message = new MqttMessage(value.getBytes());
        message.setQos(0);
        message.setRetained(false);
        try {
            this.mqttclient.publish(topic, message);
        } catch (MqttException e) {
            MqttConn.getInstance(context, "zhangkai").connect();
            DataBase dbHelper = new DataBase(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(DataBase.COLUMN_NAME_LOCATION, value);

            // Insert the new row, returning the primary key value of the new row
            db.insert(DataBase.TABLE_NAME,null,values);

            e.printStackTrace();
        }
    }
}
