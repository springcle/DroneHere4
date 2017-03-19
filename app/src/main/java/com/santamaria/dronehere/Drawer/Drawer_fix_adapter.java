package com.santamaria.dronehere.Drawer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.santamaria.dronehere.MyApplication;
import com.santamaria.dronehere.R;
import com.santamaria.dronehere.data.DroneDB;
import com.santamaria.dronehere.data.DroneSearchResult;
import com.santamaria.dronehere.data.Member;
import com.santamaria.dronehere.manager.NetworkManager;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Request;

import static com.santamaria.dronehere.Drawer.Drawer_fix.lastChecked;

/**
 * Created by ksj_notebook on 2016-06-14.
 */
public class Drawer_fix_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 소유 드론과 삭제 체크 된 드론을 비교하여 소유 드론을 모두 지우지못하게끔 하는 카운트
     **/
    int check_cnt = 0;
    int drone_cnt = 0;
    ImageView imageView;
    Member mem;
    Context context;
    List<DroneDB> drlist;
    int CheckedPostion;
    Set<Integer> mapp = new HashSet<>();

    public Drawer_fix_adapter(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setMem(Member mem, Context context) {
        this.mem = mem;
        this.context = context;
        drone_cnt = mem.getMem_drone().size();
        drlist = mem.getMem_drone();
        /*
        if( drone_cnt != Drawer_fix.previous_drone_cnt){
            Log.e("선택새로고침previouscount", Drawer_fix.previous_drone_cnt+"");
            Log.e("선택새로고침dronecount", drone_cnt+"");
        } else  {*/
        notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.fix_lay, parent, false);
        return new Fix_Viewholder(view1);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((Fix_Viewholder) holder).setDrone1(drlist.get(position));
        ((Fix_Viewholder) holder).radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (lastChecked != null) {
                    Drawer_fix.lastChecked.setChecked(false);
                    Drawer_fix.lastChecked = ((Fix_Viewholder) holder).radioButton;
                    Drawer_fix.mCheckedPostion = position;
                } else {
                    ((Fix_Viewholder) holder).radioButton.setChecked(true);
                    Drawer_fix.lastChecked = ((Fix_Viewholder) holder).radioButton;
                }
            }
        });

        ((Fix_Viewholder) holder).radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /** 클릭 한 드론의 이름으로 서버에서 해당드론의 대표 이미지를 가져옴 **/
                NetworkManager.getInstance().getDroneSearch(MyApplication.getContext(), mem.getMem_drone().get(position).getDr_name(), new NetworkManager.OnResultListener<DroneSearchResult>() {
                    @Override
                    public void onSuccess(Request request, DroneSearchResult result) {
                        GlideUrl url = new GlideUrl(result.getResult().get(0).getDr_photo());
                        Glide.with(MyApplication.getContext())
                                .load(url)
                                .into(imageView);
                    }
                    @Override
                    public void onFail(Request request, IOException exception) {
                    }
                });
            }
        });
        ((Fix_Viewholder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked() == true) {
                    check_cnt++;
                    mapp.add(position);
                } else if (buttonView.isChecked() == false) {
                    check_cnt--;
                    mapp.remove(position);
                }
            }
        });/*
        ((Fix_Viewholder) holder).checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/
//        if (isChecked==true){
//            mapp.remove(position);
//        }else{
//            mapp.add(position);
//        }

        //   ((Fix_Viewholder)holder).radioButton.setChecked(position == mCheckedPostion);
//        ((Fix_Viewholder)holder).radioButton.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //      public void onClick(View v) {
//                if (position == mCheckedPostion) {
//                    ((Fix_Viewholder)holder).radioButton.setChecked(false);
//                    mCheckedPostion = -1;
//                } else {
//                    mCheckedPostion = position;
//                    notifyDataSetChanged();
//            }
        //       });
    }


    @Override
    public int getItemCount() {
//        if(mem.getMem_drone()==null){
//            return 0;
//        }else(mem.getMem_drone().get(0)==""){
//
//        }else{
        if (drlist == null) return 0;
        return drlist.size();
//        }

    }
}