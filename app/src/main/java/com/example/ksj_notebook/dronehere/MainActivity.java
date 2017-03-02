package com.example.ksj_notebook.dronehere;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
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



    public static Context getContext() {
        return context;
    }

    public interface onKeyBackPressedListener {
        public void onBack();
    }
    private onKeyBackPressedListener mOnKeyBackPressedListener;

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


        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        NetworkCheckManager receiver = new NetworkCheckManager(this);
        registerReceiver(receiver, filter);
        context = this;
        setContentView(R.layout.activity_main);
        gps_check();
        getSupportFragmentManager().beginTransaction().replace(R.id.tab_frame, new TabHere()).commit();
        /*
        frameLayout=findViewById(android.R.id.tabcontent);

        tabHost = (FragmentTabHost)findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        */
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.status));
        }
        /*
        final ImageView tab1 = new ImageView(this);
        tab1.setImageResource(R.drawable.tab1selector);
        final ImageView tab2 = new ImageView(this);
        tab2.setImageResource(R.drawable.tab2selector);
        final ImageView tab3 = new ImageView(this);
        tab3.setImageResource(R.drawable.tab3selector);
        ImageView tab4 = new ImageView(this);
        tab4.setImageResource(R.drawable.tab4selector);
        */
        /*
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(tab1), TabHere.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2"), TabGather.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab3"), TabNews.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab4"), TabDrone.class, null);
        */
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        //toolbar=(Toolbar)findViewById(R.id.main_toolbar);
        //main_title=(TextView)findViewById(R.id.main_title);
        /*
        toolbar_btn=(Button)findViewById(R.id.toolbar_btn);
        toolbar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(MainActivity.this, "미구현 입니다.", Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });*/
        /*
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("tab1")){
                    main_title.setText("드론 Here");
                }else if(tabId.equals("tab2")){
                    main_title.setText("드론 장소");
                }else if(tabId.equals("tab3")){
                    main_title.setText("드론 뉴스");
                }else{
                    main_title.setText("드론 백과");
                }
            }
        });*/
        LogWrapper.d(TAG, "debug log");
        LogWrapper.e(TAG, "error log");
   }

    public void openHamberger(){
        drawerLayout.openDrawer(Gravity.LEFT);
    }
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
    }


    // 권한 확인
    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    // 권한 확인
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
