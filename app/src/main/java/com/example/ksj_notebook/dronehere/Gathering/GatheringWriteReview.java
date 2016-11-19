package com.example.ksj_notebook.dronehere.Gathering;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;
import com.example.ksj_notebook.dronehere.manager.PropertyManager;

import java.io.IOException;

import okhttp3.Request;

public class GatheringWriteReview extends AppCompatActivity {

    String gt_id;

    TextView title;
    EditText text;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);


        title=(TextView)findViewById(R.id.comment_title);
        text=(EditText)findViewById(R.id.comment_edit);
        button=(Button)findViewById(R.id.comment_button);


        text.setHintTextColor(getResources().getColor(R.color.edithint));
        text.setHint(R.string.edit_text_hint5);


        title.setText(getIntent().getStringExtra("gt_name"));
        gt_id=getIntent().getStringExtra("gt_id");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _id= PropertyManager.getInstance().getId();
                NetworkManager.getInstance().getWriteGR(MyApplication.getContext(),gt_id,_id,""+text.getText(),new NetworkManager.OnResultListener() {


                    @Override
                    public void onSuccess(Request request, Object result) {
                    }

                    @Override
                    public void onFail(Request request, IOException exception) {
                    }
                });
                finish();


            }
        });
    }
}
