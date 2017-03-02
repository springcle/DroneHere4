package com.example.ksj_notebook.dronehere.Here;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.ksj_notebook.dronehere.Dronedb.TabDrone;
import com.example.ksj_notebook.dronehere.News.TabNews;
import com.example.ksj_notebook.dronehere.R;

/**
 * Created by NAKNAK on 2017-03-01.
 */
public class SlidingTabHost extends Fragment {

    FragmentTabHost tabHost;
    FrameLayout frameLayout;

    public SlidingTabHost(){
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.sliding_tab_main, container, false);

        frameLayout = (FrameLayout) view.findViewById(R.id.tabcontent);
        tabHost = (FragmentTabHost)view.findViewById(R.id.tabHost);

        final ImageView tab1 = new ImageView(getContext());
        tab1.setImageResource(R.drawable.tab1selector);
        final ImageView tab2 = new ImageView(getContext());
        tab2.setImageResource(R.drawable.tab2selector);

        tabHost.setup(getContext(), getChildFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(tab1), TabNews.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(tab2), TabDrone.class, null);

        return view;
        }
}
