package com.example.ksj_notebook.dronehere.Here;


import android.Manifest;
import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ksj_notebook.dronehere.MainActivity;
import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneResistance;
import com.example.ksj_notebook.dronehere.data.DroneResistanceResult;
import com.example.ksj_notebook.dronehere.data.MagneticResult;
import com.example.ksj_notebook.dronehere.data.MemDrone;
import com.example.ksj_notebook.dronehere.data.WeatherResult;
import com.example.ksj_notebook.dronehere.dialog.AddDrone;
import com.example.ksj_notebook.dronehere.login.StartActivity;
import com.example.ksj_notebook.dronehere.manager.BackPressCloseHandler;
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
        , GoogleMap.OnCameraChangeListener, GoogleMap.OnMarkerClickListener, MainActivity.onKeyBackPressedListener {

    final static int RESULT_OK = -1;
    final static int RESULT_CANCELED = 0;
    private BackPressCloseHandler backPressCloseHandler;
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
    Marker my_marker;
    Handler mHandler = new Handler(Looper.getMainLooper());
    LayoutInflater inflater;
    Marker clickMarker = null;
    MarkerOptions clickMarker_option;
    InputMethodManager inputMethodManager;

    boolean getData_success = false;
    int magnetic;
    String sunrise, sunrise_click;
    String sunset, sunset_click;
    String wind, wind_click;
    Double dr_resistance;
    PlaceDialog placedialog;

    /** lieInside 인덱스 **/
    // [0]: 금지구역
    // [1]: 제한구역
    // [2]: 관제권
    // [3]: 위험구역
    private boolean liesInside[][] = new boolean[2][4];

    String mem_id;

    KmlLayer prohibit_layer;
    KmlLayer restrict_layer;
    KmlLayer airControlZone_layer;
    KmlLayer danger_layer;


    int[] bool = new int[4];
    int[] bool1 = new int[4];
    boolean drone_exist;
    PlaceAutocompleteAdapter placeAutocompleteAdapter;

    public TabHere() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backPressCloseHandler = new BackPressCloseHandler(getActivity());
        mem_id = PropertyManager.getInstance().getId();
        mClient = new GoogleApiClient.Builder(getContext())
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
        if(clickMarker != null) clickMarker.remove();
        context = getContext();
        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        for(int i = 0; i<2; i++){
            for(int j =0; j<4; j++){
                liesInside[i][j] = false;
            }
        }
        locationManager = (LocationManager) getActivity().getSystemService(context.LOCATION_SERVICE);
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
        this.inflater = inflater;

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentByTag("map");
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map_container1, mapFragment, "map").commit();
            mapFragment.getMapAsync(this);
        }

        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location != null) {
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14f);
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
                if (PropertyManager.getInstance().getId() == "") {
                    Toast.makeText(context, "회원가입 시 사용가능합니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (drone_exist == false) {
                    Toast.makeText(context, "드론 선택 후 사용가능합니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (placedialog == null) {
                    placedialog = new PlaceDialog(context);
                    placedialog.setCanceledOnTouchOutside(true);
                    placedialog.show();
                    place_text.requestFocus();
                    //InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    placedialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            placedialog.dismiss();
                            placedialog = null;
                        }
                    });
                }
            }
        });
        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).setOnKeyBackPressedListener(this);
    }

    @Override
    public void onBack() {
        backPressCloseHandler.onBackPressed();
    }



    /** 장소검색 바 **/
    class PlaceDialog extends Dialog {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.gravity = Gravity.TOP;
            lpWindow.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lpWindow.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //lpWindow.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
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
        locationManager = (LocationManager) getActivity().getSystemService(context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gps_check();
        }
        LocationRequest request = new LocationRequest();
        request.setInterval(8000);
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
            locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
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
            prohibit_layer = new KmlLayer(mMap, R.raw.prohibit, context);
            prohibit_layer.addLayerToMap();
            restrict_layer = new KmlLayer(mMap, R.raw.restrict, context);
            restrict_layer.addLayerToMap();
            airControlZone_layer = new KmlLayer(mMap, R.raw.aircontrolzone, context);
            airControlZone_layer.addLayerToMap();
            danger_layer = new KmlLayer(mMap, R.raw.danger, context);
            danger_layer.addLayerToMap();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraChangeListener(this);
        final LatLng korea = new LatLng(36.641111, 127.853366);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(korea, 7.1f));
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (PropertyManager.getInstance().getId() == "" || drone_exist == false)
                    return;
                fly_enable_check_marker(latLng);
                delay_marker_display(latLng);
                /* 마커 찍었을때 카메라 포커스도 같이 움직이는 코드
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 11f);
                if (mMap != null) {
                    mMap.moveCamera(update);
                }*/
            }
        });
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
//            GroundOverlayOptions overlayOptions1 = new GroundOverlayOptions().
//                    image(BitmapDescriptorFactory.fromResource(R.drawable.m22)).positionFromBounds(bounds);
//            mMap.addGroundOverlay(overlayOptions1);
    }

    private void add_marker(Location location, int[] bool) {
        if (my_marker != null) {
            my_marker.remove();
        }
        MarkerOptions options = new MarkerOptions();
        if(location != null) {
            options.position(new LatLng(location.getLatitude(), location.getLongitude()));
        } else {
            getData();
            return;
        }
        // 4대 비행가능 요소 중 1개의 불가능 요소만 있어도, 비행불가능
        boolean fly = true;
        if(bool != null) {
            for (int j = 0; j < 4; j++) {
                if (bool[j] == 0) {
                    fly = false;
                    break;
                }
            }
        } else {
            Toast.makeText(context, "데이터 읽어오는데 장애가 발생하였습니다.",Toast.LENGTH_SHORT).show();
            return;
        }
        if (PropertyManager.getInstance().getId() != "" && drone_exist == true) {
            if (fly == false) {
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_imp1_1));
            } else {
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_pos1_1));
            }
        } else if (PropertyManager.getInstance().getId() != "" && drone_exist == false) { // drone_exist : false면 터치시 커 스텀 다이얼로그 비활성화
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_not_drone));
        } else {
            drone_exist = true; // 드론 유/무 확인 후 창 비활성화 용도 인데, 비회원 일때도 커스텀다이얼로그를 활성화 시킨 후 로그인 시켜야하므로 true값을 넣어줌
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_imp1_1_unable));
        }
        my_marker = mMap.addMarker(options);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        CustomDialog dialog1;
        CustomDialog dialog2;
        if (drone_exist == true && marker.equals(my_marker)) { // 드론있으면 다이얼로그 띄워주기, 비회원일때도 띄워줘야함
            dialog1 = new CustomDialog(context, bool);
            dialog1.setCanceledOnTouchOutside(true);
            dialog1.show();
        } else if(drone_exist == true && marker.equals(clickMarker)){
            dialog2 = new CustomDialog(context, bool1);
            dialog2.setCanceledOnTouchOutside(true);
            dialog2.show();
        } else if (PropertyManager.getInstance().getId() != "" && drone_exist == false && marker.equals(my_marker)){
            AddDrone addDrone; // 사용자의 드론이 없을 시 -> 드론추가 커스텀다이얼로그 생성
            addDrone = new AddDrone(context, getActivity());
            addDrone.setCanceledOnTouchOutside(true);
            addDrone.show();
        }
        return false;
    }

    // 4대 비행가능 요소 가능/불가능 표시
    class CustomDialog extends Dialog {
        ImageView i1,i2,i3,i4;
        int[] bool;
        Button btn;
        Button exit_btn;
        //Button contact_btn;
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
            exit_btn = (Button) findViewById(R.id.exit_btn);
            /*
            contact_btn = (Button) findViewById(R.id.contact_info_btn);
            if(PropertyManager.getInstance().getId() == "") {
                contact_btn.setEnabled(false);
                contact_btn.setVisibility(View.GONE);
            }*/
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PropertyManager.getInstance().getId() == "") {
                        startActivity(new Intent(context, StartActivity.class));
                        getActivity().finish();
                    }
                    dismiss();
                }
            });
            /*
            contact_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.onestop.go.kr"));
                    startActivity(intent);
                }
            });*/
            exit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            if (bool[0] == 1) i1.setImageResource(R.drawable.i_pos1);
            else i1.setImageResource(R.drawable.i_imp1);

            if (PropertyManager.getInstance().getId() != "") {
                /* 4대 비행가능요소를 Detail하게 표시하는 코드
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
                t4.setText("현재 자기장 : " + magnetic + " \n자기장 제한 : 5미만");
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


    /** 현재 내위치의 비행 가능/불가능을 계산 **/
    public void getData() {
        //비행가능구역
        if (location == null) {
            delay_getData();
        } else {
            // 4대 비행공식 계산
            fly_enable_check_mylocation();
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
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        }, 1000);
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
            AlertDialog mDialog;
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
                    getActivity().moveTaskToBack(true);
                    getActivity().finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }
            });
            mDialog = dialog.create();
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
    }

    private void delay_marker_display(final LatLng latLng) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 권한 체크 부분
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (getData_success == true) {
                    if (clickMarker != null) {
                        clickMarker.remove();
                    }
                    clickMarker_option = new MarkerOptions();
                    clickMarker_option.position(latLng);
                    if (liesInside[1][0] == true) clickMarker_option.title("비행금지구역");
                    else if (liesInside[1][1] == true) clickMarker_option.title("비행제한구역");
                    else if (liesInside[1][2] == true) clickMarker_option.title("관제권");
                    else if (liesInside[1][3] == true) clickMarker_option.title("비행위험구역");
                    else clickMarker_option.title("일반공역");

                    clickMarker_option.snippet("풍속 : " + wind_click + "m/s");
                    clickMarker = mMap.addMarker(clickMarker_option);
                    clickMarker.showInfoWindow();
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            if (marker.equals(clickMarker)) {
                                CustomDialog dialog;
                                dialog = new CustomDialog(context, bool1);
                                dialog.setCanceledOnTouchOutside(true);
                                dialog.show();
                            }
                        }
                    });
                    getData_success = false;
                } else delay_marker_display(latLng);
            }
        }, 800);
    }
    /** 4대 비행공식 계산(내 위치) **/
    public void fly_enable_check_mylocation(){
        if (PropertyManager.getInstance().getId() != "") {
            LatLng latlng;
            try {
                latlng = new LatLng(location.getLatitude(), location.getLongitude());
            } catch (NullPointerException e){
                e.getMessage();
                return;
            }
            // liesInside[] index is..
            // [0]: 금지구역
            // [1]: 제한구역
            // [2]: 관제권
            // [3]: 위험구역
            List<KmlPolygon> polygonsInLayer0 = getPolygons(prohibit_layer.getContainers());
            liesInside[0][0] = liesOnPolygon(polygonsInLayer0, latlng);
            List<KmlPolygon> polygonsInLayer1 = getPolygons(restrict_layer.getContainers());
            liesInside[0][1] = liesOnPolygon(polygonsInLayer1, latlng);
            List<KmlPolygon> polygonsInLayer2 = getPolygons(airControlZone_layer.getContainers());
            liesInside[0][2] = liesOnPolygon(polygonsInLayer2, latlng);
            List<KmlPolygon> polygonsInLayer3 = getPolygons(danger_layer.getContainers());
            liesInside[0][3] = liesOnPolygon(polygonsInLayer3, latlng);

            /** 자기장 **/
            NetworkManager.getInstance().getMag(MyApplication.getContext(), new NetworkManager.OnResultListener<MagneticResult>() {
                @Override
                public void onSuccess(Request request, MagneticResult result) {
                        magnetic = result.getKindex().getCurrentK();
                }
                @Override
                public void onFail(Request request, IOException exception) {
                    Toast.makeText(context, "데이터를 받아오는데 실패하였습니다.",Toast.LENGTH_SHORT).show();
                }
            });
            /** 풍속,일출,일몰 값 받아오기 **/
            NetworkManager.getInstance().getWind(MyApplication.getContext(), "" + latlng.latitude, "" + latlng.longitude, new NetworkManager.OnResultListener<WeatherResult>() {
                @Override
                public void onSuccess(Request request, WeatherResult result) {
                        wind = result.getWind().getSpeed();
                        sunrise = result.getSun().getSunrise();
                        sunset = result.getSun().getSunset();

                    /** 4대 비행 공식 계산 **/
                    if ((wind != null && sunrise != null && sunset != null) || (wind_click != null && sunrise_click != null && sunset_click != null)) {
                        NetworkManager.getInstance().getResistance(MyApplication.getContext(), mem_id, new NetworkManager.OnResultListener<DroneResistanceResult>() {
                            @Override
                            public void onSuccess(Request request, DroneResistanceResult result) {
                                long now = System.currentTimeMillis() / 1000;
                                double dr_wind;
                                long l_sunrise;
                                long l_sunset;
                                List<MemDrone> memDrone;
                                String dr_resistance1;
                                DroneResistance dr;
                                dr = result.getResult();
                                memDrone = dr.getMemResultDrone();
                                boolean drname = memDrone.isEmpty(); // 사용자의 드론이 없으면 true, 있으면 false

                                if (drname == false) {
                                    drone_exist = true;
                                    dr_resistance1 = dr.getDroneResistance().toString();
                                    dr_resistance = Double.parseDouble(dr_resistance1);
                                    dr_wind = Double.parseDouble(wind);
                                    l_sunrise = Long.parseLong(sunrise);
                                    l_sunset = Long.parseLong(sunset);

                                    if(liesInside[0][0] == false && liesInside[0][1] == false && liesInside[0][2] == false)
                                        bool[0] = 1;
                                    else bool[0] = 0;

                                    if (l_sunrise < now && now < l_sunset) bool[1] = 1;
                                    else bool[1] = 0;

                                    if (dr_wind < dr_resistance) bool[2] = 1;
                                    else bool[2] = 0;

                                    if (magnetic < 5) bool[3] = 1;
                                    else bool[3] = 0;

                                    // 비행 가능, 불가능 표시 마커
                                    if ((bool != null) && location != null) {
                                        add_marker(location, bool);
                                    }

                                } else if(drname == true){

                                    for (int i = 0; i < bool.length; i++) {
                                        bool[i] = 0;
                                    }
                                    drone_exist = false;
                                    if(location != null) {
                                        add_marker(location, bool);
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
                bool[i] = 0;
                bool1[i] = 0;
            }
            if(location != null) {
                add_marker(location, bool);
            }
        }

    }
    /** 4대 비행공식 계산(마커 위치) **/
    public void fly_enable_check_marker(LatLng latlng){
        if (PropertyManager.getInstance().getId() != "") {
            // liesInside[] index is..
            // [0]: 금지구역
            // [1]: 제한구역
            // [2]: 관제권
            // [3]: 위험구역
            List<KmlPolygon> polygonsInLayer0 = getPolygons(prohibit_layer.getContainers());
            liesInside[1][0] = liesOnPolygon(polygonsInLayer0, latlng);
            List<KmlPolygon> polygonsInLayer1 = getPolygons(restrict_layer.getContainers());
            liesInside[1][1] = liesOnPolygon(polygonsInLayer1, latlng);
            List<KmlPolygon> polygonsInLayer2 = getPolygons(airControlZone_layer.getContainers());
            liesInside[1][2] = liesOnPolygon(polygonsInLayer2, latlng);
            List<KmlPolygon> polygonsInLayer3 = getPolygons(danger_layer.getContainers());
            liesInside[1][3] = liesOnPolygon(polygonsInLayer3, latlng);

            /** 자기장 **/
            NetworkManager.getInstance().getMag(MyApplication.getContext(), new NetworkManager.OnResultListener<MagneticResult>() {
                @Override
                public void onSuccess(Request request, MagneticResult result) {
                    magnetic = result.getKindex().getCurrentK();
                }
                @Override
                public void onFail(Request request, IOException exception) {
                    Toast.makeText(context, "데이터를 받아오는데 실패하였습니다.",Toast.LENGTH_SHORT).show();
                }
            });
            /** 풍속,일출,일몰 값 **/
            NetworkManager.getInstance().getWind(MyApplication.getContext(), "" + latlng.latitude, "" + latlng.longitude, new NetworkManager.OnResultListener<WeatherResult>() {
                @Override
                public void onSuccess(Request request, WeatherResult result) {
                        wind_click = result.getWind().getSpeed();
                        sunrise_click = result.getSun().getSunrise();
                        sunset_click = result.getSun().getSunset();
                    /** 비행 가능/불가능 bool 값 설정 **/
                    if ((wind != null && sunrise != null && sunset != null) || (wind_click != null && sunrise_click != null && sunset_click != null)) {
                        NetworkManager.getInstance().getResistance(MyApplication.getContext(), mem_id, new NetworkManager.OnResultListener<DroneResistanceResult>() {
                            @Override
                            public void onSuccess(Request request, DroneResistanceResult result) {
                                long now = System.currentTimeMillis() / 1000;
                                double dr_wind;
                                long l_sunrise;
                                long l_sunset;
                                List<MemDrone> memDrone;
                                String dr_resistance1;
                                DroneResistance dr;
                                dr = result.getResult();
                                memDrone = dr.getMemResultDrone();
                                boolean drname = memDrone.isEmpty(); // 사용자 드론이 없으면 true, 있으면 false
                                if (drname == false) {

                                    drone_exist = true;
                                    dr_resistance1 = dr.getDroneResistance().toString();
                                    dr_resistance = Double.parseDouble(dr_resistance1);
                                    dr_wind = Double.parseDouble(wind_click);
                                    l_sunrise = Long.parseLong(sunrise_click);
                                    l_sunset = Long.parseLong(sunset_click);

                                    if(liesInside[1][0] == false && liesInside[1][1] == false && liesInside[1][2] == false)
                                        bool1[0] = 1;
                                    else bool1[0] = 0;

                                    if (l_sunrise < now && now < l_sunset) bool1[1] = 1;
                                    else bool1[1] = 0;

                                    if (dr_wind < dr_resistance) bool1[2] = 1;
                                    else bool1[2] = 0;

                                    if (magnetic < 5) bool1[3] = 1;
                                    else bool1[3] = 0;

                                    // 비행 가능, 불가능 표시 마커
                                    if ((bool != null) || (bool1 != null) && location != null) {
                                        add_marker(location, bool1);
                                    }

                                } else if(drname == true){

                                    for (int i = 0; i < bool.length; i++) {
                                        bool[i] = 0;
                                        bool1[i] = 0;
                                    }
                                    drone_exist = false;
                                    if(location != null) {
                                        add_marker(location, bool1);
                                    }

                                }
                                getData_success = true;
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
                bool[i] = 0;
                bool1[i] = 0;
            }
            if(location != null) {
                add_marker(location, bool1);
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
            if (drone_exist == false) {
                Toast.makeText(getContext(), "드론을 선택해주세요",Toast.LENGTH_SHORT).show();
                return;
            }
            final Place place = places.get(0);
            LatLng place_location;
            place_location = place.getLatLng();
            fly_enable_check_marker(place_location);
            delay_marker_display(place_location);
            placedialog.dismiss();
            placedialog = null;
            //InputMethodManager immhide = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            if (location != null) {
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(place_location, 13f);
                if (mMap != null) {
                    mMap.moveCamera(update);
                }
            }
        }
    };

}
