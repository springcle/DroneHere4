package com.example.ksj_notebook.dronehere.Dronedb;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneDB;

import java.util.List;

/**
 * Created by ksj_notebook on 2016-05-29.
 */
public class TabDroneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {mListener=listener; }

    public interface OnItemClickListener {
        public void onItemClicked(TabDroneViewholder holder, View view, String s, int position);
    }

    List<DroneDB> db;

    public void setDb2(List<DroneDB> db){
        this.db=db;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(db==null) return 0;
        return db.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.db_item_view, null);
        return new TabDroneViewholder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TabDroneViewholder) holder).setText(db.get(position));
        ((TabDroneViewholder) holder).setOnItemClickListener(mListener);
    }

}
