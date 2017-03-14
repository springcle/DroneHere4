package com.example.ksj_notebook.dronehere.Drawercontent;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.example.ksj_notebook.dronehere.BaseActivity;
import com.example.ksj_notebook.dronehere.R;

import java.util.List;

public class Dc4 extends BaseActivity {

    List<String> bit;
    ViewPager pager3;
    Dc4Adapter adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dc4);


        Toast.makeText(Dc4.this, "오른쪽으로 넘기세요.", Toast.LENGTH_SHORT).show();
        pager3=(ViewPager)findViewById(R.id.pager3);
        adp=new Dc4Adapter();
        pager3.setAdapter(adp);
        adp.setImage();
    }
}
