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
    public static final String MQTTLOCATIOINTOPIC="zhangkai";
    public static final String MQTTBROKERHOST="tcp://test.mosquitto.org:1883";
    public static final int MQTTBROKERPORT=1883;
    public static final int MQTTKEEPALIVEINTERVAL=60;
    public static final int MQTTCONNIMEOUT=45;
    public static final String LOGFILE="gpstrackerlog.txt";

    public static final int LOCATIONPERIOD=30*1000;
    public static final int LOCATIONGPSTIMEOUT=8*1000;
    public static final int LOCATIONGPSASSITANT=8*1000;
}
