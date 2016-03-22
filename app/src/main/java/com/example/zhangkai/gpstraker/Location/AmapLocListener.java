package com.example.zhangkai.gpstraker.Location;

import android.content.Intent;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.example.zhangkai.gpstraker.Constants;
import com.example.zhangkai.gpstraker.Protocol.EncodeProtocol;
import com.example.zhangkai.gpstraker.NetWork.MqttConn;

/**
 * Created by zhangkai on 2016/3/5.
 */
public class AmapLocListener implements AMapLocationListener{
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
    }
}
