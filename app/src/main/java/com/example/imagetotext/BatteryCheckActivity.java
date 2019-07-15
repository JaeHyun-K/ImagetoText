package com.example.imagetotext;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.annotation.Inherited;

public class BatteryCheckActivity extends AppCompatActivity {

    private BatteryReceiver mBatteryReceiver=new BatteryReceiver();
    private IntentFilter mIntentFilter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battery);
    }

    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver(mBatteryReceiver,mIntentFilter);
    }

    @Override
    protected void onPause(){
        unregisterReceiver(mBatteryReceiver);
        super.onPause();
    }
}
