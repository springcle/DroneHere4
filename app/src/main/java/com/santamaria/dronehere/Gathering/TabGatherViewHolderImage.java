package com.santamaria.dronehere.Gathering;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.santamaria.dronehere.R;

import java.util.List;

/**
 * Created by ksj_notebook on 2016-05-24.
 */
public class TabGatherViewHolderImage extends RecyclerView.ViewHolder {
    ViewPager pager;
    TabGatherAdapterImage imageAdapter;


    public TabGatherViewHolderImage(View itemView) {
        super(itemView);
        pager=(ViewPager)itemView.findViewById(R.id.pager);
        imageAdapter=new TabGatherAdapterImage();
        pager.setAdapter(imageAdapter);


    }
    public void setImage(List<String> list){
        imageAdapter.setImage(list);
    }
}
