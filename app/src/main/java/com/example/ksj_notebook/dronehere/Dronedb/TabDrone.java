package com.example.ksj_notebook.dronehere.Dronedb;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneRecommendResult;
import com.example.ksj_notebook.dronehere.data.DroneSearchResult;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;

import java.io.IOException;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabDrone extends Fragment {

    RecyclerView recyclerView;
    Spinner spinner;
    TabDroneAdapter db2;
    EditText editText;
    Button button;
    TextView gone_text;
    int cnt=0;
    LinearLayoutManager layoutManager;

    ImageView logo;

    public TabDrone() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db2=new TabDroneAdapter();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_db, container, false);
        spinner = (Spinner)view.findViewById(R.id.spinner);
        recyclerView=(RecyclerView)view.findViewById(R.id.ryview);
        editText=(EditText)view.findViewById(R.id.drone_search);
        button=(Button)view.findViewById(R.id.drone_search_btn);
        gone_text=(TextView)view.findViewById(R.id.gone_text);
        //logo=(ImageView)view.findViewById(R.id.imageView6);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.sort, R.layout.spinner_view);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        recyclerView.setAdapter(db2);
        recyclerView.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        editText.setHint(R.string.drone_pick);

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(cnt==0){
                    layoutManager.setStackFromEnd(false);
                    recyclerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    recyclerView.setLayoutManager(layoutManager);
                }
                gone_text.setVisibility(View.INVISIBLE);
                //logo.setVisibility(View.INVISIBLE);
                String get=""+editText.getText();
                if(get.equals("")){
                    onResume();
                }else{
                    NetworkManager.getInstance().getDroneSearch(MyApplication.getContext(),get ,new NetworkManager.OnResultListener<DroneSearchResult>() {

                        @Override
                        public void onSuccess(Request request, DroneSearchResult result) {
                            db2.setDb2(result.getResult());
                        }

                        @Override
                        public void onFail(Request request, IOException exception) {

                        }
                    });
                }
                cnt++;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

       db2.setOnItemClickListener(new TabDroneAdapter.OnItemClickListener() {
           @Override
           public void onItemClicked(TabDroneViewholder holder, View view, String s, int position) {
               Intent intent = new Intent(getActivity(), DroneDetail.class);
               intent.putExtra("_id", s);
               startActivity(intent);
           }
       });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //cnt=0;
        //logo.setVisibility(View.VISIBLE);
        gone_text.setVisibility(View.VISIBLE);
        layoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(layoutManager);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        NetworkManager.getInstance().getDroneRecommendName(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
                            @Override
                            public void onSuccess(Request request, DroneRecommendResult result) {
                                db2.setDb2(result.getResult());
                            }
                            @Override
                            public void onFail(Request request, IOException exception) {
                            }
                        });
                        break;
                    case 1:
                        NetworkManager.getInstance().getDroneRecommendRate(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
                            @Override
                            public void onSuccess(Request request, DroneRecommendResult result) {
                                db2.setDb2(result.getResult());
                            }
                            @Override
                            public void onFail(Request request, IOException exception) {
                            }
                        });
                        break;
                    case 2:
                        NetworkManager.getInstance().getDroneRecommendBrand(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
                            @Override
                            public void onSuccess(Request request, DroneRecommendResult result) {
                                db2.setDb2(result.getResult());
                            }

                            @Override
                            public void onFail(Request request, IOException exception) {

                            }
                        });
                        break;
                    case 3:
                        NetworkManager.getInstance().getDroneRecommendUsage(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
                            @Override
                            public void onSuccess(Request request, DroneRecommendResult result) {
                                db2.setDb2(result.getResult());
                            }

                            @Override
                            public void onFail(Request request, IOException exception) {

                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}
