package com.example.ksj_notebook.dronehere.Gathering;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.Locatio;

/**
 * Created by ksj_notebook on 2016-05-21.
 */
public class TabGatherViewHolderHeader extends RecyclerView.ViewHolder {

    TextView title;
    TextView address;
    TextView rating;
    TextView content;
    TextView info1;
    TextView info2;
    TextView info3;
    TextView info4;
    RatingBar ratingBar;


    public TabGatherViewHolderHeader(View itemView) {
        super(itemView);
        title=(TextView)itemView.findViewById(R.id.gath_title);
        address=(TextView)itemView.findViewById(R.id.gath_address);
        rating=(TextView)itemView.findViewById(R.id.gath_rating);
        content=(TextView)itemView.findViewById(R.id.gath_content);
        ratingBar=(RatingBar)itemView.findViewById(R.id.gath_raingbar);

        info1=(TextView)itemView.findViewById(R.id.gath_info1);
        info2=(TextView)itemView.findViewById(R.id.gath_info2);
        info3=(TextView)itemView.findViewById(R.id.gath_info3);
        info4=(TextView)itemView.findViewById(R.id.gath_info4);
    }
    public void setHeader(Locatio location){
        title.setText(location.getLoca_name());
        address.setText(location.getLoca_address());
        rating.setText(""+(location.getLoca_rate()));
        content.setText(location.getLoca_content());
        ratingBar.setRating((float) location.getLoca_rate());

        int size=location.getLoca_info().size();
        String[] info= {"","","",""};

        for(int i=0;i<size;i++)
            info[i]=location.getLoca_info().get(i);

        info1.setText(info[0]);
        info2.setText(info[1]);
        info3.setText(info[2]);
        info4.setText(info[3]);
    }


}
