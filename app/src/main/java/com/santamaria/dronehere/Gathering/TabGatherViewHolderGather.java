package com.santamaria.dronehere.Gathering;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.santamaria.dronehere.R;
import com.santamaria.dronehere.data.Gathering;

/**
 * Created by ksj_notebook on 2016-05-21.
 */
public class TabGatherViewHolderGather extends RecyclerView.ViewHolder{

    TextView dday;
    TextView title;
    TextView nick;
    TextView date;
    RelativeLayout lin;
    Gathering data;
    RelativeLayout rel;




    public TabGatherViewHolderGather(final View itemView) {
        super(itemView);
        dday=(TextView)itemView.findViewById(R.id.gath_gathering_dday);
        title=(TextView)itemView.findViewById(R.id.gath_gathering_title);
        nick=(TextView)itemView.findViewById(R.id.gath_gathering_nick);
        date=(TextView)itemView.findViewById(R.id.gath_gathering_date);
        rel=(RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        lin=(RelativeLayout) itemView.findViewById(R.id.gt_item);
        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener != null){
                    mItemClickListener.onItemClicked(TabGatherViewHolderGather.this,itemView ,data,getAdapterPosition());
                }
            }
        });

    }

    public void setGathering(Gathering gathering){
        data=gathering;//

        String time = gathering.getGathe_regdate();

        String[] val=time.split("T");
        String[] val2=val[0].split("-");


        dday.setText("D-"+(gathering.getDday()-1));
        title.setText(gathering.getGathe_name());
        nick.setText((gathering.getGathe_memname()));
        date.setText(val2[1]+"."+val2[2]);
    }

    TabGatherAdapter.OnItemClickListener mItemClickListener;
    public void setOnItemClickListener(TabGatherAdapter.OnItemClickListener listener){
        mItemClickListener = listener;
    }
}
