package com.santamaria.dronehere.Dronedb;

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
    TextView drone_price;
    ImageView drone_image;
    LinearLayout dblist;
    String dr_id;
    ImageView star1, star2, star3, star4, star5;

    public TabDroneViewholder(final View itemView) {
        super(itemView);

        drone_name=(TextView)itemView.findViewById(R.id.drone_name);
        drone_name.setHorizontallyScrolling(false);
        drone_manufactor=(TextView)itemView.findViewById(R.id.drone_manufactor);
        drone_manufactor.setHorizontallyScrolling(false);
        drone_use2=(TextView)itemView.findViewById(R.id.drone_use2);
        drone_use2.setHorizontallyScrolling(false);
      /*  drone_rating.setHorizontallyScrolling(false);*/
/*        drone_price=(TextView)itemView.findViewById(R.id.drone_price);
        drone_price.setHorizontallyScrolling(false);*/
        drone_image=(ImageView) itemView.findViewById(R.id.drone_image);
        star1 = (ImageView) itemView.findViewById(R.id.star1);
        star2 = (ImageView) itemView.findViewById(R.id.star2);
        star3 = (ImageView) itemView.findViewById(R.id.star3);
        star4 = (ImageView) itemView.findViewById(R.id.star4);
        star5 = (ImageView) itemView.findViewById(R.id.star5);

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
        if (db.getDr_rate() == 0.0) {
            star1.setBackgroundResource(R.drawable.rating_star_empty);
            star2.setBackgroundResource(R.drawable.rating_star_empty);
            star3.setBackgroundResource(R.drawable.rating_star_empty);
            star4.setBackgroundResource(R.drawable.rating_star_empty);
            star5.setBackgroundResource(R.drawable.rating_star_empty);
        } else if (db.getDr_rate() == 0.5) {
            star1.setBackgroundResource(R.drawable.rating_star_half);
            star2.setBackgroundResource(R.drawable.rating_star_empty);
            star3.setBackgroundResource(R.drawable.rating_star_empty);
            star4.setBackgroundResource(R.drawable.rating_star_empty);
            star5.setBackgroundResource(R.drawable.rating_star_empty);
        } else if (db.getDr_rate() == 1.0) {
            star1.setBackgroundResource(R.drawable.rating_star);
            star2.setBackgroundResource(R.drawable.rating_star_empty);
            star3.setBackgroundResource(R.drawable.rating_star_empty);
            star4.setBackgroundResource(R.drawable.rating_star_empty);
            star5.setBackgroundResource(R.drawable.rating_star_empty);
        } else if (db.getDr_rate() == 1.5) {
            star1.setBackgroundResource(R.drawable.rating_star);
            star2.setBackgroundResource(R.drawable.rating_star_half);
            star3.setBackgroundResource(R.drawable.rating_star_empty);
            star4.setBackgroundResource(R.drawable.rating_star_empty);
            star5.setBackgroundResource(R.drawable.rating_star_empty);
        } else if (db.getDr_rate() == 2.0) {
            star1.setBackgroundResource(R.drawable.rating_star);
            star2.setBackgroundResource(R.drawable.rating_star);
            star3.setBackgroundResource(R.drawable.rating_star_empty);
            star4.setBackgroundResource(R.drawable.rating_star_empty);
            star5.setBackgroundResource(R.drawable.rating_star_empty);
        } else if (db.getDr_rate() == 2.5) {
            star1.setBackgroundResource(R.drawable.rating_star);
            star2.setBackgroundResource(R.drawable.rating_star);
            star3.setBackgroundResource(R.drawable.rating_star_half);
            star4.setBackgroundResource(R.drawable.rating_star_empty);
            star5.setBackgroundResource(R.drawable.rating_star_empty);
        } else if (db.getDr_rate() == 3.0) {
            star1.setBackgroundResource(R.drawable.rating_star);
            star2.setBackgroundResource(R.drawable.rating_star);
            star3.setBackgroundResource(R.drawable.rating_star);
            star4.setBackgroundResource(R.drawable.rating_star_empty);
            star5.setBackgroundResource(R.drawable.rating_star_empty);
        } else if (db.getDr_rate() == 3.5) {
            star1.setBackgroundResource(R.drawable.rating_star);
            star2.setBackgroundResource(R.drawable.rating_star);
            star3.setBackgroundResource(R.drawable.rating_star);
            star4.setBackgroundResource(R.drawable.rating_star_half);
            star5.setBackgroundResource(R.drawable.rating_star_empty);
        } else if (db.getDr_rate() == 4.0) {
            star1.setBackgroundResource(R.drawable.rating_star);
            star2.setBackgroundResource(R.drawable.rating_star);
            star3.setBackgroundResource(R.drawable.rating_star);
            star4.setBackgroundResource(R.drawable.rating_star);
            star5.setBackgroundResource(R.drawable.rating_star_empty);
        } else if (db.getDr_rate() == 4.5) {
            star1.setBackgroundResource(R.drawable.rating_star);
            star2.setBackgroundResource(R.drawable.rating_star);
            star3.setBackgroundResource(R.drawable.rating_star);
            star4.setBackgroundResource(R.drawable.rating_star);
            star5.setBackgroundResource(R.drawable.rating_star_half);
        } else if (db.getDr_rate() == 5.0) {
            star1.setBackgroundResource(R.drawable.rating_star);
            star2.setBackgroundResource(R.drawable.rating_star);
            star3.setBackgroundResource(R.drawable.rating_star);
            star4.setBackgroundResource(R.drawable.rating_star);
            star5.setBackgroundResource(R.drawable.rating_star);
        }
     /*   drone_price.setText(db.getDr_price());*/
    }

}
