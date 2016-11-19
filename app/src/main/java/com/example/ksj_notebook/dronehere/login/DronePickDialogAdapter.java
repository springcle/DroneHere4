package com.example.ksj_notebook.dronehere.login;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneDB;

import java.util.List;

/**
 * Created by ksj_notebook on 2016-06-09.
 */
public class DronePickDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        public void onItemClicked(DbSearchViewHolder holder, View view, DroneDB s, int position);
    }

    OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



    List<DroneDB> db;

    public void setDb3(List<DroneDB> db){
        this.db=db;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.db_search_item, parent,false);
        return new DbSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DbSearchViewHolder) holder).setList(db.get(position));
        ((DbSearchViewHolder)holder).setOnItemClickListener(mListener);
    }

    @Override
    public int getItemCount() {
        if(db==null)return 0;
        return db.size();
    }
}
