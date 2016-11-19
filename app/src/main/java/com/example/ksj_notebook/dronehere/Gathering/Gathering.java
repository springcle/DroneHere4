package com.example.ksj_notebook.dronehere.Gathering;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.GatherResult;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;

import java.io.IOException;

import okhttp3.Request;

public class Gathering extends AppCompatActivity {

    com.example.ksj_notebook.dronehere.data.Gathering gathering;

    RecyclerView recyclerView;
    GatheringAdapter adapter;
    GatheringAdapterImage imageAdapter2;
    ViewPager pager2;

    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView gt_nic;

    Button write;

    String gt_id;
    String gt;
    TextView gt_titl;
    TextView dda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gath_review);

        write=(Button)findViewById(R.id.gath_rv_write);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        //  toolbar=(Toolbar)view.findViewById(R.id.toolbar);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsingToolbarLayout1);
        pager2=(ViewPager)findViewById(R.id.pager2);
        gt_nic=(TextView)findViewById(R.id.gt_nic);
        gt_titl=(TextView) findViewById(R.id.gt_titl);
        dda=(TextView)findViewById(R.id.gt_dday);

        adapter=new GatheringAdapter();
        imageAdapter2=new GatheringAdapterImage();


        gt=getIntent().getStringExtra("gt"); ///나중에 타이틀바
        gt_id=getIntent().getStringExtra("gtid");
        gt_titl.setText(gt);

        pager2.setAdapter(imageAdapter2);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);



        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),GatheringWriteReview.class);
                intent.putExtra("gt_name",gathering.getGathe_name());
                intent.putExtra("gt_id",gathering.get_id());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        NetworkManager.getInstance().getGather(MyApplication.getContext(),gt_id, new NetworkManager.OnResultListener<GatherResult>(){

            @Override
            public void onSuccess(Request request, GatherResult result) {
                gathering=result.getResult();


                collapsingToolbarLayout.setTitle(gathering.getGathe_name());
                gt_nic.setText(gathering.getGathe_memname());
                dda.setText("D-"+gathering.getDday());

                imageAdapter2.setImage(gathering.getGathe_photo());
                adapter.setGathering(gathering);
            }

            @Override
            public void onFail(Request request, IOException exception) {

            }

        });
    }
}
