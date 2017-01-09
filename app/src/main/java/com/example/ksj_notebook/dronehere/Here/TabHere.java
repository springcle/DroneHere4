package com.example.ksj_notebook.dronehere.Here;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneResistance;
import com.example.ksj_notebook.dronehere.data.DroneResistanceResult;
import com.example.ksj_notebook.dronehere.data.MagneticResult;
import com.example.ksj_notebook.dronehere.data.MemDrone;
import com.example.ksj_notebook.dronehere.data.WeatherResult;
import com.example.ksj_notebook.dronehere.login.StartActivity;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;
import com.example.ksj_notebook.dronehere.manager.PropertyManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
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

    LocationManager locationManager;
    GoogleApiClient mClient;
    GoogleMap mMap;
    Context context;
    List<String> imageUrl;
    List<Bitmap> bitmaps;
    LatLngBounds bounds;
    Location location;
    AutoCompleteTextView place_text;
    Button myLocation;
    Button search_place;
    Marker marker;
    Handler mHandler = new Handler(Looper.getMainLooper());
    LayoutInflater inflater;

    int kk;
    String sunrise;
    String sunset;
    String wind;
    Double dr_resistance;

    private boolean liesInside = false;

    String mem_id;

    KmlLayer layer1;

    int[] bool = {0, 0, 0, 0};
    boolean drone_exist;
    PlaceAutocompleteAdapter placeAutocompleteAdapter;

    public TabHere() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mem_id = PropertyManager.getInstance().getId();
        context = getContext();
        mClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        locationManager = (LocationManager) getActivity().getSystemService(getActivity().getApplicationContext().LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gps_check();
        } else {
            mClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_here, container, false);

        myLocation = (Button) view.findViewById(R.id.myLocation);
        search_place = (Button) view.findViewById(R.id.search_btn);
        
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
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 11f);
                    //37.47547965,126.95924163
                    if (mMap != null) {
                        mMap.moveCamera(update);
                    }
                }
            }
        });
        search_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceDialog dialog = new PlaceDialog(context);
                dialog.show();
            }
        });
        return view;
    }

    class PlaceDialog extends Dialog {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.gravity = Gravity.TOP;
            lpWindow.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lpWindow.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lpWindow.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            getWindow().setAttributes(lpWindow);

            setContentView(R.layout.place_search);
            place_text = (AutoCompleteTextView) findViewById(R.id.place_text);
            place_text.setOnItemClickListener(mAutocompleteClickListener);
            LatLngBounds latLngBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
            AutocompleteFilter filter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .build();

            placeAutocompleteAdapter = new PlaceAutocompleteAdapter(context, mClient, latLngBounds, null);
            place_text.setAdapter(placeAutocompleteAdapter);
            placeAutocompleteAdapter.setGoogleApiClient(mClient);

            place_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        }

        public PlaceDialog(Context context) {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager) getActivity().getSystemService(getActivity().getApplicationContext().LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gps_check();
        }
        LocationRequest request = new LocationRequest();
        request.setInterval(4000);
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request, mListener);
        delay_getData();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        placeAutocompleteAdapter.setGoogleApiClient(null);
    }

    LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            locationManager = (LocationManager) getActivity().getSystemService(getActivity().getApplicationContext().LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                gps_check();
            } else {
                if (location != null) {
                    //add_marker(location);
                    getData();
                }
            }
        }
    };

    private void displayMessage(Location location) {
        if (location != null) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 11f);
            if (mMap != null) {
                mMap.moveCamera(update);
            }
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        try {
            layer1 = new KmlLayer(mMap, R.raw.zz, getContext());
            layer1.addLayerToMap();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraChangeListener(this);
        final LatLng korea = new LatLng(36.641111, 127.853366);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(korea, 7.1f));
//        CircleOptions circleOptions = new CircleOptions()
//                .center(new LatLng(35.4751, 129.1954))
//                .radius(14000)
//                .fillColor(R.color.drgreen)
//                .strokeColor(R.color.drgreen);
//        mMap.addCircle(circleOptions);

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

    private void add_marker(Location location) {

        if (marker != null) marker.remove();

        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(location.getLatitude(), location.getLongitude()));

        boolean z = true;
        for (int j = 0; j < 4; j++) {
            if (bool[j] == 0) {
                z = false;
                break;
            }
        }
        if (PropertyManager.getInstance().getId() != "" && drone_exist == true) {
            if (z == false) {
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_imp1_1));
                marker = mMap.addMarker(options);
            } else {
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_pos1_1));
                marker = mMap.addMarker(options);
            }
        } else if (PropertyManager.getInstance().getId() != "" && drone_exist == false) { // drone_exist : false면 터치시 커 스텀 다이얼로그 비활성화
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_not_drone));
            marker = mMap.addMarker(options);
        } else {
            drone_exist = true; // 드론 유/무 확인 후 창 비활성화 용도 인데, 비회원 일때도 커스텀다이얼로그를 활성화 시킨 후 로그인 시켜야하므로 true값을 넣어줌
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_imp1_1_unable));
            marker = mMap.addMarker(options);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (drone_exist == true) { // 드론있으면 다이얼로그 띄워주기, 비회원일때도 띄워줘야함
            CustomDialog dialog = new CustomDialog(getContext(), bool);
            dialog.show();
        }
        return false;
    }

    class CustomDialog extends Dialog {
        ImageView i1,i2,i3,i4;
        int[] bool;
        Button btn;
        //TextView t1,t2,t3,t4;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            lpWindow.dimAmount = 0.8f;
            lpWindow.gravity = Gravity.CENTER;
            lpWindow.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lpWindow.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(lpWindow);

            setContentView(R.layout.location_dialog);

            i1 = (ImageView) findViewById(R.id.dial_image1);
            i2 = (ImageView) findViewById(R.id.dial_image2);
            i3 = (ImageView) findViewById(R.id.dial_image3);
            i4 = (ImageView) findViewById(R.id.dial_image4);
            /*
            t1 = (TextView)findViewById(R.id.text_area);
            t2 = (TextView)findViewById(R.id.text_time);
            t3 = (TextView)findViewById(R.id.text_wind);
            t4 = (TextView)findViewById(R.id.text_magnetic);
            */
            btn = (Button) findViewById(R.id.dial_btn);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PropertyManager.getInstance().getId() == "") {
                        startActivity(new Intent(getActivity().getApplicationContext(), StartActivity.class));
                        getActivity().finish();
                    }
                    dismiss();
                }
            });
            if (bool[0] == 1) i1.setImageResource(R.drawable.i_pos1);
            else i1.setImageResource(R.drawable.i_imp1);

            if (PropertyManager.getInstance().getId() != "") {
                /*
                t1.setText("공역표시");
                if(sunrise != null && sunset != null) {
                    long l_sunrise = Long.parseLong(sunrise);
                    long l_sunset = Long.parseLong(sunset);
                    Log.w("일출", sunrise + 1000);
                    Log.w("일몰" ,sunset+1000);
                    Log.w("현재" ,System.currentTimeMillis()+"");
                    TimeConverter sunrise_converter = new TimeConverter(sunrise+"1000");
                    TimeConverter sunset_converter = new TimeConverter(sunset+"1000");
                    SimpleDateFormat dayTime = new SimpleDateFormat("hh:mm");
                    String str1 = dayTime.format(new Date(l_sunrise*1000));
                    String str2 = dayTime.format(new Date(l_sunset*1000));
                    t2.setText("오전"+str1 +" ~\n"+"오후" + str2);
                }
                if(wind != null && dr_resistance != null) {
                    t3.setText("현재풍속 : " + wind + " \n주력드론 : " + dr_resistance);
                }
                t4.setText("현재 자기장 : " + kk + " \n자기장 제한 : 5미만");
                btn.setEnabled(true);
                btn.setClickable(true);
                */
                if (bool[1] == 1) i2.setImageResource(R.drawable.i_pos2);
                else i2.setImageResource(R.drawable.i_imp2);
                if (bool[2] == 1) i3.setImageResource(R.drawable.i_pos3);
                else i3.setImageResource(R.drawable.i_imp3);
                if (bool[3] == 1) i4.setImageResource(R.drawable.i_pos4);
                else i4.setImageResource(R.drawable.i_imp4);
                for (int j = 0; j < 4; j++) {
                    if (bool[j] == 0) {
                        btn.setBackgroundResource(R.drawable.b_imp2);
                        return;
                    } else btn.setBackgroundResource(R.drawable.b_pos2);
                }
            } else {
                i1.setImageResource(R.drawable.i_imp1_not_user);
                i2.setImageResource(R.drawable.i_imp2_unable);
                i3.setImageResource(R.drawable.i_imp3_unable);
                i4.setImageResource(R.drawable.i_imp4_unable);
                btn.setBackgroundResource(R.drawable.b_imp2_unable);
            }
        }

        public CustomDialog(Context context, int[] bool) {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);
            this.bool = bool;
        }
    }

    public void getData() {
        //비행가능구역
        if (location == null) { // location 못 받아 왔을 경우 null 일 경우 딜레이 후 재실행
            delay_getData();
        } else {
            // 4대 비행공식 계산
            fly_enable_check();
        }
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
    private void delay_getData() {
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
                getData();
            }
        }, 800);
    }
    /* 자동으로 위치기능 활성화 되면 앱 실행 메소드
    private void gps_enable_check(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else{
                    gps_enable_check();
                }
            }
        },1000);
    }*/
    public void gps_check(){
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("GPS Check");
            dialog.setMessage("지역정보를 받아오기위해 위치기능을 활성화 시킨 후 실행바랍니다.");
            dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
            dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    getActivity().finish();
                    System.exit(0);
                }
            });
            dialog.show();

    }
    // 4대 비행공식 계산
    public void fly_enable_check(){
        if (PropertyManager.getInstance().getId() != "") {
            LatLng latLngTest = new LatLng(location.getLatitude(), location.getLongitude());
            List<KmlPolygon> polygonsInLayer = getPolygons(layer1.getContainers());
            liesInside = liesOnPolygon(polygonsInLayer, latLngTest);
            //자기장
            NetworkManager.getInstance().getMag(MyApplication.getContext(), new NetworkManager.OnResultListener<MagneticResult>() {
                @Override
                public void onSuccess(Request request, MagneticResult result) {
                    kk = result.getKindex().getCurrentK();
                }

                @Override
                public void onFail(Request request, IOException exception) {
                    Toast.makeText(context, "데이터를 받아오는데 실패하였습니다.",Toast.LENGTH_SHORT).show();
                }
            });
            //풍속,일출,일몰 값
            NetworkManager.getInstance().getWind(MyApplication.getContext(), "" + location.getLatitude(), "" + location.getLongitude(), new NetworkManager.OnResultListener<WeatherResult>() {
                @Override
                public void onSuccess(Request request, WeatherResult result) {
                    wind = result.getWind().getSpeed();
                    sunrise = result.getSun().getSunrise();
                    sunset = result.getSun().getSunset();

                    /** 비행 가능/불가능 bool 값 설정 **/

                    if (wind != null && sunrise != null && sunset != null) {
                        NetworkManager.getInstance().getResistance(MyApplication.getContext(), mem_id, new NetworkManager.OnResultListener<DroneResistanceResult>() {
                            @Override
                            public void onSuccess(Request request, DroneResistanceResult result) {
                                List<MemDrone> memDrone;
                                String dr_resistance1;
                                DroneResistance dr;
                                dr = result.getResult();
                                memDrone = dr.getMemResultDrone();
                                boolean drname = memDrone.isEmpty(); //멤드론 없으면 true, 있으면 false
                                if (drname == false) {
                                    drone_exist = true;
                                    dr_resistance1 = dr.getDroneResistance().toString();
                                    dr_resistance = Double.parseDouble(dr_resistance1);
                                    double dr_wind = Double.parseDouble(wind);
                                    long l_sunrise = Long.parseLong(sunrise);
                                    long l_sunset = Long.parseLong(sunset);
                                    long now = System.currentTimeMillis() / 1000;
                                    if (liesInside == false) bool[0] = 1;
                                    else bool[0] = 0;
                                    if (l_sunrise < now && now < l_sunset) bool[1] = 1;
                                    else bool[1] = 0;
                                    if (dr_wind < dr_resistance) bool[2] = 1;
                                    else bool[2] = 0;
                                    if (kk < 5) bool[3] = 1;
                                    else bool[3] = 0;

                                    // 비행 가능, 불가능 표시 마커
                                    if (bool != null && location != null) {
                                        add_marker(location);
                                    }
                                } else if(drname == true){
                                    for (int i = 0; i < bool.length; i++) {
                                        bool[i] = 0;
                                    }
                                    drone_exist = false;
                                    if(location != null) {
                                        add_marker(location);
                                    }
                                }
                            }

                            @Override
                            public void onFail(Request request, IOException exception) {
                                Toast.makeText(context, "데이터를 받아오는데 실패하였습니다.",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFail(Request request, IOException exception) {
                    Toast.makeText(context, "데이터를 받아오는데 실패하였습니다.",Toast.LENGTH_SHORT).show();
                }
            });
        } else { // 비회원 일때
            drone_exist = false;
            for (int i = 0; i < 4; i++) {
                bool[0] = 0;
            }
            if(location != null) {
                add_marker(location);
            }
        }
    }
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //final PlaceAutocompleteAdapter.PlaceAutocomplete item = (PlaceAutocompleteAdapter.PlaceAutocomplete) placeAutocompleteAdapter.getItem(position);
            ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> itemlist = placeAutocompleteAdapter.getResultList();
            //final String placeId = String.valueOf(item.placeId);
            PlaceAutocompleteAdapter.PlaceAutocomplete item1 = itemlist.get(position);
            final String placeId = String.valueOf(item1.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                        places.getStatus().toString();
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            LatLng place_location;
            place_location = place.getLatLng();
            if (location != null) {
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(place_location, 11f);
                if (mMap != null) {
                    mMap.moveCamera(update);
                }
            }
            CharSequence attributions = places.getAttributions();
        }
/*
        @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
                @Override
                public void onReceiveResult(PlaceBuffer result) throws RemoteException {
                    if (!result.getStatus().isSuccess()) {
                        result.getStatus().toString();
                        return;
                    }
                    // Selecting the first object buffer.
                    final Place place = result.get(0);
                    LatLng place_location;
                    place_location = place.getLatLng();
                    if (location != null) {
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(place_location, 11f);
                        if (mMap != null) {
                            mMap.moveCamera(update);
                        }
                    }
                    CharSequence attributions = result.getAttributions();
                }*/
            };
}



