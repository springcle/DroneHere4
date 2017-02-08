package com.example.ksj_notebook.dronehere.manager;

import android.app.Activity;
import android.widget.Toast;

/****
 * Created by NAKNAK on 2017-02-05.
 */
//
public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;
    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            SystemExit();
        }
    }
    public void SystemExit() {
        activity.moveTaskToBack(true);
        activity.finish();
        toast.cancel();
        android.os.Process.killProcess(android.os.Process.myPid() );
        System.exit(0);
    }
    public void showGuide() {
        toast = Toast.makeText(activity, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
