package com.example.ksj_notebook.dronehere.Drawercontent;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.Dc1d;

import java.text.SimpleDateFormat;

/**
 * Created by ksj_notebook on 2016-06-15.
 */
public class Dc1ViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView star;
    TextView content;
    TextView date;

    SimpleDateFormat format;

    public Dc1ViewHolder(View itemView) {
        super(itemView);

        format=new SimpleDateFormat("MM.dd");
        name=(TextView)itemView.findViewById(R.id.dc1name);
        star=(TextView)itemView.findViewById(R.id.dc1star);
        content=(TextView)itemView.findViewById(R.id.dc1content);
        date=(TextView)itemView.findViewById(R.id.dc1date);

    }
    public void setdc1(Dc1d dc1d){
        name.setText(dc1d.getLoca_name());
        star.setText("별점 "+dc1d.getRe_rate());
        content.setText(dc1d.getRe_content());
        date.setText(format.format(dc1d.getRe_regdate()));
    }

    public void setdc2(Dc1d dc1d){
        name.setText(dc1d.getGathe_name());
        star.setText(dc1d.getLoca_name());
        content.setText(dc1d.getRe_content());
        date.setText(format.format(dc1d.getRe_regdate()));
    }

    public void setdc3(Dc1d dc1d){
        name.setText(dc1d.getDr_name());
        star.setText("별점 "+dc1d.getRe_rate());
        content.setText(dc1d.getRe_content());
        date.setText(format.format(dc1d.getRe_regdate()));
    }
}
