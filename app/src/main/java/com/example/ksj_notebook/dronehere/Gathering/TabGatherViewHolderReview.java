package com.example.ksj_notebook.dronehere.Gathering;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.Review;

/**
 * Created by ksj_notebook on 2016-05-21.
 */
public class TabGatherViewHolderReview extends RecyclerView.ViewHolder {

    TextView date;
    TextView nick;
    TextView content;



    public TabGatherViewHolderReview(View itemView) {
        super(itemView);

        date=(TextView) itemView.findViewById(R.id.gath_review_date);
        nick=(TextView) itemView.findViewById(R.id.gath_review_nick);
        content=(TextView) itemView.findViewById(R.id.gath_review_content);


    }
    public void setReview(Review review){

        String time = review.getRe_regdate();

        String[] val=time.split("T");
        String[] val2=val[0].split("-");


        date.setText(val2[1]+"."+val2[2]);
        nick.setText(review.getRe_memname());
        content.setText(review.getRe_content());

    }

}
