package com.example.ksj_notebook.dronehere.Drawercontent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.Dc1d;

import java.util.List;

/**
 * Created by ksj_notebook on 2016-06-15.
 */
public class Dc1Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Dc1d> dc1d;

    public void setdc11(List<Dc1d> dc1d){
        this.dc1d=dc1d;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.dc1_layout, parent, false);
        return new Dc1ViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((Dc1ViewHolder)holder).setdc1(dc1d.get(position));
    }

    @Override
    public int getItemCount() {
        if(dc1d==null) return 0;
        return dc1d.size();
    }
}
