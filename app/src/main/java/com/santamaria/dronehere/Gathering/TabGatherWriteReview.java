package com.santamaria.dronehere.Gathering;

        import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
        import android.widget.Toast;

        import com.santamaria.dronehere.MyApplication;
import com.santamaria.dronehere.R;
import com.santamaria.dronehere.manager.NetworkManager;
import com.santamaria.dronehere.manager.PropertyManager;

import java.io.IOException;

import okhttp3.Request;

public class TabGatherWriteReview extends AppCompatActivity {

    Button button;
    TextView rvtitle;
    EditText edit;
    RatingBar ratingBar;

    String lc_name;
    String lc_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_gather_write_review);

        rvtitle=(TextView)findViewById(R.id.RvWrite_title);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        button=(Button)findViewById(R.id.RvWrite_button);
        edit=(EditText)findViewById(R.id.editt);

        lc_name=getIntent().getStringExtra("lc_name");
        lc_id=getIntent().getStringExtra("lc_id");


        rvtitle.setText(lc_name);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mem_id= PropertyManager.getInstance().getId();
                NetworkManager.getInstance().getWriteLocaReview(MyApplication.getContext(),lc_id,mem_id ,ratingBar.getRating(),""+edit.getText(),new NetworkManager.OnResultListener() {

                    @Override
                    public void onSuccess(Request request, Object result) {

                    }

                    @Override
                    public void onFail(Request request, IOException exception) {

                    }
                });
                Toast.makeText(TabGatherWriteReview.this, "", Toast.LENGTH_SHORT).show();
                setResult(10);
                finish();

            }
        });

        edit.setHint(R.string.edit_text_hint);


    }
}