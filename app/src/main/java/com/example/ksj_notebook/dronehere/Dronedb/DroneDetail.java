package com.example.ksj_notebook.dronehere.Dronedb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ksj_notebook.dronehere.BaseActivity;
import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneDB;
import com.example.ksj_notebook.dronehere.data.DroneDetailResult;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Request;

//TODO ImageList가 adapter -> holderHeader로 이동시켜야 되는데 안되는듯? 확인 필요.

public class DroneDetail extends BaseActivity implements ViewPager.OnPageChangeListener {

    DroneDetailAdapter dtAdapter;
    RecyclerView recyclerView;
    ImageButton dt_float;
    String _id;
    DroneDB db;
    TextView dbtitle;

    public static ArrayList<String> imageList = new ArrayList<>();



    /*    DetailPagerAdapter detailPagerAdapter;
    ViewPager mViewPager;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone_detail);
        dtAdapter=new DroneDetailAdapter();
        recyclerView=(RecyclerView)findViewById(R.id.dbdetailrecy);
        dt_float=(ImageButton)findViewById(R.id.dtdt);
        dbtitle=(TextView)findViewById(R.id.dbtitle);



        _id=getIntent().getStringExtra("_id");
        recyclerView.setAdapter(dtAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);




        dt_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DroneDetailReviewWrite.class);
                intent.putExtra("dr_name", db.getDr_name());
                intent.putExtra("dr_id",db.get_id());
                startActivity(intent);
            }
        });



    }


    @Override
    public void onResume() {
        super.onResume();

        NetworkManager.getInstance().getDroneDetail(MyApplication.getContext(),_id,new NetworkManager.OnResultListener<DroneDetailResult>() {
            @Override
            public void onSuccess(Request request, DroneDetailResult result) {
                db=result.getResult();
                dbtitle.setText(db.getDr_name());
                setTitle(db.getDr_name());
                dtAdapter.setDb(db);

                setImageList(db.getDr_photoArr());
                //DetailPagerAdapter.notifyDataSetChanged();
/*
                mViewPager = (ViewPager) findViewById(R.id.detail_imageView);
        *//*mViewPager.addOnPageChangeListener(this);*//*
                detailPagerAdapter = new DetailPagerAdapter();
                mViewPager.setAdapter(detailPagerAdapter);*/
            }

            @Override
            public void onFail(Request request, IOException exception) {

            }


        });


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
