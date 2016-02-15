package com.example.zhangkai.gpstraker;

import java.util.HashMap;

/**
 * Created by zhangkai on 2016/2/15.
 */
public class MqttConnections {
    private static MqttConnections instance = null;
    private HashMap<String, MqttConnection> connections = null;

    private MqttConnections(){
        connections = new HashMap<String, MqttConnection>();
    }

    public synchronized static MqttConnections getInstance(){
        if(instance == null){
            instance = new MqttConnections();
        }

        return instance;
    }

    public MqttConnection getConnection(String handle){
        return connections.get(handle);
    }

    public void addConnection(MqttConnection connection){
        connections.put(connection.handle(), connection);
    }

    public void removeConnection(MqttConnection connection){
        connections.remove(connection.handle());
    }

    public HashMap<String, MqttConnection> getConnections(){
        return connections;
    }
}
