package com.example.ksj_notebook.dronehere.Dronedb;

import android.graphics.drawable.ClipDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneDB;

/**
 * Created by ksj_notebook on 2016-05-29.
 */
public class DroneDetailViewHolderHeader extends RecyclerView.ViewHolder {

    ImageView imageView, imageView2, imageView3, imageView4, imageView5;
    TextView dt_manu;
    TextView dt_price;
    TextView dt_use;
    TextView dt_rate;
    RatingBar dt_ratingbar;

    ImageView progressBar;
    ImageView progressBar2;
    ImageView progressBar3;
    ImageView progressBar4;

    TextView dt_flight;
    TextView dt_range;
    TextView dt_pic;
    TextView dt_pk;

    Button imbtn;
    TextView spec;

    LinearLayout viss;

    TextView drdb_title;

    ClipDrawable drawable1;
    ClipDrawable drawable2;
    ClipDrawable drawable3;
    ClipDrawable drawable4;

    int cnt=0;


    public DroneDetailViewHolderHeader(View itemView) {
        super(itemView);

        drdb_title=(TextView)itemView.findViewById(R.id.drdb_title);
        imageView=(ImageView)itemView.findViewById(R.id.detail_imageView);
        imageView2=(ImageView)itemView.findViewById(R.id.detail_imageView2);
        imageView3=(ImageView)itemView.findViewById(R.id.detail_imageView3);
        imageView4=(ImageView)itemView.findViewById(R.id.detail_imageView4);
        imageView5=(ImageView)itemView.findViewById(R.id.detail_imageView5);
        dt_manu=(TextView)itemView.findViewById(R.id.dt_manu);
        dt_price=(TextView)itemView.findViewById(R.id.dt_price);
        dt_use=(TextView)itemView.findViewById(R.id.dt_use);
        dt_rate=(TextView)itemView.findViewById(R.id.dt_rate);
        dt_ratingbar=(RatingBar)itemView.findViewById(R.id.dt_ratingbar);

        progressBar=(ImageView)itemView.findViewById(R.id.progressBar11);
        progressBar2=(ImageView)itemView.findViewById(R.id.progressBar22);
        progressBar3=(ImageView)itemView.findViewById(R.id.progressBar33);
        progressBar4=(ImageView)itemView.findViewById(R.id.progressBar44);

        dt_flight=(TextView)itemView.findViewById(R.id.dt_flight);
        dt_range=(TextView)itemView.findViewById(R.id.dt_range);
        dt_pic=(TextView)itemView.findViewById(R.id.dt_pic);
        dt_pk=(TextView)itemView.findViewById(R.id.dt_pk);

        imbtn=(Button)itemView.findViewById(R.id.imbtn);
        spec=(TextView)itemView.findViewById(R.id.spec);
        viss=(LinearLayout)itemView.findViewById(R.id.viss);

         drawable1= (ClipDrawable)progressBar.getDrawable();
         drawable2= (ClipDrawable)progressBar2.getDrawable();
         drawable3= (ClipDrawable)progressBar3.getDrawable();
         drawable4= (ClipDrawable)progressBar4.getDrawable();


        imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cnt%2==0){
                    viss.setVisibility(View.VISIBLE);
                    imbtn.setBackgroundResource(R.drawable.box_2);
                    imbtn.setText("상세 스펙 접기");
                }else{
                    viss.setVisibility(View.GONE);
                    imbtn.setBackgroundResource(R.drawable.box_1);
                    imbtn.setText("상세 스펙 더보기");
                }
                cnt++;

            }
        });

    }

    public void setDt(DroneDB db){
        if (db.getDr_photoArr().get(0) != null) {
            GlideUrl url = new GlideUrl(db.getDr_photoArr().get(0));
            Glide.with(MyApplication.getContext())
                    .load(url)
                    .into(imageView);
        }
        if (db.getDr_photoArr().get(1) != null) {
            GlideUrl url2 = new GlideUrl(db.getDr_photoArr().get(1));
            Glide.with(MyApplication.getContext())
                    .load(url2)
                    .into(imageView2);
        }
        if (db.getDr_photoArr().get(2) != null) {
            GlideUrl url3 = new GlideUrl(db.getDr_photoArr().get(2));
            Glide.with(MyApplication.getContext())
                    .load(url3)
                    .into(imageView3);
        }
        if (db.getDr_photoArr().get(3) != null) {
            GlideUrl url4 = new GlideUrl(db.getDr_photoArr().get(3));
            Glide.with(MyApplication.getContext())
                    .load(url4)
                    .into(imageView4);
        }
        if (db.getDr_photoArr().get(4) != null) {
            GlideUrl url5 = new GlideUrl(db.getDr_photoArr().get(4));
            Glide.with(MyApplication.getContext())
                    .load(url5)
                    .into(imageView5);
        }
        drdb_title.setText(db.getDr_name());
        dt_manu.setText(db.getDr_manufacture());
        dt_price.setText(db.getDr_price());
        dt_use.setText(db.getDr_use());
        dt_rate.setText(""+db.getDr_rate());
        dt_ratingbar.setRating((float)db.getDr_rate());

        drawable1.setLevel(db.getDr_array()[0]*2000);
        drawable2.setLevel(db.getDr_array()[1]*2000);
        drawable3.setLevel(db.getDr_array()[2]*2000);
        drawable4.setLevel(db.getDr_array()[3]*2000);


        String zz;
        if (db.getDr_pk()>100){
            zz="p";
        }else{
            zz="K";
        }

        dt_flight.setText((int)db.getDr_flighttime()+"분");
        dt_range.setText((int)db.getDr_receiptrange()+"m");
        dt_pic.setText(""+db.getDr_megapixels()+"m.p");
        dt_pk.setText(""+db.getDr_pk()+zz);
        spec.setText(""+db.getDr_spec());

    }
}
