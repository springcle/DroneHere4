package com.example.ksj_notebook.dronehere;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    LocationManager locationManager;
    private static final int REQUEST_CODE_PERMISSION = 2;
    boolean location_permission;
    Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        gps_check();
    }
    private void goLoginActivity() {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
    }
    private void gps_enable_check() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    startActivity(intent);
                    finish();
                } else{
                    gps_enable_check();
                }
            }
        }, 1000);
    }
    // GPS on/off 확인
    public void gps_check(){
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.gps_dialog);
            Button btn1 = (Button) dialog.findViewById(R.id.gps_ok_btn);
            Button btn2 = (Button) dialog.findViewById(R.id.gps_cancle_btn);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override  // 예 버튼
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                    gps_enable_check();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override  // 아니오버튼
                public void onClick(View v) {
                    dialog.dismiss();
                    System.exit(0);
                }
            });
            WindowManager.LayoutParams wm = dialog.getWindow().getAttributes();
            wm.width = 1450;
            wm.height = 1000;
            dialog.getWindow().setAttributes(wm);
            //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            //GPS 설정화면으로 이동
        } else {
            location_permission = checkLocationPermission();
            if(location_permission == true){
                goLoginActivity();
            } else if(location_permission == false){
                permission_check();
            }
        }
    }
    public void permission_check(){
        try {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != MockPackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
                // If any permission above not allowed by user, this condition will execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // 권한 확인 체크
    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    // 권한 확인 후 진행사항
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 1 &&
                    grantResults[0] == MockPackageManager.PERMISSION_GRANTED ) {
                // Success Stuff here
                goLoginActivity();
            }
            else{
                // Failure Stuff
                Toast.makeText(getApplicationContext(), "GPS 권한이 승인되지 않았습니다.",Toast.LENGTH_SHORT).show();
                System.exit(0);
            }
        }
    }
}





