package com.example.zhangkai.gpstraker;

/**
 * Created by zhangkai on 2016/2/16.
 */
public class Constants {
    public static final String LOCATIONACTION="zhangkai.LOCATION";
    public static final String STARTLOCATIONPOLLERACTION="zhangkai.STARTLOCATIONPOLLER";
//    public static final String MQTTTOPIC="location";
//    public static final String MQTTBROKERHOST="222.222.218.50";
//    public static final int MQTTBROKERPORT=31883;

    public static final String MQTTTOPIC="zhangkai";
    public static final String MQTTBROKERHOST="test.mosquitto.org";
    public static final int MQTTBROKERPORT=1883;
    public static final int MQTTKEEPALIVEINTERVAL=60;
    public static final int MQTTCONNTIMEOUT=5;
    public static final int MQTTMAXCONNTIMEOUT=120;

    public static final int LOCATIONPERIOD=30*1000;
    public static final int LOCATIONGPSTIMEOUT=8*1000;

}
