package com.santamaria.dronehere.login;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.santamaria.dronehere.MyApplication;
import com.santamaria.dronehere.R;
import com.santamaria.dronehere.data.DroneDB;

/**
 * Created by ksj_notebook on 2016-06-09.
 */
public class DbSearchViewHolder extends RecyclerView.ViewHolder {

    TextView drname;
    TextView drmanu;
    ImageView drimage;
    LinearLayout linn;
    DroneDB data;

    public DbSearchViewHolder(final View itemView) {
        super(itemView);
        drname=(TextView)itemView.findViewById(R.id.search_drname);
        drmanu=(TextView)itemView.findViewById(R.id.search_manu);
        drimage=(ImageView)itemView.findViewById(R.id.circle_drname);
        linn=(LinearLayout)itemView.findViewById(R.id.item_linn);
        linn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener != null){
                    mItemClickListener.onItemClicked(DbSearchViewHolder.this,itemView ,data,getAdapterPosition());
                }
            }
        });
    }

    public void setList(DroneDB db){
        data=db;
        drname.setText(db.getDr_name());
        drmanu.setText(db.getDr_manufacture());

        GlideUrl url = new GlideUrl(db.getDr_photo());
        Glide.with(MyApplication.getContext())
                .load(url)
                .centerCrop()
                .into(drimage);
    }

    DronePickDialogAdapter.OnItemClickListener mItemClickListener;
    public void setOnItemClickListener(DronePickDialogAdapter.OnItemClickListener listener){
        mItemClickListener = listener;
    }
}
