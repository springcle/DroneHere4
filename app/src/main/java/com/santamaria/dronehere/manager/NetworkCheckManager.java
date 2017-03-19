package com.santamaria.dronehere.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.santamaria.dronehere.MainActivity;

/**
 * Created by NAKNAK on 2017-01-06.
 */
public class NetworkCheckManager extends BroadcastReceiver {
    private Activity activity;
    AlertDialog mDialog = null;
    public NetworkCheckManager() {
        super();
    }
    public NetworkCheckManager(Activity activity) {
        this.activity = activity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action= intent.getAction();

        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            try {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
                NetworkInfo _wifi_network =
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if(_wifi_network != null) {
                    // wifi, 3g 둘 중 하나라도 있을 경우
                    if(_wifi_network != null && activeNetInfo != null) {
                    }
                    // wifi, 3g 둘 다 없을 경우
                    else{
                        if(mDialog == null) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.getContext());
                            dialog.setTitle("Network Check");
                            dialog.setMessage("네트워크 실행 확인 후 재시도");
                            dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(activity, MainActivity.class);
                                    activity.startActivity(intent);
                                    activity.finish();
                                    mDialog = null;
                                }
                            });
                            dialog.setNegativeButton("종료", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    activity.finish();
                                    mDialog = null;
                                    activity.moveTaskToBack(true);
                                    activity.finish();
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(0);
                                }
                            });
                            mDialog = dialog.create();
                            mDialog.setCancelable(false);
                            mDialog.setCanceledOnTouchOutside(false);
                            mDialog.show();
                        }
                    }
                }
            } catch (Exception e) {
           //     Log.i("ULNetworkReceiver", e.getMessage());
            }
        }
    }
}