package com.santamaria.dronehere.Drawer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.santamaria.dronehere.BaseActivity;
import com.santamaria.dronehere.MainActivity;
import com.santamaria.dronehere.MyApplication;
import com.santamaria.dronehere.R;
import com.santamaria.dronehere.data.DroneDB;
import com.santamaria.dronehere.data.DroneRecommendResult;
import com.santamaria.dronehere.data.DroneSearchResult;
import com.santamaria.dronehere.data.Member;
import com.santamaria.dronehere.data.MemberResult;
import com.santamaria.dronehere.login.DbSearchViewHolder;
import com.santamaria.dronehere.login.DronePickDialogAdapter;
import com.santamaria.dronehere.manager.NetworkManager;
import com.santamaria.dronehere.manager.PropertyManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/** (내 정보) 프로필 수정 페이지 **/

public class Drawer_fix extends BaseActivity {

    static int previous_drone_cnt;
    int current_drone_cnt;
    static boolean ok_btn = false;
    static boolean back_btn = false;
    static boolean first_init = true;
    static RadioButton lastChecked = null;
    Member member;
    EditText nickname;
    ImageView im;
    Vibrator vibrator;
    LinearLayout adddrone;
    Button okokok;
    RecyclerView re;
    Drawer_fix_adapter adap1;
    Toolbar mToolbar;
    InputMethodManager inputMethodManager;
    static int mCheckedPostion;
    private String mem_id = PropertyManager.getInstance().getId();

    @Override
    public void onBackPressed() {
            back_btn = true;
        if ( previous_drone_cnt != current_drone_cnt){
            String dr_select;
            List<String> dr_delete2 = new ArrayList();
            for (int i = 0; i < current_drone_cnt - previous_drone_cnt; i++) {
                dr_delete2.add(member.getMem_drone().get(previous_drone_cnt+i).getDr_name());
            }
            if (member.getMem_drone().isEmpty() != true) {
                dr_select = member.getMem_drone().get(0).getDr_name();
            } else {
                dr_select = null;
            }
            /** 입력한 값으로 수정! **/
            NetworkManager.getInstance().getFix2(MyApplication.getContext(), mem_id, nickname.getText().toString(), dr_delete2, dr_select, new NetworkManager.OnResultListener() {
                //member.getMem_name()
                @Override
                public void onSuccess(Request request, Object result) {
                    finish();
                }

                @Override
                public void onFail(Request request, IOException exception) {
                }
            });
        }
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_fix);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.status2));
        }
        inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        nickname = (EditText) findViewById(R.id.nickname);
        im = (ImageView) findViewById(R.id.droneimage);

        adddrone = (LinearLayout) findViewById(R.id.adddrone);
        okokok = (Button) findViewById(R.id.okokok);
        re = (RecyclerView) findViewById(R.id.re);

        adap1 = new Drawer_fix_adapter(im);


        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.setting_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//title hidden
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back icon
        mToolbar.setTitle("내 정보 수정");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setNavigationIcon(R.drawable.edit_back_btn);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() { //뒤로가기
            @Override
            public void onClick(View view) {
                back_btn = true;
                if ( previous_drone_cnt != current_drone_cnt){
                    String dr_select;
                    List<String> dr_delete2 = new ArrayList();
                    for (int i = 0; i < current_drone_cnt - previous_drone_cnt; i++) {
                        dr_delete2.add(member.getMem_drone().get(previous_drone_cnt+i).getDr_name());
                    }
                    if (member.getMem_drone().isEmpty() != true) {
                        dr_select = member.getMem_drone().get(0).getDr_name();
                    } else {
                        dr_select = null;
                    }
                    /** 입력한 값으로 수정! **/
                    NetworkManager.getInstance().getFix2(MyApplication.getContext(), mem_id, nickname.getText().toString(), dr_delete2, dr_select, new NetworkManager.OnResultListener() {
                        //member.getMem_name()
                        @Override
                        public void onSuccess(Request request, Object result) {
                            finish();
                        }

                        @Override
                        public void onFail(Request request, IOException exception) {
                        }
                    });
                } else {
                    finish();
                }
            }
        });

        NetworkManager.getInstance().getFix(MyApplication.getContext(), mem_id, new NetworkManager.OnResultListener<MemberResult>() {

            @Override
            public void onSuccess(Request request, MemberResult result) {

                member = result.getResult();
                previous_drone_cnt = member.getMem_drone().size();
                current_drone_cnt = previous_drone_cnt;
                nickname.setText(member.getMem_name());
                adap1.setMem(member, getApplicationContext());

                GlideUrl url = new GlideUrl(member.getDr_photo());
                Glide.with(MyApplication.getContext())
                        .load(url)
                        .into(im);
                re.setAdapter(adap1);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                re.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                re.setLayoutManager(layoutManager);

            }

            @Override
            public void onFail(Request request, IOException exception) {

            }

        });

        adddrone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(member.getMem_drone().size() < 4) {
                    CustomDialog7 dialog = new CustomDialog7(Drawer_fix.this);
                    dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "드론은 4개 이하만 소유 가능합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        okokok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nickname.getText().toString() == "") {
                    Toast.makeText(Drawer_fix.this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
                } else if (nickname.getText().toString().length() < 2 || nickname.getText().toString().length() > 5) {
                    Toast.makeText(getApplicationContext(), "닉네임(2-5자)형식에 맞게 입력해주세요", Toast.LENGTH_SHORT).show();
                    vibrator.vibrate(100);
                } else if (adap1.drone_cnt - adap1.check_cnt == 0) {
                    Toast.makeText(getApplicationContext(), "소유 드론이 1개는 있어야합니다.", Toast.LENGTH_SHORT).show();
                    vibrator.vibrate(100);
                } else {
                    String dr_select;
                    List<String> dr_delete2 = new ArrayList();
                    List<Integer> dr_delete1 = new ArrayList(adap1.mapp);

                    for (int i = 0; i < dr_delete1.size(); i++) {
                        dr_delete2.add(member.getMem_drone().get(dr_delete1.get(i)).getDr_name());
                    }
                    if (member.getMem_drone().isEmpty() != true) {
                        dr_select = member.getMem_drone().get(mCheckedPostion).getDr_name();
                    } else {
                        dr_select = null;
                    }
                    /** 입력한 값으로 수정! **/
                    NetworkManager.getInstance().getFix2(MyApplication.getContext(), mem_id, nickname.getText().toString(), dr_delete2, dr_select, new NetworkManager.OnResultListener() {
                        //member.getMem_name()
                        @Override
                        public void onSuccess(Request request, Object result) {
                            ok_btn = true;
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFail(Request request, IOException exception) {
                        }
                    });
                }
            }
        });
    }
    /** 드론 추가용 목록(검색) 다이얼로그 **/
    class CustomDialog7 extends Dialog {

        EditText editText;
        RecyclerView recy;
        //Button nonono;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //  this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;/*|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;*/
            lpWindow.dimAmount = 0.8f;
            lpWindow.gravity = Gravity.CENTER;
            lpWindow.width = WindowManager.LayoutParams.MATCH_PARENT;
            lpWindow.height = WindowManager.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(lpWindow);

            setContentView(R.layout.dronepick_dialog);

            final DronePickDialogAdapter adap = new DronePickDialogAdapter();

            editText = (EditText) findViewById(R.id.droneseaa);
            recy = (RecyclerView) findViewById(R.id.drpick_recy);
            //nonono=(Button)findViewById(R.id.nonono);
            //nonono.setVisibility(View.GONE);

            /** 다이얼로그 키보드 올라가면 안올라가게 설정!**/
/*
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    }
                }
            });
*/
            recy.setAdapter(adap);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recy.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            recy.setLayoutManager(layoutManager);

            // inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0); // 키보드 내리기
            adap.setOnItemClickListener(new DronePickDialogAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(DbSearchViewHolder holder, final View view, DroneDB s, int position) {
                    for (int i = 0; i < member.getMem_drone().size(); i++) {
                        if (member.getMem_drone().get(i).getDr_name().equals(s.getDr_name())) {
                            Toast.makeText(MyApplication.getContext(), "이미 등록된 드론입니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    /** 아이템 클릭하면 해당 아이템 id값(드론 id)을 해당 유저에 추가함. **/
                    NetworkManager.getInstance().getDadd(MyApplication.getContext(), mem_id, s.get_id(), new NetworkManager.OnResultListener<DroneSearchResult>() {
                        @Override
                        public void onSuccess(Request request, DroneSearchResult result) {
                            /** 유저 아이디 보내서 Member 정보 받아옴 **/
                            NetworkManager.getInstance().getFix(MyApplication.getContext(), mem_id, new NetworkManager.OnResultListener<MemberResult>() {
                                @Override
                                public void onSuccess(Request request, MemberResult result) {
                                    member = result.getResult();
                                    adap1.drone_cnt++; // 드론 목록의 드론갯수 +1
                                    current_drone_cnt++;
                                    adap1.setMem(member, getApplicationContext());
                                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0); // 키보드 내리기
                                    dismiss();
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


            /** 드론 검색바 부분 **/
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    /**  **/
                    String get = "" + editText.getText();
                    if (editText.getText().length() == 0) {
                        NetworkManager.getInstance().getDroneRecommendRate(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
                            @Override
                            public void onSuccess(Request request, DroneRecommendResult result) {
                                adap.setDb3(result.getResult());
                            }

                            @Override
                            public void onFail(Request request, IOException exception) {

                            }
                        });
                    } else {
                        NetworkManager.getInstance().getDroneSearch(MyApplication.getContext(), get, new NetworkManager.OnResultListener<DroneSearchResult>() {

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

        public CustomDialog7(Context context) {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);
        }
    }

}