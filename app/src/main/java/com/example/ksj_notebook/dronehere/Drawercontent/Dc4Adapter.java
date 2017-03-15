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
            Glide.with(MyApplication.getContext())
                    .load(R.drawable.a0)
                    .fitCenter()
                    .into(view);
        }else if(position==1){
            Glide.with(MyApplication.getContext())
                    .load(R.drawable.a1)
                    .fitCenter()
                    .into(view);
        }else if(position==2){
            Glide.with(MyApplication.getContext())
                    .load(R.drawable.a2)
                    .fitCenter()
                    .into(view);
        }
        else if(position==3){
            Glide.with(MyApplication.getContext())
                    .load(R.drawable.a3)
                    .fitCenter()
                    .into(view);
        }
        else if(position==4){
            Glide.with(MyApplication.getContext())
                    .load(R.drawable.a4)
                    .fitCenter()
                    .into(view);
        }
        else if(position==5){
            Glide.with(MyApplication.getContext())
                    .load(R.drawable.a5)
                    .fitCenter()
                    .into(view);
        }
        else if(position==6){
            Glide.with(MyApplication.getContext())
                    .load(R.drawable.a6)
                    .fitCenter()
                    .into(view);
        }
        else if(position==7){
            Glide.with(MyApplication.getContext())
                    .load(R.drawable.a7)
                    .fitCenter()
                    .into(view);
        }
        else if(position==8){
            Glide.with(MyApplication.getContext())
                    .load(R.drawable.a8)
                    .fitCenter()
                    .into(view);
        }
        else if(position==9){
            Glide.with(MyApplication.getContext())
                    .load(R.drawable.a9)
                    .fitCenter()
                    .into(view);
        }

        
        container.addView(view);
        return view;
    }
}
