package com.example.zhangkai.gpstraker.Location;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.zhangkai.gpstraker.Constants;
import com.example.zhangkai.gpstraker.NetWork.MqttConn;
import com.example.zhangkai.gpstraker.Protocol.EncodeProtocol;
import com.example.zhangkai.gpstraker.util;

/**
 * Created by zhangkai on 2016/3/5.
 */
public class AMapLocClient {
    private  static AMapLocClient amap_instance = null;
    private AMapLocationClient locclient = null;
    public AMapLocClient(final Context context){
        AMapLocationClient.setApiKey("00f3db3c0062e311e923627f1cc1d9d6");
        locclient = new AMapLocationClient(context);
        AMapLocationClientOption locationOpt = new AMapLocationClientOption();
        locationOpt.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        locationOpt.setOnceLocation(true);
        locationOpt.setHttpTimeOut(Constants.NETWORK_LOCATION_TIMEOUT);
        locationOpt.setNeedAddress(false);
        locclient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                String locationprotocol = EncodeProtocol.encodeLocationProtocol("123456", aMapLocation);
                if(MqttConn.getInstance(context, Constants.MQTT_CLIENT_ID).isConnect()){
                    MqttConn.getInstance(context, Constants.MQTT_CLIENT_ID).publish(Constants.MQTT_TOPIC, locationprotocol);
                }
            }
        });
        locclient.setLocationOption(locationOpt);
    }
    public synchronized static AMapLocClient getInstance(Context context) {
        if (amap_instance == null) {
            amap_instance = new AMapLocClient(context);
        }

        return amap_instance;
    }

    public void start(){
        if(locclient != null){
            util.recordLog(Constants.LOGFILE, "amap start");
            locclient.startLocation();
        }else{
            util.recordLog(Constants.LOGFILE, "___________amaplocation null");
        }
    }

    public void stop(){
        if(locclient != null){
            locclient.stopLocation();
        }else{
            util.recordLog(Constants.LOGFILE, "___________amaplocation null");
        }
    }
}
