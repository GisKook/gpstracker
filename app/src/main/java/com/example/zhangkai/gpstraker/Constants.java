package com.example.zhangkai.gpstraker;

/**
 * Created by zhangkai on 2016/2/16.
 */
public class Constants {
    public static final String LOCATIONACTION = "zhangkai.LOCATION";
    public static final String STARTLOCATIONPOLLERACTION = "zhangkai.STARTLOCATIONPOLLER";
//    public static final String MQTTTOPIC="location";
//    public static final String MQTTBROKERHOST="222.222.218.50";
//    public static final int MQTTBROKERPORT=31883;

    public static final String MQTT_TOPIC = "zhangkai";
    public static final String MQTT_LOCATIOIN_TOPIC = "zhangkai";
    public static final String MQTT_BROKER_HOST = "tcp://test.mosquitto.org:1883";
    public static final int MQTT_BROKER_PORT = 1883;
    public static final int MQTT_KEEPALIVE_INTERVAL = 60;
    public static final int MQTT_CONN_IMEOUT = 45;
    public static final String LOGFILE = "gpstrackerlog.txt";

    public static final int LOCATION_PERIOD = 60 * 1000;
    public static final int LOCATION_GPS_TIMEOUT = 20 * 1000;
    public static final int NETWORK_LOCATION_TIMEOUT=10*1000;

    public static String MQTT_CLIENT_ID = "zhangkai";
}
