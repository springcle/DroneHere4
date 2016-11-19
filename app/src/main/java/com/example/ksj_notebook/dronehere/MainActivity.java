package com.example.ksj_notebook.dronehere;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ksj_notebook.dronehere.Dronedb.TabDrone;
import com.example.ksj_notebook.dronehere.Gathering.TabGather;
import com.example.ksj_notebook.dronehere.Here.TabHere;
import com.example.ksj_notebook.dronehere.News.TabNews;

public class MainActivity extends AppCompatActivity {

    private BackPressCloseHandler backPressCloseHandler;
    FragmentTabHost tabHost;
    NavigationView navigationView;
    DrawerLayout drawer;
    View frameLayout;
    Toolbar toolbar;
    DrawerLayout drawerLayout;

    TextView main_title;

    Button toolbar_btn;

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressCloseHandler = new BackPressCloseHandler(this);
        frameLayout=findViewById(android.R.id.tabcontent);

        tabHost = (FragmentTabHost)findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.status));
        }

        final ImageView tab1 = new ImageView(this);
        tab1.setImageResource(R.drawable.tab1selector);
        final ImageView tab2 = new ImageView(this);
        tab2.setImageResource(R.drawable.tab2selector);
        final ImageView tab3 = new ImageView(this);
        tab3.setImageResource(R.drawable.tab3selector);
        ImageView tab4 = new ImageView(this);
        tab4.setImageResource(R.drawable.tab4selector);


        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(tab1), TabHere.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(tab2), TabGather.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator(tab3), TabNews.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator(tab4), TabDrone.class, null);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        toolbar=(Toolbar)findViewById(R.id.main_toolbar);
        main_title=(TextView)findViewById(R.id.main_title);

        toolbar_btn=(Button)findViewById(R.id.toolbar_btn);
        toolbar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(MainActivity.this, "미구현 입니다.", Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });


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
        });
   }
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

}
