package com.example.ksj_notebook.dronehere.Dronedb;

import android.graphics.drawable.ClipDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneDB;

/**
 * Created by ksj_notebook on 2016-05-29.
 */
public class DroneDetailViewHolderHeader extends RecyclerView.ViewHolder {

    /*ImageView imageView, imageView2, imageView3, imageView4, imageView5;*/

    ViewPager mViewPager;
    DetailPagerAdapter detailPagerAdapter;



    TextView dt_manu;
    TextView dt_price;
    TextView dt_use;

    ImageView progressBar;
    ImageView progressBar2;
    ImageView progressBar3;
    ImageView progressBar4;

    ImageView star1, star2, star3, star4, star5;

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

    int cnt = 0;


    public DroneDetailViewHolderHeader(View itemView) {
        super(itemView);

        star1 = (ImageView)itemView.findViewById(R.id.detail_star1);
        star2 = (ImageView)itemView.findViewById(R.id.detail_star2);
        star3 = (ImageView)itemView.findViewById(R.id.detail_star3);
        star4 = (ImageView)itemView.findViewById(R.id.detail_star4);
        star5 = (ImageView)itemView.findViewById(R.id.detail_star5);


        drdb_title = (TextView) itemView.findViewById(R.id.drdb_title);
       /* imageView=(ImageView)itemView.findViewById(R.id.detail_imageView);
        imageView2=(ImageView)itemView.findViewById(R.id.detail_imageView2);
        imageView3=(ImageView)itemView.findViewById(R.id.detail_imageView3);
        imageView4=(ImageView)itemView.findViewById(R.id.detail_imageView4);
        imageView5=(ImageView)itemView.findViewById(R.id.detail_imageView5);
*/
       // viewpagerimg = (ImageView) itemView.findViewById(R.id.detail_viewpager_item_img); //뷰페이저용

        dt_manu = (TextView) itemView.findViewById(R.id.dt_manu);
        dt_price = (TextView) itemView.findViewById(R.id.dt_price);
        dt_use = (TextView) itemView.findViewById(R.id.dt_use);

        progressBar = (ImageView) itemView.findViewById(R.id.progressBar11);
        progressBar2 = (ImageView) itemView.findViewById(R.id.progressBar22);
        progressBar3 = (ImageView) itemView.findViewById(R.id.progressBar33);
        progressBar4 = (ImageView) itemView.findViewById(R.id.progressBar44);

        dt_flight = (TextView) itemView.findViewById(R.id.dt_flight);
        dt_range = (TextView) itemView.findViewById(R.id.dt_range);
        dt_pic = (TextView) itemView.findViewById(R.id.dt_pic);
        dt_pk = (TextView) itemView.findViewById(R.id.dt_pk);

        imbtn = (Button) itemView.findViewById(R.id.imbtn);
        spec = (TextView) itemView.findViewById(R.id.spec);
        viss = (LinearLayout) itemView.findViewById(R.id.viss);

        drawable1 = (ClipDrawable) progressBar.getDrawable();
        drawable2 = (ClipDrawable) progressBar2.getDrawable();
        drawable3 = (ClipDrawable) progressBar3.getDrawable();
        drawable4 = (ClipDrawable) progressBar4.getDrawable();

        imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cnt % 2 == 0) {
                    viss.setVisibility(View.VISIBLE);
                    imbtn.setBackgroundResource(R.drawable.box_2);
                    imbtn.setText("상세 스펙 접기");
                } else {
                    viss.setVisibility(View.GONE);
                    imbtn.setBackgroundResource(R.drawable.box_1);
                    imbtn.setText("상세 스펙 더보기");
                }
                cnt++;

            }
        });

    }


    public void setDt(DroneDB db){
        /*
        List<String> dr = db.getDr_photoArr();
        int dr_ea = dr.size();
        Log.w("사진갯수",dr_ea+"");
        if (dr_ea == 1){
            if(db.getDr_photoArr().get(0) !=""){ //서버에서 ""로 이미지 url 저장해둔것에 대한 예외처리
            GlideUrl url1 = new GlideUrl((db.getDr_photoArr().get(0)));
            Glide.with(MyApplication.getContext()).load(url1).into(imageView);
            }
        } else if(dr_ea == 2){
            if(db.getDr_photoArr().get(0) !=""){
                GlideUrl url1 = new GlideUrl((db.getDr_photoArr().get(0)));
                Glide.with(MyApplication.getContext()).load(url1).into(imageView);
            }
            if(db.getDr_photoArr().get(1) !=""){
                GlideUrl url2 = new GlideUrl((db.getDr_photoArr().get(1)));
                Glide.with(MyApplication.getContext()).load(url2).into(imageView2);
            }
        } else if(dr_ea == 3){
            if(db.getDr_photoArr().get(0) !=""){
                GlideUrl url1 = new GlideUrl((db.getDr_photoArr().get(0)));
                Glide.with(MyApplication.getContext()).load(url1).into(imageView);
            }
            if(db.getDr_photoArr().get(1) !=""){
                GlideUrl url2 = new GlideUrl((db.getDr_photoArr().get(1)));
                Glide.with(MyApplication.getContext()).load(url2).into(imageView2);
            }
            if(db.getDr_photoArr().get(2) !=""){
                GlideUrl url3 = new GlideUrl((db.getDr_photoArr().get(2)));
                Glide.with(MyApplication.getContext()).load(url3).into(imageView3);
            }
        } else if(dr_ea == 4){
            if(db.getDr_photoArr().get(0) !=""){
                GlideUrl url1 = new GlideUrl((db.getDr_photoArr().get(0)));
                Glide.with(MyApplication.getContext()).load(url1).into(imageView);
            }
            if(db.getDr_photoArr().get(1) !=""){
                GlideUrl url2 = new GlideUrl((db.getDr_photoArr().get(1)));
                Glide.with(MyApplication.getContext()).load(url2).into(imageView2);
            }
            if(db.getDr_photoArr().get(2) !=""){
                GlideUrl url3 = new GlideUrl((db.getDr_photoArr().get(2)));
                Glide.with(MyApplication.getContext()).load(url3).into(imageView3);
            }
            if(db.getDr_photoArr().get(3) !=""){
                GlideUrl url4 = new GlideUrl((db.getDr_photoArr().get(3)));
                Glide.with(MyApplication.getContext()).load(url4).into(imageView4);
            }
        } else if(dr_ea == 5){
            if(db.getDr_photoArr().get(0) !=""){
                GlideUrl url1 = new GlideUrl((db.getDr_photoArr().get(0)));
                Glide.with(MyApplication.getContext()).load(url1).into(imageView);
            }
            if(db.getDr_photoArr().get(1) !=""){
                GlideUrl url2 = new GlideUrl((db.getDr_photoArr().get(1)));
                Glide.with(MyApplication.getContext()).load(url2).into(imageView2);
            }
            if(db.getDr_photoArr().get(2) !=""){
                GlideUrl url3 = new GlideUrl((db.getDr_photoArr().get(2)));
                Glide.with(MyApplication.getContext()).load(url3).into(imageView3);
            }
            if(db.getDr_photoArr().get(3) !=""){
                GlideUrl url4 = new GlideUrl((db.getDr_photoArr().get(3)));
                Glide.with(MyApplication.getContext()).load(url4).into(imageView4);
            }if(db.getDr_photoArr().get(4) !=""){
                GlideUrl url5 = new GlideUrl((db.getDr_photoArr().get(4)));
                Glide.with(MyApplication.getContext()).load(url5).into(imageView5);
            }
        }
*/

        mViewPager = (ViewPager) itemView.findViewById(R.id.detail_imageView);
        /*mViewPager.addOnPageChangeListener(this);*/

        detailPagerAdapter = new DetailPagerAdapter();
        mViewPager.setAdapter(detailPagerAdapter);

        drdb_title.setText(db.getDr_name());
        dt_manu.setText(db.getDr_manufacture());
        dt_price.setText(db.getDr_price());
        dt_use.setText(db.getDr_use());

        drawable1.setLevel(db.getDr_array()[0]*2000);
        drawable2.setLevel(db.getDr_array()[1]*2000);
        drawable3.setLevel(db.getDr_array()[2]*2000);
        drawable4.setLevel(db.getDr_array()[3]*2000);

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
