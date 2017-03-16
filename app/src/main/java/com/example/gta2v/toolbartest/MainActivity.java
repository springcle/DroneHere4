package com.example.gta2v.toolbartest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            toolbar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);//title hidden
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back icon


  /*      final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);*/


        getSupportActionBar().setTitle("설정");


        toolbar.setNavigationOnClickListener(new View.OnClickListener() { //뒤로가기
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}

