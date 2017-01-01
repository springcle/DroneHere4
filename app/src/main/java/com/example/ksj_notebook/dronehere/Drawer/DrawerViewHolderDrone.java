package com.example.ksj_notebook.dronehere.Drawer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneDB;

/**
 * Created by ksj_notebook on 2016-06-03.
 */
public class DrawerViewHolderDrone extends RecyclerView.ViewHolder {

    Button drawer_drname;
    ImageView drawer_drcheck;

    public DrawerViewHolderDrone(View itemView) {
        super(itemView);

        drawer_drname=(Button)itemView.findViewById(R.id.drawer_drname);
        drawer_drcheck=(ImageView) itemView.findViewById(R.id.drawer_drcheck);

    }

    public void setDrone(DroneDB db){
        drawer_drname.setText(db.getDr_name());
        if(getAdapterPosition()==1) {
            drawer_drcheck.setVisibility(View.VISIBLE);
        }else{
            drawer_drcheck.setVisibility(View.INVISIBLE);
        }
       // drawer_drcheck.setImageResource();
    }
}
