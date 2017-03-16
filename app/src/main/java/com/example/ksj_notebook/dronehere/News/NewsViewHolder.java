package com.example.ksj_notebook.dronehere.News;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
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
    LinearLayout newsClick;
    String address;

    public NewsViewHolder(final View itemView) {
        super(itemView);
        titleView = (TextView)itemView.findViewById(R.id.newsTitle);
        titleView.setHorizontallyScrolling(false);
        dateView = (TextView)itemView.findViewById(R.id.newsDate);
        dateView.setHorizontallyScrolling(false);
        providerView = (TextView)itemView.findViewById(R.id.newsProvider);
        providerView.setHorizontallyScrolling(false);
        newsClick=(LinearLayout)itemView.findViewById(R.id.newsClick);
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
        /** &quot; 이라는 문자열 공백으로 리플레이스 해줬음 **/
        titleView.setText(news.getNews_title().replace("&quot;",""));
        titleView.setTypeface(Typeface.DEFAULT);

        dateView.setText(news.getNews_date());
        providerView.setText(news.getNews_press());
    }
}
