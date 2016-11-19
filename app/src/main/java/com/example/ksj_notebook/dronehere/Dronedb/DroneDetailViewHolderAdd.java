package com.example.ksj_notebook.dronehere.Dronedb;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.ksj_notebook.dronehere.R;

/**
 * Created by ksj_notebook on 2016-06-09.
 */
public class DroneDetailViewHolderAdd extends RecyclerView.ViewHolder {

   Button btn;

    public DroneDetailViewHolderAdd(View itemView) {
        super(itemView);

        btn=(Button)itemView.findViewById(R.id.dr_add);
    }
    public void setAdd_btn(String d){
        btn.setText(d);
    }
}
