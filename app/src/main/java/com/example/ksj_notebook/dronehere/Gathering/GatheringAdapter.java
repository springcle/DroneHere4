package com.example.ksj_notebook.dronehere.Gathering;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.AddReviewResult;
import com.example.ksj_notebook.dronehere.data.Gathering;
import com.example.ksj_notebook.dronehere.data.Review;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;

/**
 * Created by ksj_notebook on 2016-05-24.
 */
public class GatheringAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Gathering items1;
    List<Review> items2;
    int cnt;



    public void setGathering(Gathering gathering) {

        items1=gathering;
        items2=gathering.getGathe_review();
        cnt=1;
        notifyDataSetChanged();
    }
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_REVIEW=1;
    private static final int TYPE_ADD=2;

    public int getItemViewType(int position) {
        if ((position) == 0) {
            return TYPE_HEADER;
        }else if(position==items2.size()+1) {
            return TYPE_ADD;
        }else return TYPE_REVIEW;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gt_header, parent, false);
                return new GatheringViewHolderHeader(headerView);
            case TYPE_REVIEW:
                View reviewView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gt_review, parent, false);
                return new GatheringViewHolderReview(reviewView);
            case TYPE_ADD:
                View add1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_gather_addholder, parent, false);
                return new TabGatherViewHolderAdd(add1);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((GatheringViewHolderHeader) holder).setHeader(items1);
                break;
            case TYPE_REVIEW:
                ((GatheringViewHolderReview) holder).setReview(items2.get(position-1));
                break;
            case TYPE_ADD:
                ((TabGatherViewHolderAdd)holder).setAdd_btn("댓글 더보기 ("+items2.size()+"/"+items1.getGathe_re_num()+")");

                ((TabGatherViewHolderAdd)holder).add_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        NetworkManager.getInstance().getAddReview2(MyApplication.getContext(),items1.get_id(),cnt ,new NetworkManager.OnResultListener<AddReviewResult>() {
                            @Override
                            public void onSuccess(Request request, AddReviewResult result) {
                                cnt++;
                                List<Review> add1= result.getResult();
                                items2.addAll(add1);
                                notifyDataSetChanged();
                                ((TabGatherViewHolderAdd)holder).setAdd_btn("한줄리뷰 더보기 ("+items2.size()+"/"+items1.getGathe_re_num()+")");
                            }
                            @Override
                            public void onFail(Request request, IOException exception) {     }
                        });
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(items2==null)return 0;
        return items2.size()+2;
    }
}
