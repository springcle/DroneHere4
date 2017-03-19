package com.santamaria.dronehere.Here;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by NAKNAK on 2017-03-01.
 */
public class FragmentTabPager extends FragmentPagerAdapter {
    List<Fragment> fragmentList;
    Context context;

    public FragmentTabPager(Context context, FragmentManager fm, List<Fragment> fragmentList){
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}

