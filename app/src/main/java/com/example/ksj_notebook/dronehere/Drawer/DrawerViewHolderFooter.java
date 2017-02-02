package com.example.ksj_notebook.dronehere.Drawer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.ksj_notebook.dronehere.R;

/**
 * Created by ksj_notebook on 2016-06-03.
 */
public class DrawerViewHolderFooter extends RecyclerView.ViewHolder {

    Button btn1;
    Button btn2;
    Button btn3;

    Button etc_btn;

    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;

    Button btn8;
    Button btn9;

    public DrawerViewHolderFooter(View itemView) {
        super(itemView);
        btn1 = (Button) itemView.findViewById(R.id.button5);
        btn2 = (Button) itemView.findViewById(R.id.button6);
        btn3 = (Button) itemView.findViewById(R.id.button7);

        etc_btn = (Button) itemView.findViewById(R.id.etc_btn);
/*
        btn4=(Button)itemView.findViewById(R.id.button8);
        btn5=(Button)itemView.findViewById(R.id.button9);
        btn6=(Button)itemView.findViewById(R.id.button10);
        btn7=(Button)itemView.findViewById(R.id.button11);
        btn8=(Button)itemView.findViewById(R.id.button12);
        btn9=(Button)itemView.findViewById(R.id.button13);
        */
    }

    public  void setFooter(){

    }
}
