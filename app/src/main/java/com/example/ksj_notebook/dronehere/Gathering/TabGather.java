package com.example.ksj_notebook.dronehere.Gathering;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.LocaContentResult;
import com.example.ksj_notebook.dronehere.data.LocaListResult;
import com.example.ksj_notebook.dronehere.data.Locatio;
import com.example.ksj_notebook.dronehere.data.POI;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabGather extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback
        ,GoogleMap.OnMarkerClickListener { //,MainActivity.onKeyBackPressedListener

    private long backKeyPressedTime = 0;
    GoogleApiClient mClient;
    GoogleMap mMap;
    Map<Marker, POI> poiResolver = new HashMap<>();
    Context context;

    Locatio locatio;

    SlidingUpPanelLayout sliding;

    TabGatherAdapter adapter;
    RecyclerView recyclerView;

    Location location;

    Button myLocation2;


    ImageButton flo_1;

    RelativeLayout zzzzz;

    CustomDialog2 dialog;

    Marker marker2;
    Handler mHandler = new Handler(Looper.getMainLooper());

    public TabGather() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
        mClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
//                .enableAutoManage((FragmentActivity) context, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        adapter = new TabGatherAdapter();
    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
    }



    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tab_gather, container, false);

        sliding = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.gath_recy);
        myLocation2=(Button)view.findViewById(R.id.myLocation2);

        flo_1=(ImageButton)view.findViewById(R.id.flo_1);

        zzzzz=(RelativeLayout)view.findViewById(R.id.zzzzz);
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentByTag("map");
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map_container2, mapFragment, "map").commit();
            mapFragment.getMapAsync(this);
        }

        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


  flo_1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          dialog=new CustomDialog2(getContext());
          dialog.show();
      }
  });

        adapter.setOnItemClickListener(new TabGatherAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(TabGatherViewHolderGather holder, View view, com.example.ksj_notebook.dronehere.data.Gathering s, int position) {

                Intent intent = new Intent(getActivity(), Gathering.class);
                intent.putExtra("gt",locatio.getLoca_name());
                intent.putExtra("gtid", s.get_id());
                startActivityForResult(intent,10);
            }

        });

        myLocation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location != null) {
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12f);
                    if (mMap != null) {
                        mMap.moveCamera(update);
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


            POI poi2 = poiResolver.get(marker2);
            NetworkManager.getInstance().getLocaContent(MyApplication.getContext(), poi2.getLoca_num(), new NetworkManager.OnResultListener<LocaContentResult>() {
                @Override
                public void onSuccess(Request request, LocaContentResult result) {

                    locatio = result.getResult();
                    adapter.setGather(locatio);
                }

                @Override
                public void onFail(Request request, IOException exception) {
                }
            });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(mClient);
        displayMessage(location);
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request, mListener);


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 권한 체크 부분
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                location = LocationServices.FusedLocationApi.getLastLocation(mClient);
                displayMessage(location);

            }
        }, 800);


        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(location.getLatitude(), location.getLongitude()));
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.i_loc1));
        mMap.addMarker(options);
    }





    LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
         //    displayMessage(location);
        }
    };

    private void displayMessage(Location location) {
        if (location != null) {


            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12f);


            if (mMap != null) {
                mMap.moveCamera(update);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMarkerClickListener(this);


        CameraUpdate update2 = CameraUpdateFactory.newLatLngZoom(new LatLng(38,128), 12f);
        mMap.moveCamera(update2);

    }

    @Override
    public void onResume() {
        super.onResume();


        NetworkManager.getInstance().getLocaList(MyApplication.getContext(), new NetworkManager.OnResultListener<LocaListResult>() {
            @Override
            public void onSuccess(Request request, LocaListResult result) {

                List<Locatio> localist= result.getResult();


                for(int i=0;i<localist.size();i++) {
                    POI poi=new POI();
                    poi.setLoca_num(localist.get(i).getLoca_num());
                    poi.setLati(localist.get(i).getLoca_latitude());
                    poi.setLongi(localist.get(i).getLoca_longitude());
                    poi.setList_num(localist.get(i).getGathe_list_num());
                    addMarker(poi);
                }
            }

            @Override
            public void onFail(Request request, IOException exception) {

            }
        });


        if(dialog!=null)
        dialog.dismiss();

    }



    @Override
    public boolean onMarkerClick(Marker marker) {
        POI poi = poiResolver.get(marker);
        marker2=marker;

        if(poi!=null) {
            NetworkManager.getInstance().getLocaContent(MyApplication.getContext(), poi.getLoca_num(), new NetworkManager.OnResultListener<LocaContentResult>() {
                @Override
                public void onSuccess(Request request, LocaContentResult result) {

                    locatio = result.getResult();
                    adapter.setGather(locatio);
                    sliding.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    sliding.setPanelHeight(convertToPixels(context, 183));
                }

                @Override
                public void onFail(Request request, IOException exception) {
                }
            });
        }


        return true;
    }



    private void addMarker(POI poi) {

        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(poi.getLati(), poi.getLongi()));
        options.icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.i_loc2,""+poi.getList_num())));
     //   options.title(""+poi.getList_num());
        Marker marker= mMap.addMarker(options);

        poiResolver.put(marker, poi);

    }



    //마커안에텍스트
    private Bitmap writeTextOnDrawable(int drawableId, String text) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setColor(getResources().getColor(R.color.marker));
        paint.setTextSize(convertToPixels(context, 15));
        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);
        Canvas canvas = new Canvas(bm);
        float xPos =  (float)canvas.getWidth()/2 - (float)(textRect.width()*0.55);
        float yPos =  (float)canvas.getHeight()/2  ;
        canvas.drawText(text, xPos, yPos, paint);
        return  bm;
    }
    public static int convertToPixels(Context context, int dp)
    {
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        float px=dp*(metrics.densityDpi/160f);
        return (int) px ;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //((MainActivity) activity).setOnKeyBackPressedListener(this);
    } // in SearchFragment

/**
    @Override
    public void onBack() {
/**
        if (sliding.getPanelState()!= SlidingUpPanelLayout.PanelState.HIDDEN){
            sliding.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }else{
            getActivity().finish();
         **/
/**
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            SystemExit();
        }
    }
    public void SystemExit() {
        getActivity().moveTaskToBack(true);
        getActivity().finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
    public void showGuide() {
        Toast.makeText(getActivity().getApplicationContext(),
                "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
    }

**/
    class CustomDialog2 extends Dialog {

        ImageButton flo_1_1;
        ImageButton flo_2;
        ImageButton flo_3;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            lpWindow.dimAmount = 0.8f;
            lpWindow.gravity= Gravity.CENTER;
            lpWindow.width=WindowManager.LayoutParams.MATCH_PARENT;
            lpWindow.height=WindowManager.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(lpWindow);

            setContentView(R.layout.write_dialog);

            flo_1_1=(ImageButton)findViewById(R.id.flo_1_1);
            flo_2=(ImageButton)findViewById(R.id.flo_2);
            flo_3=(ImageButton)findViewById(R.id.flo_3);

            flo_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), TabGatherWriteReview.class);
                    intent.putExtra("lc_name", locatio.getLoca_name());
                    intent.putExtra("lc_id",locatio.get_id());
                    startActivityForResult(intent,20);
                }
            });
            flo_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(getActivity(),TabGatherWriteGathering.class); //모임쓰기
                    intent.putExtra("lc_id",locatio.get_id());
                    intent.putExtra("lc_name",locatio.getLoca_name());
                    startActivityForResult(intent, 10);
                }
            });
            flo_1_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        public CustomDialog2(Context context) {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);
        }
    }






}
