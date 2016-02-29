package com.example.zhangkai.gpstraker;

import android.content.Context;
import android.location.LocationManager;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zhangkai on 2016/2/20.
 */
public class util {
    public static void recordLog(String filename, String filecontent){
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss", Locale.CHINA);
        String strDate = (String) dateformat.format(new Date());
        File log = new File(Environment.getExternalStorageDirectory(),filename);

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(log.getAbsolutePath(), log.exists()));
            out.write(strDate);
            out.write(" : ");
            out.write(filecontent);
            out.write("\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isGpsEnabled(Context context){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        return gps_enabled;
    }

    public static boolean isNetworkLocationEnabled(Context context){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean network_enable = false;

        try {
            network_enable = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        return network_enable ;
    }
}
