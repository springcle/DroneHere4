package com.example.ksj_notebook.dronehere.Drawercontent;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;

/**
 * Created by ksj_notebook on 2016-06-15.
 */
public class Dc4Adapter extends PagerAdapter {


    public void  setImage(){
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view=(View)object;
        container.removeView(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        ImageView view = new ImageView(MyApplication.getContext());
        if(position==0){
            view.setBackgroundResource(R.drawable.a0);
        }else if(position==1){
            view.setBackgroundResource(R.drawable.a1);
        }else if(position==2){
            view.setBackgroundResource(R.drawable.a2);
        }
        else if(position==3){
            view.setBackgroundResource(R.drawable.a3);
        }
        else if(position==4){
            view.setBackgroundResource(R.drawable.a4);
        }
        else if(position==5){
            view.setBackgroundResource(R.drawable.a5);
        }
        else if(position==6){
            view.setBackgroundResource(R.drawable.a6);
        }
        else if(position==7){
            view.setBackgroundResource(R.drawable.a7);
        }
        else if(position==8){
            view.setBackgroundResource(R.drawable.a8);
        }
        else if(position==9){
            view.setBackgroundResource(R.drawable.a9);
        }
        
        container.addView(view);
        return view;
    }
}
