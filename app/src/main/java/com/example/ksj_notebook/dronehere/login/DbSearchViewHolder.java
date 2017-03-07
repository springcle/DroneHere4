package com.example.ksj_notebook.dronehere.login;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneDB;

/**
 * Created by ksj_notebook on 2016-06-09.
 */
public class DbSearchViewHolder extends RecyclerView.ViewHolder {

    TextView drname;
    TextView drmanu;
    LinearLayout linn;
    DroneDB data;

    public DbSearchViewHolder(final View itemView) {
        super(itemView);
        drname=(TextView)itemView.findViewById(R.id.search_drname);
        drmanu=(TextView)itemView.findViewById(R.id.search_manu);
        linn=(LinearLayout)itemView.findViewById(R.id.item_linn);
        linn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener != null){
                    mItemClickListener.onItemClicked(DbSearchViewHolder.this,itemView ,data,getAdapterPosition());
                }
            }
        });
    }

    public void setList(DroneDB db){
        data=db;
        drname.setText(db.getDr_name());
        drmanu.setText(db.getDr_manufacture());
    }

    DronePickDialogAdapter.OnItemClickListener mItemClickListener;
    public void setOnItemClickListener(DronePickDialogAdapter.OnItemClickListener listener){
        mItemClickListener = listener;
    }
}
