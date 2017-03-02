package com.example.ksj_notebook.dronehere.Dronedb;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneDB;

/**
 * Created by ksj_notebook on 2016-05-20.
 */
public class TabDroneViewholder extends RecyclerView.ViewHolder{


    TabDroneAdapter.OnItemClickListener mItemClickListener;
    public void setOnItemClickListener(TabDroneAdapter.OnItemClickListener listener){
        mItemClickListener=listener;
    }


    TextView drone_name;
    TextView drone_manufactor;
    TextView drone_use2;
    TextView drone_rating;
    TextView drone_price;
    ImageView drone_image;
    LinearLayout dblist;
    String dr_id;

    public TabDroneViewholder(final View itemView) {
        super(itemView);

        drone_name=(TextView)itemView.findViewById(R.id.drone_name);
        drone_name.setHorizontallyScrolling(false);
        drone_manufactor=(TextView)itemView.findViewById(R.id.drone_manufactor);
        drone_manufactor.setHorizontallyScrolling(false);
        drone_use2=(TextView)itemView.findViewById(R.id.drone_use2);
        drone_use2.setHorizontallyScrolling(false);
        drone_rating=(TextView)itemView.findViewById(R.id.drone_rating);
        drone_rating.setHorizontallyScrolling(false);
        drone_price=(TextView)itemView.findViewById(R.id.drone_price);
        drone_price.setHorizontallyScrolling(false);
        drone_image=(ImageView) itemView.findViewById(R.id.drone_image);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener!=null)
             mItemClickListener.onItemClicked(TabDroneViewholder.this,itemView,dr_id,getAdapterPosition());
            }
        });

    }

    public void setText(DroneDB db){

        dr_id=db.get_id();

        if (db.getDr_photo() != null) {
            GlideUrl url = new GlideUrl(db.getDr_photo());
            Glide.with(MyApplication.getContext())
                    .load(url)
                    .into(drone_image);
        }
        drone_name.setText(db.getDr_name());
        drone_manufactor.setText(db.getDr_manufacture());
        drone_use2.setText(db.getDr_use());
        drone_rating.setText(""+db.getDr_rate());
        drone_price.setText(db.getDr_price());
    }

}
