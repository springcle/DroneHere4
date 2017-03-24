package com.santamaria.dronehere.Dronedb;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.santamaria.dronehere.BaseActivity;
import com.santamaria.dronehere.MyApplication;
import com.santamaria.dronehere.R;
import com.santamaria.dronehere.manager.NetworkManager;
import com.santamaria.dronehere.manager.PropertyManager;

import java.io.IOException;

import okhttp3.Request;

public class DroneDetailReviewWrite extends BaseActivity {

    //TextView drrv_name;
    RatingBar drrv_ratingbar;
    EditText drrv_edit;
    Button drrv_btn;
    String dr_id;
    Vibrator vibrator;
    Toolbar mToolbar;

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
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // drrv_name = (TextView) findViewById(R.id.drrv_name);
        drrv_ratingbar = (RatingBar) findViewById(R.id.drrv_ratingbar);
        drrv_btn = (Button) findViewById(R.id.drrv_btn);

        drrv_edit = (EditText) findViewById(R.id.drrv_edit);
        drrv_edit.setHint("내용을 입력해 주세요 (40자 제한)");
        drrv_edit.setTypeface(Typeface.DEFAULT);

        //   drrv_name.setText(getIntent().getStringExtra("dr_name"));

        mToolbar = (Toolbar) findViewById(R.id.setting_toolbar);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//title hidden
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back icon
        mToolbar.setTitle(getIntent().getStringExtra("dr_name")); //드론 상세페이지의 드론이름을 인텐트로 받아와서 타이틀에 넣어줌.
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setNavigationIcon(R.drawable.edit_back_btn);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() { //뒤로가기
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        dr_id = getIntent().getStringExtra("dr_id");
        drrv_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (drrv_edit.getText().toString().length() > 40 && (drrv_edit.getText().toString().isEmpty()) != true) {
                    Toast.makeText(getApplicationContext(), "40자 이하로 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    vibrator.vibrate(100);
                }
            }
        });
        drrv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mem_id = PropertyManager.getInstance().getId();
                if (mem_id != "") {
                    if ((drrv_edit.getText().toString().isEmpty()) != true && drrv_edit.getText().toString().length() <= 40) {

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
                        vibrator.vibrate(100);
                    } else {
                        Toast.makeText(getApplicationContext(), "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        vibrator.vibrate(100);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "로그인 후 이용이 가능합니다.", Toast.LENGTH_SHORT).show();
                    vibrator.vibrate(100);
                }

            }
        });
    }
}
