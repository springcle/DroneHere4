package com.example.ksj_notebook.dronehere.Here;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ksj_notebook.dronehere.Dronedb.TabDrone;
import com.example.ksj_notebook.dronehere.News.TabNews;
import com.example.ksj_notebook.dronehere.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NAKNAK on 2017-03-01.
 */
public class SlidingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Fragment tab_news, tab_drone;
    FragmentManager fragmentManager;
    LinearLayoutManager lm;
    ViewPager pager;
    FragmentPagerAdapter tabAdapter;
    private Context context;
    Button tab_news_btn, tab_drone_btn;
    private static final int TYPE_BODY = 0;
    private static final int TYPE_ETC = 1;

    public SlidingAdapter(Context context, FragmentManager fragmentManager, LinearLayoutManager lm,Button news, Button drone){
        this.context = context;
        this.lm = lm;
        this.fragmentManager = fragmentManager;
        this.tab_news_btn = news;
        this.tab_drone_btn = drone;
    }
    @Override
    public int getItemViewType(int position) {
        if ((position) == 0) {
            return TYPE_BODY;
        } else{
            return TYPE_ETC;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ETC:
                /*
                View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_header, parent, false);
                return new SlidingViewHolderHeader(headerView);*/
            case TYPE_BODY:
                View bodyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_main, parent, false);
                pager = (ViewPager)bodyView.findViewById(R.id.sliding_viewpager);
                return new SlidingViewHolderMain(context, bodyView,fragmentManager);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ETC :
                /*
                ((SlidingViewHolderHeader)holder).tab_news.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pager.setCurrentItem(0);
                        lm.scrollToPositionWithOffset(0,0);

                        tab_news = new TabNews();
                        tab_drone = new TabDrone();
                        //FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                        //FragmentManager manager = ((Activity)context).getFragmentManager();
                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameWork, tab_news).commit();

                    }
                });*/
                /*
                ((SlidingViewHolderHeader)holder).tab_drone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pager.setCurrentItem(1);
                        lm.scrollToPositionWithOffset(0, 0);
                        tab_news = new TabNews();
                        tab_drone = new TabDrone();
                        //FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                        //FragmentManager manager = ((Activity)context).getFragmentManager();
                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameWork, tab_drone).commit();
                    }
                });*/
                break;
            case  TYPE_BODY:
                init_viewpager();
                tab_news_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pager.setCurrentItem(0);
                        lm.scrollToPositionWithOffset(0,0);
                    }
                });
                tab_drone_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pager.setCurrentItem(1);
                        lm.scrollToPositionWithOffset(0,0);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    void init_viewpager(){
        tab_news = new TabNews();
        tab_drone = new TabDrone();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new TabNews());
        fragmentList.add(new TabDrone());
        tabAdapter = new FragmentTabPager(context, fragmentManager, fragmentList);
        pager.setAdapter(tabAdapter);
        pager.setCurrentItem(0);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                lm.scrollToPositionWithOffset(0,0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
