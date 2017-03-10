package com.example.ksj_notebook.dronehere.Dronedb;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.ksj_notebook.dronehere.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabDrone extends Fragment {

    SelectCategory selectCategory;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        /** 픽셀 = dp 곱하기 3 **/
        spinner.setDropDownVerticalOffset(105);
        spinner.setDropDownWidth(300);

        recyclerView.setAdapter(db2);
        recyclerView.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        editText.setHint(R.string.drone_pick);
        selectCategory =  new SelectCategory(getContext(), spinner, editText, db2, layoutManager, recyclerView);

        selectCategory.text_listener();
        selectCategory.recommand_list();
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
        /** 스피너 아이템 선택 리스너 **/
        selectCategory.select_spinner();
    }
}
