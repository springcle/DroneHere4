package com.example.ksj_notebook.dronehere.Dronedb;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.AddReviewResult;
import com.example.ksj_notebook.dronehere.data.DroneDB;
import com.example.ksj_notebook.dronehere.data.Review;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;

import static com.example.ksj_notebook.dronehere.MyApplication.getContext;

/**
 * Created by ksj_notebook on 2016-05-29.
 */




public class DroneDetailAdapter extends RecyclerView.Adapter<ViewHolder> {

    DroneDB db;
    List<Review> item2;
    int cnt;


  //  ViewPager mViewPager;

    public void setDb(DroneDB db){
        this.db=db;
        cnt=1;
        item2=db.getDr_review();
        notifyDataSetChanged();
    }

    private static final int TYPE_DRONE_HEADER = 0;
    private static final int TYPE_DRONE_RV = 1;
    private static final int TYPE_DRONE_ADD=2;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_DRONE_HEADER:
                View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dbdetail_header, parent, false);
                return new DroneDetailViewHolderHeader(headerView);
            case TYPE_DRONE_RV:
                View rvView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dbdetail_rv, parent,false);
                return new DroneDetailViewHolderReview(rvView);
            case TYPE_DRONE_ADD:
                View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.db_add, parent,false);
                return new DroneDetailViewHolderAdd(addView);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_DRONE_HEADER;
        }else if(position==db.getDr_review().size()+1){
            return TYPE_DRONE_ADD;
        }else return TYPE_DRONE_RV;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_DRONE_HEADER:
                ((DroneDetailViewHolderHeader) holder).setDt(db);
                break;
            case TYPE_DRONE_RV:
                if((position-1)==0) {
                    ((DroneDetailViewHolderReview) holder).setDt_rv(item2.get(position - 1));
                }else{
                    ((DroneDetailViewHolderReview) holder).setDt_rv2(item2.get(position - 1));
                }

                break;
            case TYPE_DRONE_ADD:
                ((DroneDetailViewHolderAdd)holder).setAdd_btn("한줄평 더보기 ("+item2.size()+"/"+db.getDr_re_num()+")");
                ((DroneDetailViewHolderAdd)holder).btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        NetworkManager.getInstance().getDbAdd1(getContext(),""+db.get_id(),cnt ,new NetworkManager.OnResultListener<AddReviewResult>() {

                            @Override
                            public void onSuccess(Request request, AddReviewResult result) {
                                cnt++;
                                List<Review> add= result.getResult();
                                item2.addAll(add);
                                notifyDataSetChanged();
                                ((DroneDetailViewHolderAdd)holder).setAdd_btn("한줄평 더보기 ("+item2.size()+"/"+db.getDr_re_num()+")");
                            }

                            @Override
                            public void onFail(Request request, IOException exception) {

                            }
                        });
                    }
                });
                break;



        }
    }

    @Override
    public int getItemCount() {
        if(db==null)return 0;
        return 2+db.getDr_review().size();
    }







}
