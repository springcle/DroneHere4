package com.example.ksj_notebook.dronehere.Here;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.ksj_notebook.dronehere.Dronedb.TabDrone;
import com.example.ksj_notebook.dronehere.News.TabNews;
import com.example.ksj_notebook.dronehere.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by NAKNAK on 2017-03-01.
 */
public class SlidingViewHolderMain extends RecyclerView.ViewHolder {

    ViewPager pager;
    private Context context;
    Fragment tab_news, tab_drone;
    //FrameLayout fr;
    FragmentManager fragmentManager;

    public SlidingViewHolderMain(Context context, View itemView, FragmentManager fragmentManager) {
        super(itemView);
        this.context = context;
        this.fragmentManager = fragmentManager;
        pager=(ViewPager)itemView.findViewById(R.id.sliding_viewpager);
        //fr = (FrameLayout)itemView.findViewById(R.id.frameWork);
    }

    public void init_body(){
        tab_news = new TabNews();
        tab_drone = new TabDrone();
        //FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
        //FragmentManager manager = ((Activity)context).getFragmentManager();
        //tab_news = new Fragment();
        //((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.sliding_viewpager, tab_news).commit();
        /*
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new TabNews());
        fragmentList.add(new TabDrone());
        FragmentPagerAdapter tabAdapter = new FragmentTabPager(manager, fragmentList);
        pager.setAdapter(tabAdapter);
        */
        //FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
        //FragmentManager manager = ((Activity)context).getFragmentManager();
        //((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameWork, tab_news).commit();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new TabNews());
        fragmentList.add(new TabDrone());
        FragmentPagerAdapter tabAdapter = new FragmentTabPager(context, fragmentManager, fragmentList);
        pager.setAdapter(tabAdapter);
        pager.setCurrentItem(0);
    }
}
