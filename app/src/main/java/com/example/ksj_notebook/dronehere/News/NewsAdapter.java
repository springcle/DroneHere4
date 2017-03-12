package com.example.ksj_notebook.dronehere.News;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.News;

import java.util.List;

/**
 * Created by ksj_notebook on 2016-05-18.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {
    public interface OnItemClickListener {
        public void onItemClicked(NewsViewHolder holder, View view, String s, int position);
    }

    OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {mListener=listener;   }

    List<News> news;


    public void setNews(List<News> news){
        this.news=news;
        notifyDataSetChanged();
        }

    @Override
     public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_news_item,null);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.setArticle(news.get(position));
        holder.setOnItemClickListener(mListener);
    }



    @Override
    public int getItemCount() {
        if(news==null) return 0;
        return news.size();
    }


}
