package com.example.zhangkai.gpstraker;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

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
            instance.mqttclient = new MqttAndroidClient(context, Constants.MQTTBROKERHOST, clientid);
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
        connOpt.setConnectionTimeout(Constants.MQTTCONNIMEOUT);
        connOpt.setKeepAliveInterval(Constants.MQTTKEEPALIVEINTERVAL);
        connOpt.setCleanSession(false);
        this.mqttclient.setCallback(new MqttCallbackHandler(this.context,this.clientid));
        this.mqttclient.setTraceCallback(new MqttTraceCallback());
        try{
            this.mqttclient.connect(connOpt,this.context, acListener);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }
}
