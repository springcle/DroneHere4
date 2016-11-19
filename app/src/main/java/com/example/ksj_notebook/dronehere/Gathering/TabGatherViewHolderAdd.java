package com.example.ksj_notebook.dronehere.Gathering;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.ksj_notebook.dronehere.R;

/**
 * Created by ksj_notebook on 2016-06-05.
 */
public class TabGatherViewHolderAdd extends RecyclerView.ViewHolder {

   Button add_btn;
    public TabGatherViewHolderAdd(View itemView) {
        super(itemView);
        add_btn=(Button)itemView.findViewById(R.id.add_btn);

    }
    public void setAdd_btn(String string){
        add_btn.setText(string);
    }

}
