package com.example.ksj_notebook.dronehere.News;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.News;
import com.example.ksj_notebook.dronehere.data.NewsResult;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabNews extends Fragment {

    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    List<News> news;

    public TabNews() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newsAdapter = new NewsAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tab_news, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.newslist);
        view.setBackgroundColor(Color.rgb(234,234,234));

        recyclerView.setAdapter(newsAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        NetworkManager.getInstance().getNews(MyApplication.getContext(), new NetworkManager.OnResultListener<NewsResult>() {
            @Override
            public void onSuccess(Request request, NewsResult result) {
                news= result.getResult();

                newsAdapter.setNews(news);
            }

            @Override
            public void onFail(Request request, IOException exception) {            }
        });

        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(NewsViewHolder holder, View view, String s, int position) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
                startActivity(intent);
            }
        });
        return view;
    }

}
