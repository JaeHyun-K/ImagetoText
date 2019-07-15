package com.example.imagetotext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.widget.ImageView;
import android.widget.TextView;

public class BatteryReceiver extends BroadcastReceiver {

    public void onReceive(Context context,Intent intent){
        TextView statusLabel=((BatteryCheckActivity)context).findViewById(R.id.statusLabel);
        TextView percentageLabel=((BatteryCheckActivity)context).findViewById(R.id.percentageLabel);
        ImageView batteryImage=((BatteryCheckActivity)context).findViewById(R.id.batteryImage);

        String action=intent.getAction();
        if(action!=null && action.equals(Intent.ACTION_BATTERY_CHANGED)){
            int status=intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
            String message="";
            switch (status){
                case BatteryManager.BATTERY_STATUS_FULL:
                    message="FULL";
                    break;

                case BatteryManager.BATTERY_STATUS_CHARGING:
                    message="Charging";
                    break;

                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    message="Discharging";
                    break;

                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    message="Not charging";
                    break;

                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    message="UNKNOWN";
                    break;

            }
            statusLabel.setText(message);

            int level=intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            int scale=intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
            int percentage=level * 100/scale;
            percentageLabel.setText(percentage+"%");

            Resources res=context.getResources();

            if(percentage>=90){
                batteryImage.setImageDrawable(res.getDrawable(R.drawable.full));
            } else if(90>percentage && percentage>=65){
                batteryImage.setImageDrawable(res.getDrawable(R.drawable.b80));
            }else if(65>percentage &&percentage>=45){
                batteryImage.setImageDrawable(res.getDrawable(R.drawable.b50));
            }else if(45>percentage &&percentage>=20){
                batteryImage.setImageDrawable(res.getDrawable(R.drawable.b30));
            }else if(20>percentage && percentage>=1){
                batteryImage.setImageDrawable(res.getDrawable(R.drawable.b10));
            }else {
                batteryImage.setImageDrawable(res.getDrawable(R.drawable.b0));
            }

        }
    }
}
