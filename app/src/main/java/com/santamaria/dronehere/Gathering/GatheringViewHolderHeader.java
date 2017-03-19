package com.santamaria.dronehere.Gathering;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.santamaria.dronehere.R;
import com.santamaria.dronehere.data.Gathering;

/**
 * Created by ksj_notebook on 2016-05-24.
 */
public class GatheringViewHolderHeader extends RecyclerView.ViewHolder{

    TextView date;
    TextView content;
    Button button;
    RelativeLayout relll;

    public GatheringViewHolderHeader(View itemView) {
        super(itemView);

        date=(TextView)itemView.findViewById(R.id.gt_title);
        content=(TextView)itemView.findViewById(R.id.gt_content);
        button=(Button)itemView.findViewById(R.id.gt_btn);
        relll=(RelativeLayout)itemView.findViewById(R.id.relll);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                relll.setBackgroundResource(R.color.white);
            }
        });
    }
    public void setHeader(Gathering gathering){
        date.setText("일시:"+gathering.getGathe_regdate());
        content.setText(gathering.getGathe_content());
    }
}
