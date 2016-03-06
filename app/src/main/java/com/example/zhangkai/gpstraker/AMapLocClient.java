package com.example.zhangkai.gpstraker;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.HashMap;

/**
 * Created by zhangkai on 2016/3/5.
 */
public class AMapLocClient {
    private  static AMapLocClient amap_instance = null;
    private AMapLocationClient locclient = null;
    private AMapLocClient(Context context){
        AMapLocationClient.setApiKey("00f3db3c0062e311e923627f1cc1d9d6");
        locclient = new AMapLocationClient(context);
        AMapLocationClientOption locationOpt = new AMapLocationClientOption();
        locationOpt.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        locationOpt.setOnceLocation(true);
        locationOpt.setHttpTimeOut(5000);
        locationOpt.setNeedAddress(false);
        locclient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                util.recordLog(Constants.LOGFILE, "network" + aMapLocation.getAltitude() + " " + aMapLocation.getLongitude());

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
        locclient.startLocation();
    }
}
