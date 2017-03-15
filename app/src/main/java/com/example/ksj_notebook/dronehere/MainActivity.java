package com.example.ksj_notebook.dronehere;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.ksj_notebook.dronehere.Here.TabHere;
import com.example.ksj_notebook.dronehere.manager.NetworkCheckManager;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_PERMISSION = 2;
    FragmentTabHost tabHost;
    View frameLayout;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    private static Context context;
    TextView main_title;
    Button toolbar_btn;

    /** 로그아웃 시, 설정 창(Drawer_etc)과 메인액티비티까지 finish() 해줘야 하므로, Drawer_etc 액티비티에서 메인액티비티를 사용하기 위함 **/
    public static Activity mainactivity;

    public static Context getContext() {
        return context;
    }

    /** back키 overriding **/
    private onKeyBackPressedListener mOnKeyBackPressedListener;
    public interface onKeyBackPressedListener {
        public void onBack();
    }
    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener) {
        mOnKeyBackPressedListener = listener;
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else if (mOnKeyBackPressedListener != null) {
            mOnKeyBackPressedListener.onBack();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        mainactivity = MainActivity.this;

        /** 네트워크 체크 리스너 **/
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        NetworkCheckManager receiver = new NetworkCheckManager(this);
        registerReceiver(receiver, filter);

        context = this;
        setContentView(R.layout.activity_main);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);

        gps_check();

        getSupportFragmentManager().beginTransaction().replace(R.id.tab_frame, new TabHere()).commit();
        /** status바(최상단 바) **/
        int id = getResources().getIdentifier("config_enableTranslucentDecor", "bool", "android");
        if (id != 0 && getResources().getBoolean(id)) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.Transparency));
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
         }
   */
    /** 탭히어 프래그먼트에 햄버거 버튼이 있으므로, 탭히어에 사용하기위해 public 선언 **/
    public void openHamberger(){
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    /** 위치기능 on/off 체크 **/
    public void gps_check(){
        try {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    public void storage_check(){
        try {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != MockPackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /** 위치기능 사용 권한 확인 **/
    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    /** 위치기능 사용 권한 확인 **/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 1 &&
                    grantResults[0] == MockPackageManager.PERMISSION_GRANTED ) {
                // Success Stuff here

                //SharedPreferences.Editor editor = settings.edit();
                //editor.putBoolean("gps_permission",true);
                //editor.apply();
            }
            else{
                // Failure Stuff
            }
        }
    }
}
