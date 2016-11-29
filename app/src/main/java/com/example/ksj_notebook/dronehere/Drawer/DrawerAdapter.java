package com.example.ksj_notebook.dronehere.Drawer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ksj_notebook.dronehere.Drawercontent.Dc4;
import com.example.ksj_notebook.dronehere.Drawercontent.Dc5;
import com.example.ksj_notebook.dronehere.Drawercontent.Dc6;
import com.example.ksj_notebook.dronehere.Drawercontent.Dc8;
import com.example.ksj_notebook.dronehere.Drawercontent.Dc9;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneDB;
import com.example.ksj_notebook.dronehere.data.Member;
import com.example.ksj_notebook.dronehere.manager.PropertyManager;

import java.util.List;

/**
 * Created by ksj_notebook on 2016-06-03.
 */
public class DrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface onLogoutClickListener{
        public void onLogoutClicked();
    }

    onLogoutClickListener mListener;
    public void setOnLogoutClickListener(onLogoutClickListener listener){
        mListener = listener;
    }
    Member mem;
    List<DroneDB> drlist;
    Context context;

    public void setMem(Member mem, Context context){
        this.mem=mem;
        this.context=context;
        drlist=mem.getMem_drone();
        notifyDataSetChanged();
    }
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_DRONE = 1;
    private static final int TYPE_FOOTER=2;


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
                            if(mListener != null){
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
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(PropertyManager.getInstance().getId() == "") return 2;
        if(mem==null) return 0;
        return mem.getMem_drone().size()+2;
    }
}
