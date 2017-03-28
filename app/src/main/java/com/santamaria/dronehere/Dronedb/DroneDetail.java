package com.santamaria.dronehere.Dronedb;

import android.content.Intent;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.santamaria.dronehere.BaseActivity;
import com.santamaria.dronehere.MyApplication;
import com.santamaria.dronehere.R;
import com.santamaria.dronehere.data.DroneDB;
import com.santamaria.dronehere.data.DroneDetailResult;
import com.santamaria.dronehere.manager.NetworkManager;
import com.santamaria.dronehere.manager.PropertyManager;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Request;



public class DroneDetail extends BaseActivity implements ViewPager.OnPageChangeListener {

    DroneDetailAdapter dtAdapter;
    RecyclerView recyclerView;
    ImageButton dt_float;
    String _id;
    DroneDB db;
    Toolbar mToolbar;
    String mem_id;
    public static ArrayList<String> imageList = new ArrayList<>();



    /*    DetailPagerAdapter detailPagerAdapter;
    ViewPager mViewPager;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone_detail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.status2));
        }

        mem_id = PropertyManager.getInstance().getId();

        dtAdapter = new DroneDetailAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.dbdetailrecy);
        dt_float = (ImageButton) findViewById(R.id.dtdt);


        _id = getIntent().getStringExtra("_id");
        recyclerView.setAdapter(dtAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);



      /*  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
            Outline outline = null;
            outline = new Outline();
            outline.setOval(0, 0, size, size);
            findViewById(R.id.dtdt).setOutline(outline);
        }
*/
        ViewOutlineProvider viewOutlineProvider = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewOutlineProvider = new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    // Or read size directly from the view's width/height
                    int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        outline.setOval(0, 0, size, size);
                    }
                }
            };
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dt_float.setOutlineProvider(viewOutlineProvider);
        }

        NetworkManager.getInstance().getDroneDetail(MyApplication.getContext(),_id,new NetworkManager.OnResultListener<DroneDetailResult>() {
            @Override
            public void onSuccess(Request request, DroneDetailResult result) {
                db=result.getResult();
                dtAdapter.setDb(db);
                setImageList(db.getDr_photoArr());
                mToolbar = (Toolbar) findViewById(R.id.setting_toolbar);
                setSupportActionBar(mToolbar);
                getSupportActionBar().setDisplayShowTitleEnabled(false);//title hidden
                getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back icon
                mToolbar.setNavigationIcon(R.drawable.edit_back_btn);
                mToolbar.setTitle(db.getDr_name());
                mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() { //뒤로가기
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                        finish();
                    }
                });
                if(mem_id != ""){
                    dt_float.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), DroneDetailReviewWrite.class);
                            intent.putExtra("dr_name", db.getDr_name());
                            intent.putExtra("dr_id", db.get_id());
                            startActivity(intent);
                        }
                    });
                } else if(mem_id == "") {
                    dt_float.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "로그인 후 리뷰작성이 가능합니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onFail(Request request, IOException exception) {
                if(mem_id != ""){
                    dt_float.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "서버 장애로 데이터를 받아 올 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if(mem_id == "") {
                    dt_float.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "로그인 후 리뷰작성이 가능합니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
       // itemcount.setText(String.valueOf(position + 1)+" / " + img_count);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setImageList(ArrayList<String> imageList) {// 어댑터로 셋 해줄 메소드 생성

        this.imageList = imageList;
    }

    public static ArrayList<String> getImageList() {
        return imageList;
    }
}
