package com.example.ksj_notebook.dronehere.Here;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.MagneticResult;
import com.example.ksj_notebook.dronehere.data.WeatherResult;
import com.example.ksj_notebook.dronehere.login.StartActivity;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;
import com.example.ksj_notebook.dronehere.manager.PropertyManager;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.kml.KmlContainer;
import com.google.maps.android.kml.KmlLayer;
import com.google.maps.android.kml.KmlPlacemark;
import com.google.maps.android.kml.KmlPolygon;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabHere extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback
        , GoogleMap.OnCameraChangeListener, GoogleMap.OnMarkerClickListener {

    GoogleApiClient mClient;
    GoogleMap mMap;
    Context context;
    List<String> imageUrl;
    List<Bitmap> bitmaps;
    LatLngBounds bounds;
    Location location;
 //   ToggleButton weightToggle;
    Button myLocation;
    Marker marker;

    LayoutInflater inflater;

    int kk;
    String sunrise;
    String sunset;
    String wind;

    String mem_id=PropertyManager.getInstance().getId();

    KmlLayer layer1;

    int[] bool={1,1,1,1};

    public TabHere() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        mClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_here, container, false);

        myLocation = (Button) view.findViewById(R.id.myLocation);
  //      weightToggle = (ToggleButton) view.findViewById(R.id.weightToggle);

        this.inflater = inflater;

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentByTag("map");
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map_container1, mapFragment, "map").commit();
            mapFragment.getMapAsync(this);

        }

//        weightToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked==true){
//                    layer2.removeLayerFromMap();
//                    try {
//                        layer1.addLayerToMap();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (XmlPullParserException e) {
//                        e.printStackTrace();
//                    }
//
//                }else{
//                    layer1.removeLayerFromMap();
//                    try {
//                        layer2.addLayerToMap();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (XmlPullParserException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });


        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location != null) {
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12f);
                    //37.47547965,126.95924163
                    if (mMap != null) {
                        mMap.moveCamera(update);
                    }
                }
            }
        });
        return view;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(mClient);
        displayMessage(location);
        LocationRequest request = new LocationRequest();
        request.setInterval(3000); //원래 4000이었음
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request, mListener);
        getData();
    }

    LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            //getData();
            if(bool!=null){
                addMarker(location);
            }
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraChangeListener(this);


//        CircleOptions circleOptions = new CircleOptions()
//                .center(new LatLng(35.4751, 129.1954))
//                .radius(14000)
//                .fillColor(R.color.drgreen)
//                .strokeColor(R.color.drgreen);
//        mMap.addCircle(circleOptions);



        try {
            layer1=new KmlLayer(mMap,R.raw.zz,getContext());
//            layer2=new KmlLayer(mMap,R.raw.zz2,getContext());
            layer1.addLayerToMap();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // layer = new KmlLayer(getMap(), R.raw.kmlFile, getApplicationContext());
//
//        mMap.addPolygon(new PolygonOptions()
//                .add(new LatLng(126.664722 ,35.419722),new LatLng(126.664722 ,35.369722),new LatLng(126.414722 ,35.169722),new LatLng( 126.248055, 35.169722),new LatLng(126.248055 ,35.219722),new LatLng(126.498055, 35.419722),new LatLng(126.664722 ,35.419722))
//                .strokeColor(Color.RED)
//                .fillColor(Color.RED));
//
//        mMap.addPolygon(new PolygonOptions()
//                .add(new LatLng(126.664722,35.369722),new LatLng(126.664722, 35.169722),new LatLng(126.414722, 35.169722),new LatLng(126.664722 ,35.369722))
//                .strokeColor(Color.RED)
//                .fillColor(Color.RED));
//
//        mMap.addPolygon(new PolygonOptions()
//                .add(new LatLng(126.331389, 35.503055),new LatLng(126.331389, 35.303055),new LatLng(125.998055, 35.303055),new LatLng(125.998055, 35.503055),new LatLng(126.331389, 35.503055))
//                .strokeColor(Color.RED)
//                .fillColor(Color.RED));





//        bounds = new LatLngBounds(new LatLng(32.35000, 124.100), new LatLng(39.4500, 131.2000));
//        GroundOverlayOptions overlayOptions2 = new GroundOverlayOptions().
//                image(BitmapDescriptorFactory.fromResource(R.drawable.m2)).positionFromBounds(bounds);
//        mMap.addGroundOverlay(overlayOptions2);


    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {


//            GroundOverlayOptions overlayOptions1 = new GroundOverlayOptions().
//                    image(BitmapDescriptorFactory.fromResource(R.drawable.m22)).positionFromBounds(bounds);
//            mMap.addGroundOverlay(overlayOptions1);
    }


    private void addMarker(Location location) {

        if(marker!=null)marker.remove();


        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(location.getLatitude(), location.getLongitude()));

            int z=1;
            for (int j = 0; j < 4; j++) {
                if (bool[j] == 0) {
                    z=0;
                    break;
                }
            }
        //
        if(PropertyManager.getInstance().getId() != "") {
            if (z == 0) {
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_imp1_1));
                marker = mMap.addMarker(options);
            } else {
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_pos1_1));
                marker = mMap.addMarker(options);
            }
        }
        else {
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_imp1_1_unable));
            marker = mMap.addMarker(options);
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {


        CustomDialog dialog=new CustomDialog(getContext(),bool);
        dialog.show();
        return false;
    }


    class CustomDialog extends Dialog{

        ImageView i1;
        ImageView i2;
        ImageView i3;
        ImageView i4;
        int[] bool;
        Button btn;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            lpWindow.dimAmount = 0.8f;
            lpWindow.gravity= Gravity.CENTER;
            lpWindow.width=WindowManager.LayoutParams.WRAP_CONTENT;
            lpWindow.height=WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(lpWindow);

            setContentView(R.layout.location_dialog);

            i1=(ImageView)findViewById(R.id.dial_image1);
            i2=(ImageView)findViewById(R.id.dial_image2);
            i3=(ImageView)findViewById(R.id.dial_image3);
            i4=(ImageView)findViewById(R.id.dial_image4);
            btn=(Button)findViewById(R.id.dial_btn);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity().getApplicationContext(), StartActivity.class));
                    dismiss();
                    getActivity().finish();
                }
            });

            if(bool[0]==1) i1.setImageResource(R.drawable.i_pos1);
            else i1.setImageResource(R.drawable.i_imp1);
            if(PropertyManager.getInstance().getId() != "") {
                if (bool[1] == 1) i2.setImageResource(R.drawable.i_pos2);
                else i2.setImageResource(R.drawable.i_imp2);
                if (bool[2] == 1) i3.setImageResource(R.drawable.i_pos3);
                else i3.setImageResource(R.drawable.i_imp3);
                if (bool[3] == 1) i4.setImageResource(R.drawable.i_pos4);
                else i4.setImageResource(R.drawable.i_imp4);

                for(int j=0;j<4;j++){
                    if(bool[j]==0){
                        btn.setBackgroundResource(R.drawable.b_imp2);
                        return;
                    }
                    btn.setBackgroundResource(R.drawable.b_pos2);
                }
            }
            else {
                i2.setImageResource(R.drawable.i_imp2_unable);
                i3.setImageResource(R.drawable.i_imp3_unable);
                i4.setImageResource(R.drawable.i_imp4_unable);
                btn.setBackgroundResource(R.drawable.b_imp2_unable);
            }
        }

        public CustomDialog(Context context,int[] bool) {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);
            this.bool=bool;
        }
    }

   public void getData(){
       NetworkManager.getInstance().getMag(MyApplication.getContext(), new NetworkManager.OnResultListener<MagneticResult>() {
           @Override
           public void onSuccess(Request request, MagneticResult result) {
               kk = result.getKindex().getCurrentK();
           }

           @Override
           public void onFail(Request request, IOException exception) {
           }
       });

       NetworkManager.getInstance().getWind(MyApplication.getContext(), "" + location.getLatitude(), "" + location.getLongitude(), new NetworkManager.OnResultListener<WeatherResult>() {
           @Override
           public void onSuccess(Request request, WeatherResult result) {
               //wind=result.getGweather().getCurrent().get(0).getWind().getSpeed().getValue();
               wind = result.getWind().getSpeed();
               sunrise = result.getSun().getSunrise();
               sunset = result.getSun().getSunset();
               Log.d("낙낙낙",wind);
               Log.d("낙낙낙",sunrise);
               Log.d("낙낙낙", sunset);
               Log.d("낙낙낙",kk+"");
               Log.d("시간",System.currentTimeMillis()/1000+"");
               LatLng latLngTest = new LatLng(location.getLatitude(), location.getLongitude()); // The location to test. You will initialize it with your user's location
               LatLng latLngTest1 = new LatLng(37.3979826,126.9284821);
               List<KmlPolygon> polygonsInLayer = getPolygons(layer1.getContainers());
               boolean liesInside = liesOnPolygon(polygonsInLayer, latLngTest);
               boolean liesInside1 = liesOnPolygon(polygonsInLayer, latLngTest1);
               Log.w("확인",liesInside+"");
               Log.w("확인",liesInside1+"");
               /** 비행 가능ers.hasCon/불가능 bool 값 설정 **/
               /** 수정 중
               if(wind!=null&&sunrise!=null&&sunset!=null){
                   NetworkManager.getInstance().getResistance(MyApplication.getContext(), mem_id, new NetworkManager.OnResultListener<DroneResistanceResult>() {
                       @Override
                       public void onSuccess(Request request, DroneResistanceResult result) {
                           DroneResistance dr;
                           dr = result.getResult();
                           String dr_resistance1 = dr.getDroneResistance().toString();
                           double dr_resistance = Double.parseDouble(dr_resistance1);
                           double dr_wind = Double.parseDouble(wind);
                           long l_sunrise = Long.parseLong(sunrise);
                           long l_sunset = Long.parseLong(sunset);
                           long now = System.currentTimeMillis()/1000;

                           if (l_sunrise<now && now<l_sunset) bool[1]=1; else bool[1]=0;
                           if (dr_wind < dr_resistance) bool[2]=1; else bool[2]=0;
                           if (kk < 5) bool[3]=1; else bool[3]=0;

                       }

                       @Override
                       public void onFail(Request request, IOException exception) {

                       }

                   });
               }**/


/** 이전 것
               if(wind!=null&&sunrise!=null&&sunset!=null){
                   Log.w("됨?","?");
                   NetworkManager.getInstance().getFlight(MyApplication.getContext(),mem_id,"3",wind,""+kk,sunrise,sunset ,new NetworkManager.OnResultListener<DroneFlightResult>() {

                       @Override
                       public void onSuccess(Request request, DroneFlightResult result) {
                           bool= result.getResult();
                           Log.w("되냐","?");
                           Log.w("확인",bool[0]+"");
                           Log.w("확인",bool[1]+"");
                           Log.w("확인",bool[2]+"");
                           Log.w("확인",bool[3]+"");
                       }
                       @Override
                       public void onFail(Request request, IOException exception) {
                       }
                   });

               }
 **/
           }

           @Override
           public void onFail(Request request, IOException exception) {
           }
       });



   }
    private List<KmlPolygon> getPolygons(Iterable<KmlContainer> containers) {
        List<KmlPolygon> polygons = new ArrayList<>();

        if (containers == null) {
            return polygons;
        }

        for (KmlContainer container : containers) {
            polygons.addAll(getPolygons(container));
        }

        return polygons;
    }

    private List<KmlPolygon> getPolygons(KmlContainer container) {
        List<KmlPolygon> polygons = new ArrayList<>();

        if (container == null) {
            return polygons;
        }

        Iterable<KmlPlacemark> placemarks = container.getPlacemarks();
        if (placemarks != null) {
            for (KmlPlacemark placemark : placemarks) {
                if (placemark.getGeometry() instanceof KmlPolygon) {
                    polygons.add((KmlPolygon) placemark.getGeometry());
                }
            }
        }

        if (container.hasContainers()) {
            polygons.addAll(getPolygons(container.getContainers()));
        }

        return polygons;
    }

    private boolean liesOnPolygon(List<KmlPolygon> polygons, LatLng test) {
        boolean lies = false;

        if (polygons == null || test == null) {
            return lies;
        }

        for (KmlPolygon polygon : polygons) {
            if (liesOnPolygon(polygon, test)) {
                lies = true;
                break;
            }
        }

        return lies;
    }

    private boolean liesOnPolygon(KmlPolygon polygon, LatLng test) {
        boolean lies = false;

        if (polygon == null || test == null) {
            return lies;
        }

        // Get the outer boundary and check if the test location lies inside
        ArrayList<LatLng> outerBoundary = polygon.getOuterBoundaryCoordinates();
        lies = PolyUtil.containsLocation(test, outerBoundary, true);

        if (lies) {
            // Get the inner boundaries and check if the test location lies inside
            ArrayList<ArrayList<LatLng>> innerBoundaries = polygon.getInnerBoundaryCoordinates();
            if (innerBoundaries != null) {
                for (ArrayList<LatLng> innerBoundary : innerBoundaries) {
                    // If the test location lies in a hole, the polygon doesn't contain the location
                    if (PolyUtil.containsLocation(test, innerBoundary, true)) {
                        lies = false;
                        break;
                    }
                }
            }
        }

        return lies;
    }
}



