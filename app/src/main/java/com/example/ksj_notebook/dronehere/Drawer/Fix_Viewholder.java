package com.example.ksj_notebook.dronehere.Drawer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneDB;

/**
 * Created by ksj_notebook on 2016-06-14.
 */
public class Fix_Viewholder extends RecyclerView.ViewHolder {

    CheckBox checkBox;
    RadioButton radioButton;
    TextView drdr_fix;

    public Fix_Viewholder(View itemView) {
        super(itemView);
        checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        radioButton = (RadioButton) itemView.findViewById(R.id.radioButton);
        drdr_fix = (TextView) itemView.findViewById(R.id.drdr_fix);
    }

    public void setDrone1(DroneDB db) {
        drdr_fix.setText(db.getDr_name());
        if (getAdapterPosition() == 0) {
            radioButton.setChecked(true);
        }
        // drawer_drcheck.setImageResource();
    }
}
