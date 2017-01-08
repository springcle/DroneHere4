package com.example.ksj_notebook.dronehere.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.ksj_notebook.dronehere.MainActivity;

/**
 * Created by NAKNAK on 2017-01-06.
 */
public class AppNetwork extends BroadcastReceiver {
    private Activity activity;

    public AppNetwork() {
        super();
    }
    public AppNetwork(Activity activity) {
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.getContext());
                        dialog.setTitle("Network Check");
                        dialog.setMessage("네트워크 실행 확인 후 재시도");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(MainActivity.getContext(), MainActivity.class);
                                MainActivity.getContext().startActivity(intent);
                            }
                        });
                        dialog.setNegativeButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                System.exit(0);
                            }
                        });
                        dialog.show();
                    }
                }
            } catch (Exception e) {
                Log.i("ULNetworkReceiver", e.getMessage());
            }
        }
    }
}