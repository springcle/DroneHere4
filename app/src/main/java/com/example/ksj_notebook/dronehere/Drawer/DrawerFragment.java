package com.example.ksj_notebook.dronehere.Drawer;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.ksj_notebook.dronehere.Drawercontent.Dc4;
import com.example.ksj_notebook.dronehere.Drawercontent.Dc8;
import com.example.ksj_notebook.dronehere.Drawercontent.Dc9;
import com.example.ksj_notebook.dronehere.MainActivity;
import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DrawerResult;
import com.example.ksj_notebook.dronehere.data.DroneDB;
import com.example.ksj_notebook.dronehere.data.Member;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;
import com.example.ksj_notebook.dronehere.manager.PropertyManager;

import java.io.IOException;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerFragment extends Fragment {

    RecyclerView recyclerView;
    private String mem_id;
    Member mem;
    Context context;

    /** Hamberger Header (닉네임, 주력드론명, 에디트버튼 등)**/
    LinearLayout hamberger_header_background;
    TextView drawer_nick;
    TextView drone_name;
    ImageView drawer_image;
    Button edit; // 원 모양 에디트 버튼(드론사진)
    DroneDB user_drone;

    /** Hamberger Body (내활동내역, 비행가이드, 설정)**/
    LinearLayout my_activity_btn;
    LinearLayout flying_guide_btn;
    LinearLayout setting_btn;


    /** Hamberger Footer (회원약관, 프로그램정보)**/
    LinearLayout empty_layout;
    Button member_stipulation_btn;
    Button program_info_btn;

    public DrawerFragment() {
        // Required empty public constructor
    }

    public void setMem(Member mem, Context context){
        this.mem=mem;
        this.context=context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //adapter=new rawerAdapter();
        mem_id=PropertyManager.getInstance().getId();
        NetworkManager.getInstance().getDrawer(MyApplication.getContext(), mem_id, new NetworkManager.OnResultListener<DrawerResult>() {
            @Override
            public void onSuccess(Request request, DrawerResult result) {
                setMem(result.getResult(), getContext());
                init_event();
            }
            @Override
            public void onFail(Request request, IOException exception) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /**
        adapter=new DrawerAdapter();
        View view= inflater.inflate(R.layout.fragment_blank, container, false);

        recyclerView=(RecyclerView)view.findViewById(R.id.drawer_recy);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutFrozen(true);
        **/
        View view= inflater.inflate(R.layout.hamberger_bar, container, false);
        /** 햄버거 헤더 **/
        hamberger_header_background = (LinearLayout)view.findViewById(R.id.hamberger_header_background);
        drawer_nick=(TextView) view.findViewById(R.id.nickname);
        drone_name=(TextView) view.findViewById(R.id.drone_name);
        drawer_image=(ImageView) view.findViewById(R.id.circle_image);
        edit = (Button) view.findViewById(R.id.hamberger_edit_btn);
        /** 햄버거 바디 **/
        my_activity_btn = (LinearLayout) view.findViewById(R.id.my_activity_btn);
        flying_guide_btn = (LinearLayout) view.findViewById(R.id.flying_guide_btn);
        setting_btn = (LinearLayout) view.findViewById(R.id.setting_btn);
        /** 햄버거 푸터 **/
        empty_layout = (LinearLayout) view.findViewById(R.id.empty_layout);
        member_stipulation_btn = (Button) view.findViewById(R.id.member_stipulation_btn);
        program_info_btn = (Button) view.findViewById(R.id.program_info_btn);
         /*
        adapter.setOnLogoutClickListener(new DrawerAdapter.onLogoutClickListener() {
            @Override
            public void onLogoutClicked() {
                PropertyManager.getInstance().setId("");
                Toast.makeText(MyApplication.getContext(), "비회원으로 로그인 하려면 어플을 종료 후 다시 실행하세요.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getContext(), StartActivity.class));
                getActivity().finish();
            }
        });
        */
        /*
        adapter.setOnRefreshListener(new DrawerAdapter.onRefreshListener() {
            @Override
            public void onRefreshed() {
                NetworkManager.getInstance().getDrawer(MyApplication.getContext(), mem_id, new NetworkManager.OnResultListener<DrawerResult>() {
                    @Override
                    public void onSuccess(Request request, DrawerResult result) {
                        adapter.setMem(result.getResult(), getContext());
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onFail(Request request, IOException exception) {
                    }
                });
            }
        });
        */
        return view;
    }
    public void init_event(){
        /** 헤더 **/
        if(PropertyManager.getInstance().getId() != "") { // 회원 일 경우
            set_hamburger_Info(mem);
            // Circle Image(에디트 버튼)
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), Drawer_fix.class);
                    getContext().startActivity(intent);
                }
            });
        } else {
            edit.setEnabled(false);
            drawer_nick.setText("비회원");
        }
        // 빈 레이아웃(햄버거 바 뒤에 지도 스크롤 방지)
        hamberger_header_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        /** 바디 **/
        // 내 활동내역
        my_activity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "비활성화 중입니다.", Toast.LENGTH_SHORT).show();
            }
        });
        // 비행 가이드
        flying_guide_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(PropertyManager.getInstance().getId() == "")
                //    Toast.makeText(context, "비활성화 중입니다.", Toast.LENGTH_SHORT).show();
                //else {
                //    Intent intent = new Intent(context, Dc2.class);
                //    context.startActivity(intent);
                //}
                Intent intent = new Intent(context, Dc4.class);
                context.startActivity(intent);
            }
        });
        // 설정
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Drawer_etc.class);
                context.startActivity(intent);
            }
        });
        /** 푸터 **/
        // 빈 레이아웃(햄버거 바 뒤에 지도 스크롤 방지)
        empty_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        // 회원약관
        member_stipulation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getContext().startActivity(new Intent(MainActivity.getContext(), Dc8.class));
            }
        });
        // 프로그램정보
        program_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getContext().startActivity(new Intent(MainActivity.getContext(), Dc9.class));
            }
        });
    }

    public void set_hamburger_Info(Member member){
        drawer_nick.setText(member.getMem_name());
        if(member.getMem_drone() != null) {
            GlideUrl url = new GlideUrl(member.getDr_photo());
            Glide.with(MyApplication.getContext())
                    .load(url)
                    //.override(drawer_image.getMaxWidth(),drawer_image.getMaxHeight())
                    .into(drawer_image);
            user_drone = new DroneDB();
            user_drone = member.getMem_drone().get(0);
            drone_name.setText(user_drone.getDr_name());
        }
    }

    @Override
    public void onResume() {
        super.onResume();/*
        String mem_id=PropertyManager.getInstance().getId();
                        NetworkManager.getInstance().getDrawer(MyApplication.getContext(),mem_id, new NetworkManager.OnResultListener<DrawerResult>() {
                            @Override
                            public void onSuccess(Request request, DrawerResult result) {
                                adapter.setMem(result.getResult(),getContext());
                            }
                            @Override
                            public void onFail(Request request, IOException exception) {
                            }
                        });*/
    }
}
