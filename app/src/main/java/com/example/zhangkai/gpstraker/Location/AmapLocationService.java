package com.example.zhangkai.gpstraker.Location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.zhangkai.gpstraker.Constants;
import com.example.zhangkai.gpstraker.NetWork.MqttConn;
import com.example.zhangkai.gpstraker.Protocol.EncodeProtocol;

/**
 * Created by zhangkai on 2016/3/21.
 */
public class AmapLocationService extends Service {
    private AMapLocationClient locclient = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        AmapLocationThread thread = new AmapLocationThread("amaplocation", this);
        thread.start();
        return 0;
    }

    private class AmapLocationThread extends HandlerThread {
        private Handler handler = new Handler();
        Context context = null;

        private Runnable onTimeout = new Runnable(){
            @Override
            public void run() {
                quit();
            }
        };

        public AmapLocationThread(String name, Context cxt) {
            super(name);
            context = cxt;
        }

        protected void onPreExecute() {
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
            locclient.startLocation();
            handler.postDelayed(onTimeout, Constants.NETWORK_LOCATION_TIMEOUT);
        }

        protected void onPostExecute() {
//            AMapLocClient.getInstance(context).stop();
            locclient.stopLocation();
            stopSelf();
        }

        @Override
        protected void onLooperPrepared() {
            try {
                onPreExecute();
            } catch (RuntimeException e) {
                onPostExecute();
                throw (e);
            }

        }

        @Override
        public void run() {
            try {
                super.run();
            } finally {
                onPostExecute();
            }
        }

    }
}
