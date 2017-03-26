package com.santamaria.dronehere.Gathering;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.santamaria.dronehere.R;
import com.santamaria.dronehere.data.Review;

/**
 * Created by ksj_notebook on 2016-05-24.
 */
public class GatheringViewHolderReview extends RecyclerView.ViewHolder {

    TextView tx1;
    TextView tx2;

    public GatheringViewHolderReview(View itemView) {
        super(itemView);

        tx1=(TextView)itemView.findViewById(R.id.gt_nick);
        tx2=(TextView)itemView.findViewById(R.id.gt_rv);
    }
    public void setReview(Review rv){
        tx1.setText(rv.getRe_memname());
        tx2.setText(rv.getRe_content());
    }
}
