package com.santamaria.dronehere.Gathering;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.santamaria.dronehere.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TabGatherWriteImagePicker extends AppCompatActivity implements GridView.OnItemClickListener {


    Button btn;
    GridView mGrid;
    Cursor mCursor;
    List<String> imageUrl = new ArrayList<>();
    ImageAdapter adapter;

    Set<Integer> mapp=new HashSet<>();


    int cnt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        btn = (Button) findViewById(R.id.picker_btn);

        mGrid = (GridView) findViewById(R.id.grid);

        ContentResolver cr = getContentResolver();
        mCursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        adapter = new ImageAdapter(this);
        mGrid.setAdapter(adapter);
        mGrid.setOnItemClickListener(this);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               List<Integer> mappp=new ArrayList(mapp);

                for(int i=0;i<mappp.size();i++){
                    int j=mappp.get(i);


                        mCursor.moveToPosition(j);
                        final String path2 = mCursor.getString(mCursor.getColumnIndex(
                                MediaStore.Images.ImageColumns.DATA));
                        imageUrl.add(path2);

                }

                Intent intent = new Intent();
                intent.putStringArrayListExtra("imaUrl", (ArrayList<String>) imageUrl);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });


    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(mGrid.isItemChecked(position)){
            view.setBackgroundColor(Color.BLACK);
        }else{
            view.setBackgroundColor(Color.WHITE);
        }

    }





    class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mCursor.getCount();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public class GridViewholder {
            ImageView im1;
            ImageView im2;
        }
        public View getView(final int position, View convertView, ViewGroup parent) {

            final GridViewholder holder;


            if (convertView == null) {
                holder=new GridViewholder();
                LayoutInflater ltInflate = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                convertView = ltInflate.inflate(R.layout.grid_viewholder, null);
                holder.im1=(ImageView)convertView.findViewById(R.id.imageView2);
                holder.im2=(ImageView)convertView.findViewById(R.id.imageView3);
                convertView.setTag(holder);
            } else {
                holder = (GridViewholder) convertView.getTag();
            }

            mCursor.moveToPosition(position);
            final String path = mCursor.getString(mCursor.getColumnIndex(
                    MediaStore.Images.ImageColumns.DATA));


            if (path != null) {
                Glide.with(getApplicationContext())
                        .load(Uri.fromFile(new File(path)))
                        .override(450, 450)
                        .centerCrop()
                        .into(holder.im1);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(holder.im2.getVisibility()==View.VISIBLE){
                        holder.im2.setVisibility(View.INVISIBLE);
                        mapp.remove(position);
                        cnt--;
                    }else{
                        if(cnt<5){
                            holder.im2.setVisibility(View.VISIBLE);
                            mapp.add(position);
                            cnt++;
                        }

                    }

                }
            });


            return convertView;


//
//            RelativeLayout frameLayout;
//            ImageView imageView = new ImageView(mContext);
//
//            if (convertView == null) {
//                frameLayout = new RelativeLayout(mContext);
//            } else {
//                frameLayout = (RelativeLayout) convertView;
//            }
//            mCursor.moveToPosition(position);
//            final String path = mCursor.getString(mCursor.getColumnIndex(
//                    MediaStore.Images.ImageColumns.DATA));
//
//
//            if (path != null) {
//                Glide.with(getApplicationContext())
//                        .load(Uri.fromFile(new File(path)))
//                        .override(450, 450)
//                        .centerCrop()
//                        .into(imageView);
//            }
//
//            frameLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                }
//            });
//
//            frameLayout.addView(imageView);
//            return frameLayout;

   }
}
}

