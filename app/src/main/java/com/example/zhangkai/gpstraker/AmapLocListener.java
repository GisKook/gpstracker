package com.example.zhangkai.gpstraker;

import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by zhangkai on 2016/3/5.
 */
public class AmapLocListener implements AMapLocationListener{
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Log.i("giskook", aMapLocation.toString());
    }
}
