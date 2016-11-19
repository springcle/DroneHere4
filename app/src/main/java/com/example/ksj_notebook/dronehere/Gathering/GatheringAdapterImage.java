package com.example.ksj_notebook.dronehere.Gathering;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.ksj_notebook.dronehere.MyApplication;

import java.util.List;

/**
 * Created by ksj_notebook on 2016-05-25.
 */
public class GatheringAdapterImage extends PagerAdapter {
    List<String> bitmaps;

    public void  setImage(List<String> bitmaps){
        this.bitmaps=bitmaps;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(bitmaps==null) return 0;
        return bitmaps.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

            ImageView view = new ImageView(MyApplication.getContext());

            if (bitmaps != null) {
                GlideUrl url = new GlideUrl(bitmaps.get(position));
                Glide.with(MyApplication.getContext())
                        .load(url)
                        .into(view);
            }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view=(View)object;
        container.removeView(view);
    }
}
