package com.example.zhangkai.gpstraker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class GPSActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        Intent mLocationUpInent = new Intent(this,LocationUpService.class);
        startService(mLocationUpInent);
    }
}
