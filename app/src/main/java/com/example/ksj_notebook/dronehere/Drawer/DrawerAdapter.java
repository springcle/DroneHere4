package com.example.ksj_notebook.dronehere.Drawer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneDB;
import com.example.ksj_notebook.dronehere.data.Member;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;
import com.example.ksj_notebook.dronehere.manager.PropertyManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by ksj_notebook on 2016-06-03.
 */
public class DrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_DRONE = 1;
    private static final int TYPE_FOOTER=2;

    onLogoutClickListener mListener;
    onRefreshListener refreshListener;
    Member mem;
    List<DroneDB> drlist;
    Context context;
    private String mem_id = PropertyManager.getInstance().getId();

    public interface onLogoutClickListener{
        public void onLogoutClicked();
    }
    public interface onRefreshListener{
        public void onRefreshed();
    }
    public void setOnLogoutClickListener(onLogoutClickListener listener){
        mListener = listener;
    }
    public void setOnRefreshListener(onRefreshListener listener){
        refreshListener = listener;
    }

    public void setMem(Member mem, Context context){
        this.mem=mem;
        this.context=context;
        drlist=mem.getMem_drone();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(PropertyManager.getInstance().getId() != "") {
            if (position == 0) {
                return TYPE_HEADER;
            } else if (position <= drlist.size()) {
                return TYPE_DRONE;
            } else return TYPE_FOOTER;
        } else {
            if (position == 0) {
                return TYPE_HEADER;
            }else return TYPE_FOOTER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);
                return new DrawerViewHolderHeader(headerView);
            case TYPE_DRONE:
                View drview = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_drone, parent, false);
                return new DrawerViewHolderDrone(drview);
            case TYPE_FOOTER:
                View footerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_footer, parent, false);
                return new DrawerViewHolderFooter(footerView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER :
                if(PropertyManager.getInstance().getId() != "") { // 회원 일 경우
                    ((DrawerViewHolderHeader) holder).setNick(mem);
                    ((DrawerViewHolderHeader)holder).drawer_logout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mListener != null) {
                                mListener.onLogoutClicked();
                            }
                        }
                    });
                    ((DrawerViewHolderHeader)holder).drawer_fix.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, Drawer_fix.class);
                            context.startActivity(intent);
                        }
                    });
                    /*
                    ((DrawerViewHolderHeader)holder).drawer_dr_plus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CustomDialog7 dialog=new CustomDialog7(MainActivity.getContext());
                            dialog.show();
                        }
                    });
                    */
                }
                else { // 비회원 일 경우(회원정보 수정, 로그아웃 기능 안보이게)
                    ((DrawerViewHolderHeader) holder).drawer_logout.setVisibility(View.INVISIBLE);
                    ((DrawerViewHolderHeader) holder).drawer_fix.setVisibility(View.INVISIBLE);
                    ((DrawerViewHolderHeader) holder).drawer_nick.setText("");
                    ((DrawerViewHolderHeader) holder).drawer_image.setImageResource(R.drawable.i_02);
                }
                break;
            case  TYPE_DRONE:
                ((DrawerViewHolderDrone) holder).setDrone(drlist.get(position-1));
                if(position != 1 && mem.getMem_drone().size() > 1) {
                    ((DrawerViewHolderDrone) holder).drawer_drname.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                String dr_name = ((Button) v).getText().toString();
                                List<String> dr_delete = new ArrayList<>();
                                NetworkManager.getInstance().getFix2(MyApplication.getContext(), mem_id, mem.getMem_name(), dr_delete, dr_name, new NetworkManager.OnResultListener() {
                                    @Override
                                    public void onSuccess(Request request, Object result) {
                                        if (refreshListener != null) {
                                            refreshListener.onRefreshed();
                                        }
                                        //Intent intent = new Intent(context, MainActivity.class);
                                        //context.startActivity(intent);
                                    }

                                    @Override
                                    public void onFail(Request request, IOException exception) {
                                    }
                                });
                        }
                    });
                }
                break;
            case TYPE_FOOTER :
                ((DrawerViewHolderFooter)holder).setFooter();

                //if(PropertyManager.getInstance().getId() == "") {
                    ((DrawerViewHolderFooter) holder).btn1.setTextColor(Color.GRAY);
                    ((DrawerViewHolderFooter) holder).btn2.setTextColor(Color.GRAY);
                    ((DrawerViewHolderFooter) holder).btn3.setTextColor(Color.GRAY);
                //}
                ((DrawerViewHolderFooter) holder).btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //if(PropertyManager.getInstance().getId() == "")
                            Toast.makeText(context, "비활성화 중입니다.", Toast.LENGTH_SHORT).show();
                        //else {
                        //    Intent intent = new Intent(context, Dc1.class);
                        //    context.startActivity(intent);
                        //}
                    }
                });

                ((DrawerViewHolderFooter) holder).btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //if(PropertyManager.getInstance().getId() == "")
                            Toast.makeText(context, "비활성화 중입니다.", Toast.LENGTH_SHORT).show();
                        //else {
                        //    Intent intent = new Intent(context, Dc2.class);
                        //    context.startActivity(intent);
                        //}
                    }
                });
                ((DrawerViewHolderFooter) holder).btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       //if(PropertyManager.getInstance().getId() == "")
                            Toast.makeText(context, "비활성화 중입니다.", Toast.LENGTH_SHORT).show();
                        //else {
                        //    Intent intent = new Intent(context, Dc3.class);
                        //    context.startActivity(intent);
                        //}
                    }
                });
                ((DrawerViewHolderFooter) holder).etc_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //기타 설정
                        Intent intent = new Intent(context,Drawer_etc.class);
                        context.startActivity(intent);
                        //if(PropertyManager.getInstance().getId() == "")
                        //else {
                        //    Intent intent = new Intent(context, Dc3.class);
                        //    context.startActivity(intent);
                        //}
                    }
                });
                /*
                ((DrawerViewHolderFooter)holder).btn4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, Dc4.class);
                        context.startActivity(intent);
                    }
                });
                ((DrawerViewHolderFooter)holder).btn5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, Dc5.class);
                        context.startActivity(intent);
                    }
                });
                ((DrawerViewHolderFooter)holder).btn6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //항공촬영지침
                        Intent intent = new Intent(context, Dc6.class);
                        context.startActivity(intent);
                    }
                });
                ((DrawerViewHolderFooter)holder).btn7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //국토부링크
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.molit.go.kr/USR/policyTarget/m_24066/dtl.jsp?idx=584"));
                        context.startActivity(intent);
                    }
                });
                ((DrawerViewHolderFooter)holder).btn8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //회원약관
                        Intent intent = new Intent(context, Dc8.class);
                        context.startActivity(intent);
                    }
                });
                ((DrawerViewHolderFooter)holder).btn9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //프로그램정보
                        Intent intent = new Intent(context, Dc9.class);
                        context.startActivity(intent);
                    }
                });
                */
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(PropertyManager.getInstance().getId() == "") return 2;
        if(mem==null) return 0;
        return mem.getMem_drone().size()+2;
    }
    /*
    class CustomDialog7 extends Dialog {

        EditText editText;
        RecyclerView recy;
        Button nonono;

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
            nonono=(Button)findViewById(R.id.nonono);
            nonono.setVisibility(View.GONE);

            recy.setAdapter(adap);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recy.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            recy.setLayoutManager(layoutManager);

            adap.setOnItemClickListener(new DronePickDialogAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(DbSearchViewHolder holder, View view, DroneDB s, int position) {
                    for(int i=0;i<mem.getMem_drone().size();i++){
                        if(mem.getMem_drone().get(i).getDr_name().equals(s.getDr_name())){
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
                                    Intent intent = new Intent(MainActivity.getContext(), MainActivity.class);
                                    MainActivity.getContext().startActivity(intent);
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

        public CustomDialog7(Context context) {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);
        }
    }
    */
}
