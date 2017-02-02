package com.example.ksj_notebook.dronehere.Drawer;


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
import com.example.ksj_notebook.dronehere.data.DrawerResult;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;
import com.example.ksj_notebook.dronehere.manager.PropertyManager;

import java.io.IOException;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerFragment extends Fragment {

    DrawerAdapter adapter;
    RecyclerView recyclerView;

    public DrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //adapter=new DrawerAdapter();
        String mem_id=PropertyManager.getInstance().getId();
        NetworkManager.getInstance().getDrawer(MyApplication.getContext(),mem_id, new NetworkManager.OnResultListener<DrawerResult>() {
            @Override
            public void onSuccess(Request request, DrawerResult result) {
                adapter.setMem(result.getResult(),getContext());
            }
            @Override
            public void onFail(Request request, IOException exception) {
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        adapter=new DrawerAdapter();
        View view= inflater.inflate(R.layout.fragment_blank, container, false);

        recyclerView=(RecyclerView)view.findViewById(R.id.drawer_recy);
        recyclerView.setAdapter(adapter);
        adapter.setOnLogoutClickListener(new DrawerAdapter.onLogoutClickListener() {
            @Override
            public void onLogoutClicked() {
                PropertyManager.getInstance().setId("");
                getActivity().finish();
                System.exit(0);
                //Toast.makeText(MyApplication.getContext(), "비회원으로 로그인 하려면 어플을 종료 후 다시 실행하세요.", Toast.LENGTH_LONG).show();
                //startActivity(new Intent(getContext(), StartActivity.class));
                //getActivity().finish();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();/*
        String mem_id=PropertyManager.getInstance().getId();
                        NetworkManager.getInstance().getDrawer(MyApplication.getContext(),mem_id, new NetworkManager.OnResultListener<DrawerResult>() {
                            @Override
                            public void onSuccess(Request request, DrawerResult result) {
                                adapter.setMem(result.getResult(),getContext());
                            }
                            @Override
                            public void onFail(Request request, IOException exception) {
                            }
                        });*/
    }
}
