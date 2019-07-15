package com.example.imagetotext;

import android.app.Activity;
import android.os.Build;
import android.widget.Toast;

public class BackPressHandler {

    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;

    public BackPressHandler(Activity activity) {
        this.activity = activity;
    }

    public void onBackPressed() {

        if (isAfter2Seconds()) {
            backKeyPressedTime = System.currentTimeMillis();

            toast = Toast.makeText(activity, "Press \'Back\' Button to Close App", Toast.LENGTH_SHORT);
            toast.show();

            return;
        }

        if (isBefore2Seconds()) {
            appShutdown();
            toast.cancel();
        }
    }

    private Boolean isAfter2Seconds() {
        return System.currentTimeMillis() > backKeyPressedTime + 200;
    }

    private Boolean isBefore2Seconds() {
        return System.currentTimeMillis() <= backKeyPressedTime + 200;
    }

    private void appShutdown() {
        if (Build.VERSION.SDK_INT >= 21) {
//            activity.finishAndRemoveTask();
            activity.moveTaskToBack(true);
            activity.finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            activity.finish();
        }

        System.exit(0);
    }
}
