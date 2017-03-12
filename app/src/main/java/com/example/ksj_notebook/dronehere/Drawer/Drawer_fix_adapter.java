package com.example.ksj_notebook.dronehere.Drawer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneDB;
import com.example.ksj_notebook.dronehere.data.Member;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ksj_notebook on 2016-06-14.
 */
public class Drawer_fix_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Member mem;
    Context context;
    List<DroneDB> drlist;
    int CheckedPostion;
    int mCheckedPostion;
    Set<Integer> mapp=new HashSet<>();

    static RadioButton lastChecked=null;
    public void setMem(Member mem, Context context){
        this.mem=mem;
        this.context=context;
        drlist=mem.getMem_drone();
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.fix_lay, parent, false);
        return new Fix_Viewholder(view1);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (position==0) {
            lastChecked = ((Fix_Viewholder) holder).radioButton;
            mCheckedPostion=position;
        }
        ((Fix_Viewholder) holder).setDrone1(drlist.get(position));
        ((Fix_Viewholder) holder).radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(lastChecked !=null){
                    lastChecked.setChecked(false);
                    lastChecked = ((Fix_Viewholder) holder).radioButton;
                    mCheckedPostion=position;
                }else {
                    ((Fix_Viewholder) holder).radioButton.setChecked(true);
                    lastChecked = ((Fix_Viewholder) holder).radioButton;
                }
            }
        });


        ((Fix_Viewholder) holder).checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( ((Fix_Viewholder) holder).checkBox.isChecked()==true){
                    if(mem.getMem_drone().size() != 1) {
                        mapp.add(position);
                    } else {
                        Toast.makeText(context, "소유 드론이 1개는 있어야합니다.",Toast.LENGTH_SHORT).show();
                        ((Fix_Viewholder) holder).checkBox.setChecked(false);
                    }
                }else{
                    mapp.remove(position);
                }

            }
        });


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
        if(drlist==null) return 0;
            return drlist.size();
//        }

    }
}
