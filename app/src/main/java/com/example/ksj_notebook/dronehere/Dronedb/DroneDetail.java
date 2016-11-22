package com.example.ksj_notebook.dronehere.Dronedb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneDB;
import com.example.ksj_notebook.dronehere.data.DroneDetailResult;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;

import java.io.IOException;

import okhttp3.Request;

public class DroneDetail extends AppCompatActivity {

    DroneDetailAdapter dtAdapter;
    RecyclerView recyclerView;
    ImageButton dt_float;
    String _id;
    DroneDB db;

    TextView dbtitle;
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
            }

            @Override
            public void onFail(Request request, IOException exception) {

            }


        });


    }
}
