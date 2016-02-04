package com.example.zhangkai.gpstraker;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
//import android.widget.Toast;

/**
 * Created by zhangkai on 2016/2/4.
 */
public class ReportLocationDataService extends IntentService {
    public ReportLocationDataService(){
        super("ReportLocationDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        Toast.makeText(getApplicationContext(), "handle intent", Toast.LENGTH_SHORT).show();
        Log.i("---------------------------------------------------------------------",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}
