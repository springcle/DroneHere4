package com.santamaria.dronehere.Gathering;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.santamaria.dronehere.MyApplication;
import com.santamaria.dronehere.R;
import com.santamaria.dronehere.data.AddGatheringResult;
import com.santamaria.dronehere.data.AddReviewResult;
import com.santamaria.dronehere.data.Gathering;
import com.santamaria.dronehere.data.Locatio;
import com.santamaria.dronehere.data.Review;
import com.santamaria.dronehere.manager.NetworkManager;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;

/**
 * Created by ksj_notebook on 2016-05-21.
 */
public class TabGatherAdapter extends RecyclerView.Adapter<ViewHolder>{

    public interface OnItemClickListener {
        public void onItemClicked(TabGatherViewHolderGather holder, View view, Gathering s, int position);
    }

    OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    Locatio location;
    List<String> items1;
    List<Review> items2;
    List<Gathering> items3;
    List<Gathering> items4;

    int cnt1;
    int cnt2;
    int cnt3;


    public void setGather(Locatio location) {
        this.location=location;
        cnt1=1;
        cnt2=1;
        cnt3=1;
        items1=location.getLoca_photo();
        items2=location.getLoca_review();
        items3=location.getGathe_list();
        items4=location.getGathe_list_past();
        notifyDataSetChanged();
    }


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_REVIEW=2;
    private static final int ADD1=3;
    private static final int TYPE_GATHERING=4;
    private static final int ADD2=5;
    private static final int PAST_GATHERING=6;
    private static final int ADD3=7;

    @Override
    public int getItemViewType(int position) {
        if ((position) == 0) {
            return TYPE_HEADER;
        } else if(position==1){
            return TYPE_IMAGE;
        } else if(position<(items2.size()+2)){
            return TYPE_REVIEW;
        }else if(position==(items2.size()+2)) {
            return ADD1;
        } else if(position<(items2.size()+items3.size()+3)){
            return TYPE_GATHERING;
        } else if(position==items2.size()+items3.size()+3){
            return ADD2;
        } else if(position<(items2.size()+items3.size()+items4.size()+4)){
            return PAST_GATHERING;
        } else return ADD3;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gath_item_header, parent, false);
                return new TabGatherViewHolderHeader(headerView);
            case TYPE_IMAGE:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gath_item_image, parent, false);
                return new TabGatherViewHolderImage(view);
            case TYPE_REVIEW:
                View reviewView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gath_item_review, parent, false);
                return new TabGatherViewHolderReview(reviewView);
            case TYPE_GATHERING:
                View gatheringView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gath_item_gathering, parent, false);
                return new TabGatherViewHolderGather(gatheringView);
            case PAST_GATHERING:
                View gatheringView2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.gath_item_gathering, parent, false);
                return new TabGatherViewHolderGather(gatheringView2);
            case ADD1:
                View add1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_gather_addholder, parent, false);
                return new TabGatherViewHolderAdd(add1);
            case ADD2:
                View add2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_gather_addholder, parent, false);
                return new TabGatherViewHolderAdd(add2);
            case ADD3:
                View add3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_gather_addholder, parent, false);
                return new TabGatherViewHolderAdd(add3);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER :
                ((TabGatherViewHolderHeader) holder).setHeader(location);
                break;
            case  TYPE_IMAGE:
                ((TabGatherViewHolderImage) holder).setImage(items1);
                break;
            case TYPE_REVIEW :
                ((TabGatherViewHolderReview)holder).setReview(items2.get(position-2));
                break;
            case TYPE_GATHERING :
                ((TabGatherViewHolderGather)holder).setGathering(items3.get(position- (items2.size()+3)));
                ((TabGatherViewHolderGather)holder).setOnItemClickListener(mListener);
                break;
            case PAST_GATHERING :
                ((TabGatherViewHolderGather)holder).setGathering(items4.get(position- (items2.size()+items3.size()+4)));
                ((TabGatherViewHolderGather)holder).rel.setBackgroundResource(R.drawable.cir2);
                ((TabGatherViewHolderGather)holder).dday.setText("  댓글\n   "+items4.get(position- (items2.size()+items3.size()+4)).getGathe_past_re_num()+"개");
                ((TabGatherViewHolderGather)holder).setOnItemClickListener(mListener);
                break;
            case ADD1:
                ((TabGatherViewHolderAdd)holder).setAdd_btn("한줄리뷰 더보기 ("+items2.size()+"/"+location.getLoca_re_num()+")");

                ((TabGatherViewHolderAdd)holder).add_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        NetworkManager.getInstance().getAddReview(MyApplication.getContext(),location.getLoca_num(),cnt1 ,new NetworkManager.OnResultListener<AddReviewResult>() {
                            @Override
                            public void onSuccess(Request request, AddReviewResult result) {
                                    cnt1++;
                                List<Review> add1= result.getResult();
                                items2.addAll(add1);
                                notifyDataSetChanged();
                                ((TabGatherViewHolderAdd)holder).setAdd_btn("한줄리뷰 더보기 ("+items2.size()+"/"+location.getLoca_re_num()+")");
                            }
                            @Override
                            public void onFail(Request request, IOException exception) {     }
                        });
                    }
                });
                break;

            case ADD2:
                ((TabGatherViewHolderAdd)holder).setAdd_btn("모임 더보기 ("+items3.size()+"/"+location.getGathe_num()+")");

                ((TabGatherViewHolderAdd)holder).add_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        NetworkManager.getInstance().getAddGath(MyApplication.getContext(),location.getLoca_num(),cnt2 ,new NetworkManager.OnResultListener<AddGatheringResult>() {
                            @Override
                            public void onSuccess(Request request, AddGatheringResult result) {
                                cnt2++;
                                List<Gathering> add2= result.getResult();
                                items3.addAll(add2);
                                notifyDataSetChanged();
                                ((TabGatherViewHolderAdd)holder).setAdd_btn("모임 더보기 ("+items3.size()+"/"+location.getGathe_num()+")");
                            }
                            @Override
                            public void onFail(Request request, IOException exception) {     }
                        });
                    }
                });
                break;

            case ADD3:
                ((TabGatherViewHolderAdd)holder).setAdd_btn("지난모임 더보기 ("+items4.size()+"/"+location.getGathe_past_num()+")");

                ((TabGatherViewHolderAdd)holder).add_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        NetworkManager.getInstance().getAddGath2(MyApplication.getContext(),location.getLoca_num(),cnt3 ,new NetworkManager.OnResultListener<AddGatheringResult>() {
                            @Override
                            public void onSuccess(Request request, AddGatheringResult result) {
                                cnt3++;
                                List<Gathering> add3= result.getResult();
                                items4.addAll(add3);
                                notifyDataSetChanged();
                                ((TabGatherViewHolderAdd)holder).setAdd_btn("지난모임 더보기 ("+items4.size()+"/"+location.getGathe_past_num()+")");
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
        if(location==null) return 0;
        return items2.size()+items3.size()+items4.size()+5;
    }
}
