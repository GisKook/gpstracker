package com.example.zhangkai.gpstraker;
import android.content.Intent;

import com.commonsware.cwac.wakeful.WakefulIntentService;

/**
 * Created by zhangkai on 2016/3/2.
 */
public class GPSLocatioinService extends WakefulIntentService{
    public GPSLocatioinService(){
        super("GPSLocationService");
    }

    @Override
    protected void doWakefulWork(Intent intent){
        util.recordLog("doWakefuwork.txt","test");
    }
}
