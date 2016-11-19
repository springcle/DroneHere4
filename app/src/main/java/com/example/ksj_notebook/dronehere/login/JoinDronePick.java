package com.example.ksj_notebook.dronehere.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ksj_notebook.dronehere.MainActivity;
import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneDB;
import com.example.ksj_notebook.dronehere.data.DroneSearchResult;
import com.example.ksj_notebook.dronehere.data.LoginResult;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;
import com.example.ksj_notebook.dronehere.manager.PropertyManager;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Request;

public class JoinDronePick extends AppCompatActivity {

    EditText nick;
    ToggleButton btn1;
    Button btn2;
    Button btn3;

    String mem_pass;
    String mem_name;
    String mem_email;
    String dr_id;
    String dr_name;
    String dr_weight1;

    int cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone_pick);

        nick=(EditText)findViewById(R.id.join_nick);
        btn1=(ToggleButton)findViewById(R.id.searchNot);
        btn2=(Button)findViewById(R.id.search);
        btn3=(Button)findViewById(R.id.joinjoin);

        mem_email=getIntent().getStringExtra("email");
        mem_pass=getIntent().getStringExtra("pass");

        nick.setHint(R.string.edit_text_nick);

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

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog3 dialog=new CustomDialog3(JoinDronePick.this);
                dialog.show();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                if((""+nick.getText()).equals("")){
                    mem_name=mem_email;
                }else{
                    mem_name=nick.getText().toString();
                }

                if(cnt==2){

                    NetworkManager.getInstance().getJoin2(MyApplication.getContext(),mem_pass,mem_name,mem_email, new NetworkManager.OnResultListener() {

                        @Override
                        public void onSuccess(Request request, final Object result) {
                            NetworkManager.getInstance().getLogin(MyApplication.getContext(),mem_email,mem_pass, new NetworkManager.OnResultListener<LoginResult>() {

                                @Override
                                public void onSuccess(Request request, LoginResult result) {

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

                }else if(cnt==3){

                    NetworkManager.getInstance().getJoin3(MyApplication.getContext(),mem_pass,mem_name,mem_email,dr_id, new NetworkManager.OnResultListener() {

                        @Override
                        public void onSuccess(Request request, final Object result) {
                            NetworkManager.getInstance().getLogin(MyApplication.getContext(),mem_email,mem_pass, new NetworkManager.OnResultListener<LoginResult>() {



                                @Override
                                public void onSuccess(Request request, LoginResult result) {

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

                }else{

                    NetworkManager.getInstance().getJoin4(MyApplication.getContext(),mem_pass,mem_name,mem_email,dr_name,dr_weight1, new NetworkManager.OnResultListener() {

                        @Override
                        public void onSuccess(Request request, Object result) {


                            NetworkManager.getInstance().getLogin(MyApplication.getContext(),mem_email,mem_pass, new NetworkManager.OnResultListener<LoginResult>() {

                                @Override
                                public void onSuccess(Request request, LoginResult result) {

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
        Button nodrone;

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
            nodrone=(Button)findViewById(R.id.nonono);
            nodrone.setVisibility(View.VISIBLE);

            recy.setAdapter(adap);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recy.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            recy.setLayoutManager(layoutManager);

            nodrone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomDialog5 dialog=new CustomDialog5(JoinDronePick.this);
                    dialog.show();
                    dismiss();

                }
            });

            adap.setOnItemClickListener(new DronePickDialogAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(DbSearchViewHolder holder, View view, DroneDB s, int position) {
                    cnt=3;
                    dr_id=s.get_id();
                    btn1.setVisibility(View.INVISIBLE);
                    btn2.setText(s.getDr_name());
                    btn2.setBackgroundResource(R.drawable.b_11_2);
                    btn3.setVisibility(View.VISIBLE);
                    dismiss();
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
                        btn1.setVisibility(View.INVISIBLE);
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


}
