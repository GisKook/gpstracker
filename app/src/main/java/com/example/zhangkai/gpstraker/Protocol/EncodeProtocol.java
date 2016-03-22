package com.example.zhangkai.gpstraker.Protocol;

import android.location.Location;

import com.amap.api.location.AMapLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by zhangkai on 2016/3/7.
 */
public class EncodeProtocol {
    public static String encodeLocationProtocol(String card_no, Location loc){
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss", Locale.CHINA);
        String strDate = (String) dateformat.format(loc.getTime());
        JSONObject jsondata = new JSONObject();
        try {
            jsondata.put("date", strDate);
            jsondata.put("card_no",card_no);
            if(loc.getProvider().equals("gps")){
                jsondata.put("type", "gps");
            }else{
                jsondata.put("type", "LBS");
            }
            JSONObject jsoncontent = new JSONObject();
            jsoncontent.put("type", "Point");
            JSONArray coordinates = new JSONArray();
            coordinates.put(loc.getLongitude());
            coordinates.put(loc.getLatitude());
            jsoncontent.put("coordinates", coordinates);
            jsondata.put("content", jsoncontent);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

        return jsondata.toString();
    }

    public static String encodeGPSLocationProtocol(String card_no,Location loc){
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss", Locale.CHINA);
        String strDate = (String) dateformat.format(loc.getTime());
        JSONObject jsondata = new JSONObject();
        try {
            jsondata.put("date", strDate);
            jsondata.put("card_no",card_no);
            jsondata.put("type", "gps");
            JSONObject jsoncontent = new JSONObject();
            jsoncontent.put("type", "Point");
            JSONArray coordinates = new JSONArray();
            coordinates.put(loc.getLongitude());
            coordinates.put(loc.getLatitude());
            jsoncontent.put("coordinates", coordinates);
            jsondata.put("content", jsoncontent);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

        return jsondata.toString();
    }

    public static String encodeLBSLocationProtocol(String card_no, AMapLocation location){
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss", Locale.CHINA);
        String strDate = (String) dateformat.format(location.getTime());
        JSONObject jsondata = new JSONObject();
        try {
            jsondata.put("date", strDate);
            jsondata.put("card_no",card_no);
            jsondata.put("type", "LBS");
            JSONObject jsoncontent = new JSONObject();
            jsoncontent.put("type", "Point");
            JSONArray coordinates = new JSONArray();
            coordinates.put(location.getLongitude());
            coordinates.put(location.getLatitude());
            jsoncontent.put("coordinates", coordinates);
            jsondata.put("content", jsoncontent);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

        return jsondata.toString();
    }
}
