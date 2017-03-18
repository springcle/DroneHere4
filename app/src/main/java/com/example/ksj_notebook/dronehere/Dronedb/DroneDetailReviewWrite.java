package com.example.ksj_notebook.dronehere.Dronedb;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ksj_notebook.dronehere.BaseActivity;
import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;
import com.example.ksj_notebook.dronehere.manager.PropertyManager;

import java.io.IOException;

import okhttp3.Request;

public class DroneDetailReviewWrite extends BaseActivity {

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
        drrv_name = (TextView) findViewById(R.id.drrv_name);
        drrv_ratingbar = (RatingBar) findViewById(R.id.drrv_ratingbar);
        drrv_btn = (Button) findViewById(R.id.drrv_btn);

        drrv_edit = (EditText) findViewById(R.id.drrv_edit);
        drrv_edit.setHint("내용을 입력해 주세요 (40자 제한)");
        drrv_edit.setTypeface(Typeface.DEFAULT);

        drrv_name.setText(getIntent().getStringExtra("dr_name"));
        dr_id = getIntent().getStringExtra("dr_id");

        drrv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((drrv_edit.getText().toString().isEmpty()) != true && drrv_edit.getText().toString().length() <= 40) {
                    String mem_id = PropertyManager.getInstance().getId();
                    NetworkManager.getInstance().getWriteDbRv(MyApplication.getContext(), dr_id, mem_id, drrv_ratingbar.getRating(), "" + drrv_edit.getText(), new NetworkManager.OnResultListener() {
                        @Override
                        public void onSuccess(Request request, Object result) {
                            Toast.makeText(getApplicationContext(), "리뷰 작성 완료!", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFail(Request request, IOException exception) {
                        }
                    });
                } else if (drrv_edit.getText().toString().length() > 40 && (drrv_edit.getText().toString().isEmpty()) != true) {
                    Toast.makeText(getApplicationContext(), "40자 이하로 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}