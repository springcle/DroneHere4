package com.example.ksj_notebook.dronehere.Dronedb;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ksj_notebook.dronehere.R;

import java.util.ArrayList;

import static com.example.ksj_notebook.dronehere.MyApplication.getContext;

/**
 * Created by woosuk on 2016-12-11.
 */

public class DetailPagerAdapter  extends PagerAdapter{

   private ArrayList<DroneDetail> pagerimageList = new ArrayList<>();


    public DetailPagerAdapter(ArrayList<DroneDetail> pagerimageList) {
        this.pagerimageList = pagerimageList;
    }


    /* 페이저 어댑터에서    onBindView 역할 + */
    @Override
    public Object instantiateItem(ViewGroup container, int position) { // onBindView
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dbdetail_viewpager_item, container, false);
        ImageView itemImg = (ImageView) view.findViewById(R.id.detail_viewpager_item_img);

        Glide.with(getContext())
                .load(pagerimageList.get(position).imageList.get(position))
                .centerCrop()
                .thumbnail(0.1f)
                .into(itemImg);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return pagerimageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }






}
