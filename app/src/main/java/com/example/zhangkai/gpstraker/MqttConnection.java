package com.example.zhangkai.gpstraker;

import android.content.Context;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * Created by zhangkai on 2016/2/10.
 */
public class MqttConnection {
    /*
   * Basic Information about the client
   */
    /** ClientHandle for this Connection Object**/
    private String clientHandle = null;
    /** The clientId of the client associated with this <code>Connection</code> object **/
    private String clientId = null;
    /** The host that the {@link MqttAndroidClient} represented by this <code>Connection</code> is represented by **/
    private String host = null;
    /** The port on the server this client is connecting to **/
    private int port = 0;
    /** {@link MqttConnectionStatus} of the {@link MqttAndroidClient} represented by this <code>Connection</code> object. Default value is {@link} **/
    private MqttConnectionStatus status = MqttConnectionStatus.NONE;

    /** The {@link MqttAndroidClient} instance this class represents**/
    private MqttAndroidClient client = null;

    /** Collection of {@link PropertyChangeListener} **/
    private ArrayList<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();

    /** The {@link Context} of the application this object is part of**/
    private Context context = null;

    /** The {@link MqttConnectOptions} that were used to connect this client**/
    private MqttConnectOptions conOpt;

    /** True if this connection is secured using SSL **/
    private boolean sslConnection = false;

    private int conntimeout = Constants.MQTTCONNTIMEOUT;

    /**
     * Connections status for  a connection
     */
    enum MqttConnectionStatus {

        /** Client is Connecting **/
        CONNECTING,
        /** Client is Connected **/
        CONNECTED,
        /** Client is Disconnecting **/
        DISCONNECTING,
        /** Client is Disconnected **/
        DISCONNECTED,
        /** Client has encountered an Error **/
        ERROR,
        /** Status is unknown **/
        NONE
    }
    /**
     * Creates a connection from persisted information in the database store, attempting
     * to create a {@link MqttAndroidClient} and the client handle.
     * @param clientId The id of the client
     * @param host the server which the client is connecting to
     * @param port the port on the server which the client will attempt to connect to
     * @param context the application context
     * @param sslConnection true if the connection is secured by SSL
     * @return a new instance of <code>Connection</code>
     */
    public static MqttConnection createMqttConnection(String clientId, String host,
                                              int port, Context context, boolean sslConnection) {
        String handle = null;
        String uri = null;
        if (sslConnection) {
            uri = "ssl://" + host + ":" + port;
            handle = clientId;
        }
        else {
            uri = "tcp://" + host + ":" + port;
            handle = clientId;
        }
        MqttAndroidClient client = new MqttAndroidClient(context, uri, clientId);
        return new MqttConnection(handle, clientId, host, port, context, client, sslConnection);
    }

    /**
     * Creates a connection object with the server information and the client
     * hand which is the reference used to pass the client around activities
     * @param clientHandle The handle to this <code>Connection</code> object
     * @param clientId The Id of the client
     * @param host The server which the client is connecting to
     * @param port The port on the server which the client will attempt to connect to
     * @param context The application context
     * @param client The MqttAndroidClient which communicates with the service for this connection
     * @param sslConnection true if the connection is secured by SSL
     */
    public MqttConnection(String clientHandle, String clientId, String host,
                      int port, Context context, MqttAndroidClient client, boolean sslConnection) {
        //generate the client handle from its hash code
        this.clientHandle = clientHandle;
        this.clientId = clientId;
        this.host = host;
        this.port = port;
        this.context = context;
        this.client = client;
        this.sslConnection = sslConnection;
    }

    public boolean isConnnected(){
        return this.client.isConnected();
    }
    public void connect(int timeout){
        String[] actionArgs = new String[0];
        ActionListener acListener = new ActionListener(this.context,ActionListener.Action.CONNECT,clientHandle,actionArgs);
        MqttConnectOptions connOpt = new MqttConnectOptions();
        this.conntimeout += timeout;
        if(this.conntimeout >= Constants.MQTTMAXCONNTIMEOUT){
            connOpt.setConnectionTimeout(Constants.MQTTMAXCONNTIMEOUT);
        }
        connOpt.setKeepAliveInterval(Constants.MQTTKEEPALIVEINTERVAL);
        connOpt.setCleanSession(false);
        this.client.setCallback(new MqttCallbackHandler(this.context,this.clientHandle));
        this.client.setTraceCallback(new MqttTraceCallback());
        try{
            this.client.connect(connOpt,this.context, acListener);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    public String handle(){
        return clientHandle;
    }

    public void setConnectionStatus(MqttConnectionStatus status){
        this.status = status;
    }

    public MqttConnectionStatus getConnnectionStatus(){
        return this.status;
    }

    public void Publish(String topic, String value) {
        MqttMessage message = new MqttMessage(value.getBytes());
        message.setQos(0);
        message.setRetained(false);
        try {
            client.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
