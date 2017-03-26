package com.santamaria.dronehere.Drawercontent;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.santamaria.dronehere.BaseActivity;
import com.santamaria.dronehere.MyApplication;
import com.santamaria.dronehere.R;
import com.santamaria.dronehere.data.Dc1Result;
import com.santamaria.dronehere.manager.NetworkManager;
import com.santamaria.dronehere.manager.PropertyManager;

import java.io.IOException;

import okhttp3.Request;

public class Dc2 extends BaseActivity {

    Dc2Adapter adp;
    RecyclerView recy;
    String mem_id= PropertyManager.getInstance().getId();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dc2);

        recy=(RecyclerView)findViewById(R.id.dc2recy);
        adp=new Dc2Adapter();

        recy.setAdapter(adp);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recy.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recy.setLayoutManager(layoutManager);


            NetworkManager.getInstance().getDc2(MyApplication.getContext(), mem_id, new NetworkManager.OnResultListener<Dc1Result>() {

                @Override
                public void onSuccess(Request request, Dc1Result result) {
                    adp.setdc11(result.getResult());

                }

                @Override
                public void onFail(Request request, IOException exception) {

                }

            });
    }
}
