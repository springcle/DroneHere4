package com.santamaria.dronehere.Gathering;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.santamaria.dronehere.MyApplication;
import com.santamaria.dronehere.R;
import com.santamaria.dronehere.manager.NetworkManager;
import com.santamaria.dronehere.manager.PropertyManager;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Request;

public class TabGatherWriteGathering extends AppCompatActivity{


    Button addBtn;
    Button gtBtn;
    TextView gtTitle;

    List<File> photo= new ArrayList<>();

    LinearLayout hori;
    List<String> imageList;

    EditText editText1;
    Button editText2;
    EditText editText3;
    EditText editText4;

    String loca_name;
    String loca_id;


    private SimpleDateFormat dateformat1 = new SimpleDateFormat("MMMM / yyyy", Locale.ENGLISH);
    private SimpleDateFormat dateFormat2= new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());

    final static int IM=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_gathering);

        addBtn = (Button) findViewById(R.id.gtImage_btn);
        gtBtn = (Button) findViewById(R.id.gt_btn);
        gtTitle = (TextView) findViewById(R.id.gtTitle);
        hori = (LinearLayout) findViewById(R.id.hori);
        editText1=(EditText)findViewById(R.id.editText111);
        editText2=(Button)findViewById(R.id.editText222);
        editText3=(EditText)findViewById(R.id.editText333);
        editText4=(EditText)findViewById(R.id.editText444);

        editText1.setHint(R.string.edit_text_hint1);
        editText2.setHint(R.string.edit_text_hint2);
        editText3.setHint(R.string.edit_text_hint3);
        editText4.setHint(R.string.edit_text_hint4);



        loca_name = getIntent().getStringExtra("lc_name");
        loca_id=getIntent().getStringExtra("lc_id");

        gtTitle.setText(loca_name);


        editText2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CustomDialog4 dialog=new CustomDialog4(TabGatherWriteGathering.this);
                dialog.show();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(getApplicationContext(), TabGatherWriteImagePicker.class);
                startActivityForResult(intent, IM);
            }
        });
        gtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText1.getText().toString().equals("")&&editText1.getText().toString().equals("")&&editText1.getText().toString().equals("")&&editText1.getText().toString().equals("")){
                    Toast.makeText(TabGatherWriteGathering.this, "필수 사항을 입력해 주세요", Toast.LENGTH_SHORT).show();
                }else{
                    if (imageList!=null){
                        String mem_id=PropertyManager.getInstance().getId();
                        NetworkManager.getInstance().getWriteGath(MyApplication.getContext(),loca_id,mem_id,""+editText1.getText(),""+editText2.getText(),""+editText3.getText(),""+editText4.getText(),photo ,new NetworkManager.OnResultListener() {

                            @Override
                            public void onSuccess(Request request, Object result) {
                                finish();
                            }
                            @Override
                            public void onFail(Request request, IOException exception) {

                            }
                        });
                    }else{
                        Toast.makeText(TabGatherWriteGathering.this, "사진을 한장 이상 추가해 주세요", Toast.LENGTH_SHORT).show();
                    }
                }





            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode==IM)&&(resultCode== Activity.RESULT_OK))
            hori.removeAllViews();
        if(data!=null){
            imageList= data.getStringArrayListExtra("imaUrl");
        }


        if (imageList != null) {

            for (int j=0; j<imageList.size();j++) {
                ImageView im = new ImageView(getApplicationContext());
                Glide.with(getApplicationContext())
                        .load(Uri.fromFile(new File(imageList.get(j))))
                        .override(200, 150)
                        .centerCrop()
                        .into(im);

                im.setPadding(0,0,5,0);
                hori.addView(im);

                photo.add(new File(imageList.get(j)));
            }


        }
    }



    class CustomDialog4 extends Dialog {


        CompactCalendarView cal;
        TextView tex;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            lpWindow.dimAmount = 0.8f;
            lpWindow.gravity= Gravity.CENTER;
            lpWindow.width=WindowManager.LayoutParams.MATCH_PARENT;
            lpWindow.height=WindowManager.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(lpWindow);
            setContentView(R.layout.datepic_dial);

            cal=(CompactCalendarView)findViewById(R.id.compactcalendar_view);
            tex=(TextView)findViewById(R.id.datepic_tx);
            tex.setText(dateformat1.format(new Date()));

            cal.setLocale(Locale.ENGLISH);
            cal.setUseThreeLetterAbbreviation(true);

            cal.setListener(new CompactCalendarView.CompactCalendarViewListener() {
                @Override
                public void onDayClick(Date dateClicked) {

                    editText2.setText(dateFormat2.format(dateClicked));
                    dismiss();
                }

                @Override
                public void onMonthScroll(Date firstDayOfNewMonth) {
                    tex.setText(dateformat1.format(firstDayOfNewMonth));
                }
            });

        }
        public CustomDialog4(Context context) {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);

        }
    }


}
