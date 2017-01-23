package com.example.ksj_notebook.dronehere.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ksj_notebook.dronehere.MainActivity;
import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneDB;
import com.example.ksj_notebook.dronehere.data.DroneSearchResult;
import com.example.ksj_notebook.dronehere.data.Member;
import com.example.ksj_notebook.dronehere.data.MemberResult;
import com.example.ksj_notebook.dronehere.login.DbSearchViewHolder;
import com.example.ksj_notebook.dronehere.login.DronePickDialogAdapter;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;
import com.example.ksj_notebook.dronehere.manager.PropertyManager;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Request;

/**
 * Created by NAKNAK on 2017-01-15.
 */
public class AddDrone extends Dialog{
    Member member;
    EditText editText;
    RecyclerView recy;
    Button nonono;
    Activity activity;
    private String mem_id = PropertyManager.getInstance().getId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        NetworkManager.getInstance().getFix(MyApplication.getContext(), mem_id,new NetworkManager.OnResultListener<MemberResult>() {

            @Override
            public void onSuccess(Request request, MemberResult result) {
                member = result.getResult();
            }
            @Override
            public void onFail(Request request, IOException exception) {

            }

        });

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
        nonono=(Button)findViewById(R.id.nonono);
        nonono.setVisibility(View.GONE);

        recy.setAdapter(adap);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recy.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recy.setLayoutManager(layoutManager);


        adap.setOnItemClickListener(new DronePickDialogAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(DbSearchViewHolder holder, View view, DroneDB s, int position) {
                for(int i=0;i<member.getMem_drone().size();i++){
                    if(member.getMem_drone().get(i).getDr_name().equals(s.getDr_name())){
                        Toast.makeText(MyApplication.getContext(), "이미 등록된 드론입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                NetworkManager.getInstance().getDadd(MyApplication.getContext(),mem_id,s.get_id() ,new NetworkManager.OnResultListener<DroneSearchResult>() {
                    @Override
                    public void onSuccess(Request request, DroneSearchResult result) {
                        NetworkManager.getInstance().getFix(MyApplication.getContext(), mem_id,new NetworkManager.OnResultListener<MemberResult>() {
                            @Override
                            public void onSuccess(Request request, MemberResult result) {
                                member=result.getResult();
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                getContext().startActivity(intent);
                                dismiss();
                                activity.finish();
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

    public AddDrone(Context context,Activity activity) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.activity = activity;
    }
}
