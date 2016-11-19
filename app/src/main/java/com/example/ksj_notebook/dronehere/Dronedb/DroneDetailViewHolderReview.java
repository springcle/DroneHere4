package com.example.ksj_notebook.dronehere.Dronedb;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.Review;

/**
 * Created by ksj_notebook on 2016-05-29.
 */
public class DroneDetailViewHolderReview extends RecyclerView.ViewHolder {

    TextView dt_writer;
    TextView dt_rv;
    ImageView line9;

    public DroneDetailViewHolderReview(View itemView) {
        super(itemView);

        dt_writer=(TextView)itemView.findViewById(R.id.dt_writer);
        dt_rv=(TextView)itemView.findViewById(R.id.dt_rv);
        line9=(ImageView)itemView.findViewById(R.id.line9);
    }

    public void setDt_rv(Review rv){
        dt_writer.setText(rv.getRe_memname());
        dt_rv.setText(rv.getRe_content());
    }

    public void setDt_rv2(Review rv){
        dt_writer.setText(rv.getRe_memname());
        dt_rv.setText(rv.getRe_content());
        line9.setBackgroundColor(Color.rgb(221,225,242));
    }
}
