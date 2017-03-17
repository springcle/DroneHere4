package com.example.ksj_notebook.dronehere.Dronedb;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;
import com.example.ksj_notebook.dronehere.manager.PropertyManager;

import java.io.IOException;

import okhttp3.Request;

public class DroneDetailReviewWrite extends AppCompatActivity {

    TextView drrv_name;
    RatingBar drrv_ratingbar;
    EditText drrv_edit;
    Button drrv_btn;
    String dr_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_write_review);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.status2));
        }
        drrv_name=(TextView)findViewById(R.id.drrv_name);
        drrv_ratingbar=(RatingBar)findViewById(R.id.drrv_ratingbar);
        drrv_btn=(Button)findViewById(R.id.drrv_btn);

        drrv_edit=(EditText)findViewById(R.id.drrv_edit);
        drrv_edit.setHint(R.string.edit_text_hint5);

        drrv_name.setText(getIntent().getStringExtra("dr_name"));
        dr_id=getIntent().getStringExtra("dr_id");

        drrv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mem_id= PropertyManager.getInstance().getId();
                NetworkManager.getInstance().getWriteDbRv(MyApplication.getContext(),dr_id,mem_id ,drrv_ratingbar.getRating(),""+drrv_edit.getText(),new NetworkManager.OnResultListener() {

                    @Override
                    public void onSuccess(Request request, Object result) {

                    }

                    @Override
                    public void onFail(Request request, IOException exception) {

                    }
                });

            finish();

            }
        });
    }
}
