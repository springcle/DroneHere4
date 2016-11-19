package com.example.ksj_notebook.dronehere.News;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.News;

/**
 * Created by ksj_notebook on 2016-05-18.
 */
public class NewsViewHolder extends RecyclerView.ViewHolder {

    NewsAdapter.OnItemClickListener mItemClickListener;
    public void setOnItemClickListener(NewsAdapter.OnItemClickListener listener){
        mItemClickListener = listener;
    }

    TextView titleView;
    TextView dateView;
    TextView providerView;
    RelativeLayout newsClick;
    String address;


    public NewsViewHolder(final View itemView) {
        super(itemView);

        titleView = (TextView)itemView.findViewById(R.id.newsTitle);
        dateView = (TextView)itemView.findViewById(R.id.newsDate);
        providerView = (TextView)itemView.findViewById(R.id.newsProvider);
        newsClick=(RelativeLayout)itemView.findViewById(R.id.newsClick);
        newsClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClicked(NewsViewHolder.this, itemView, address, getAdapterPosition());
                }
            }
        });
    }

    public void setArticle(News news){
        address=news.getNews_url();
        titleView.setText(news.getNews_title());
        dateView.setText(news.getNews_date());
        providerView.setText(news.getNews_press());
    }
}
