package com.example.ksj_notebook.dronehere.Drawer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.Member;

/**
 * Created by ksj_notebook on 2016-06-03.
 */
public class DrawerViewHolderHeader extends RecyclerView.ViewHolder {

    TextView drawer_nick;
    Button drawer_fix;
    //Button drawer_dr_plus;
    Button drawer_logout;
    ImageView drawer_image;

    public DrawerViewHolderHeader(View itemView) {
        super(itemView);
        drawer_nick=(TextView)itemView.findViewById(R.id.drawer_nick);
        drawer_fix=(Button) itemView.findViewById(R.id.drawer_fix);
        drawer_logout=(Button)itemView.findViewById(R.id.drawer_logout);
        drawer_image=(ImageView)itemView.findViewById(R.id.drawer_image);
        //drawer_dr_plus=(Button) itemView.findViewById(R.id.drawer_dr_plus);
    }
    public void setNick(Member member){
        drawer_nick.setText(member.getMem_name());
            GlideUrl url = new GlideUrl(member.getDr_photo());
            Glide.with(MyApplication.getContext())
                    .load(url)
                    .into(drawer_image);
    }
}
