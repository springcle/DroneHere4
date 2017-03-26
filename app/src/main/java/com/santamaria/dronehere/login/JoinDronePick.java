package com.santamaria.dronehere.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.santamaria.dronehere.BaseActivity;
import com.santamaria.dronehere.MainActivity;
import com.santamaria.dronehere.MyApplication;
import com.santamaria.dronehere.R;
import com.santamaria.dronehere.data.DroneDB;
import com.santamaria.dronehere.data.DroneRecommendResult;
import com.santamaria.dronehere.data.DroneSearchResult;
import com.santamaria.dronehere.data.UserLoginResult;
import com.santamaria.dronehere.manager.NetworkManager;
import com.santamaria.dronehere.manager.PropertyManager;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Request;

public class JoinDronePick extends BaseActivity {

    Vibrator vibrator;
    EditText nick;
    //ToggleButton btn1;
    Button btn2;
    ImageButton btn3;

    String mem_pass;
    String mem_name;
    String mem_email;
    String dr_id = " ";
    String dr_name;
    String dr_weight1;

    int cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone_pick);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.status2));
        }
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        nick=(EditText)findViewById(R.id.join_nick);
        btn2=(Button)findViewById(R.id.search);
        btn3=(ImageButton)findViewById(R.id.joinjoin);

        mem_email=getIntent().getStringExtra("email");
        mem_pass=getIntent().getStringExtra("pass");

        nick.setHint(R.string.edit_text_nick);
        /** 에디트 텍스트 폰트 미적용 **/
        nick.setTypeface(Typeface.DEFAULT);

        nick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str1 = nick.getText().toString();
                if (str1.isEmpty() == true) {
                    nick.setBackgroundResource(R.drawable.b_04_1);
                } else nick.setBackgroundResource(R.drawable.b_04);
            }
        });
/*
        btn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked==true){
                    cnt=2;
                    btn3.setVisibility(View.VISIBLE);
                    btn2.setVisibility(View.INVISIBLE);
                }else{
                    btn3.setVisibility(View.INVISIBLE);
                    btn2.setVisibility(View.VISIBLE);
                }

            }
        });
*/
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog3 dialog = new CustomDialog3(JoinDronePick.this);
                dialog.show();
            }
        });

        /* 2017-03-06
        1. 닉네임 입력하면 이메일주소 들어가지 않도록 수정.
        2. 닉네임 공백 비허용
        3. 드론 무조건 선택하도록 변경
        */

        btn3.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                String text;
                text = nick.getText().toString();
                if (("" + nick.getText()).equals("")) {
                    Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
                    vibrator.vibrate(100);
                }
                else if(text.length() < 2  || text.length() > 5){
                    Toast.makeText(getApplicationContext(), "닉네임(2-5자)형식에 맞게 입력해주세요", Toast.LENGTH_SHORT).show();
                    vibrator.vibrate(100);
                }
                else if(dr_id.equals(" ")){
                    Toast.makeText(getApplicationContext(), "드론을 추가해주세요", Toast.LENGTH_SHORT).show();
                    vibrator.vibrate(100);
                }
                else {
                    mem_name = nick.getText().toString();
                    NetworkManager.getInstance().getJoin3(MyApplication.getContext(), mem_pass, mem_name, mem_email, dr_id, new NetworkManager.OnResultListener() {

                        @Override
                        public void onSuccess(Request request, Object result) {

                            NetworkManager.getInstance().getLogin(MyApplication.getContext(), mem_email, mem_pass, new NetworkManager.OnResultListener<UserLoginResult>() {

                                @Override
                                public void onSuccess(Request request, UserLoginResult result) {

                                    PropertyManager.getInstance().setId(result.getResult().getMem_id());
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }

                                @Override
                                public void onFail(Request request, IOException exception) {

                                }
                            });

                        }

                        @Override
                        public void onFail(Request request, IOException exception) {

                        }
                    });

                }
            }
        });
    }

    class CustomDialog3 extends Dialog {

        EditText editText;
        RecyclerView recy;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            lpWindow.dimAmount = 0.8f;
            lpWindow.gravity= Gravity.CENTER;
            lpWindow.width=WindowManager.LayoutParams.MATCH_PARENT;
            lpWindow.height=WindowManager.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(lpWindow);

            setContentView(R.layout.dronepick_dialog);

            final DronePickDialogAdapter adap=new DronePickDialogAdapter();

            editText=(EditText)findViewById(R.id.droneseaa);
            recy=(RecyclerView)findViewById(R.id.drpick_recy);

            recy.setAdapter(adap);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recy.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            recy.setLayoutManager(layoutManager);

            adap.setOnItemClickListener(new DronePickDialogAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(DbSearchViewHolder holder, View view, DroneDB s, int position) {
                    dr_id=s.get_id();
                    //btn1.setVisibility(View.INVISIBLE);
                    btn2.setText(s.getDr_name());
                    btn2.setBackgroundResource(R.drawable.b_11_2);
                    btn3.setVisibility(View.VISIBLE);
                    dismiss();
                }
            });


            /** 다이얼로그 시작되면 바로 추천순으로 드론 출력 **/
            NetworkManager.getInstance().getDroneRecommendRate(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
                @Override
                public void onSuccess(Request request, DroneRecommendResult result) {
                    adap.setDb3(result.getResult());
                }

                @Override
                public void onFail(Request request, IOException exception) {

                }
            });



            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String get=""+editText.getText();
                    if(get.equals("")){
                        adap.setDb3(new ArrayList<DroneDB>());
                    }else{
                        NetworkManager.getInstance().getDroneSearch(MyApplication.getContext(),get ,new NetworkManager.OnResultListener<DroneSearchResult>() {

                            @Override
                            public void onSuccess(Request request, DroneSearchResult result) {
                                /** 에디트텍스트 검색 시 필터 된 드론정보 서버로 부터 받아와서 저장 **/
                                adap.setDb3(result.getResult());
                            }

                            @Override
                            public void onFail(Request request, IOException exception) {

                            }

                        });
                    }
                }
            });

        }

        public CustomDialog3(Context context) {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);
        }
    }


/*
    class CustomDialog5 extends Dialog {

        EditText drn_name;
        Button down12;
        Button up12;
        Button okk;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            lpWindow.dimAmount = 0.8f;
            lpWindow.gravity= Gravity.CENTER;
            lpWindow.width=WindowManager.LayoutParams.MATCH_PARENT;
            lpWindow.height=WindowManager.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(lpWindow);

            setContentView(R.layout.nodrone_pick);

            drn_name=(EditText)findViewById(R.id.drn_name);
            down12=(Button)findViewById(R.id.down12);
            up12=(Button)findViewById(R.id.up12);
            okk=(Button)findViewById(R.id.okk);



            down12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        down12.setBackgroundResource(R.drawable.b_16_2);
                        up12.setBackgroundResource(R.drawable.b_17_1);
                        dr_weight1="0";
                }
            });
            up12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dr_weight1="1";
                    up12.setBackgroundResource(R.drawable.b_17_2);
                    down12.setBackgroundResource(R.drawable.b_16_1);
                }
            });

            okk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(drn_name.getText().toString()==""){
                        Toast.makeText(MyApplication.getContext(), "드론이름을 입력하세요", Toast.LENGTH_SHORT).show();
                    }else{
                        dr_name=drn_name.getText().toString();
                        btn3.setVisibility(View.VISIBLE);
                        //btn1.setVisibility(View.INVISIBLE);
                        btn2.setVisibility(View.INVISIBLE);
                        cnt=4;
                        dismiss();
                    }

                }
            });


        }

        public CustomDialog5(Context context) {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);
        }
    }
*/

}
