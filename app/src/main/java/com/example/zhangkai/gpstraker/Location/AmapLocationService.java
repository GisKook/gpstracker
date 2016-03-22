package com.example.zhangkai.gpstraker.Location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.zhangkai.gpstraker.Constants;

/**
 * Created by zhangkai on 2016/3/21.
 */
public class AmapLocationService extends Service {
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
            AMapLocClient.getInstance(context).start();
            handler.postDelayed(onTimeout, Constants.NETWORK_LOCATION_TIMEOUT);
        }

        protected void onPostExecute() {
            AMapLocClient.getInstance(context).stop();
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
