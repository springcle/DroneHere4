package com.santamaria.dronehere.Here;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
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
import com.santamaria.dronehere.Dronedb.TabDrone;
import com.santamaria.dronehere.MainActivity;
import com.santamaria.dronehere.MyApplication;
import com.santamaria.dronehere.News.TabNews;
import com.santamaria.dronehere.R;
import com.santamaria.dronehere.data.DroneResistance;
import com.santamaria.dronehere.data.DroneResistanceResult;
import com.santamaria.dronehere.data.MagneticResult;
import com.santamaria.dronehere.data.MemDrone;
import com.santamaria.dronehere.data.WeatherResult;
import com.santamaria.dronehere.dialog.AddDrone;
import com.santamaria.dronehere.login.StartActivity;
import com.santamaria.dronehere.manager.BackPressCloseHandler;
import com.santamaria.dronehere.manager.NetworkManager;
import com.santamaria.dronehere.manager.PropertyManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

// TODO 빌드 그래들에서 map 부분 10.2.0으로 바꾸면서 코드도 바꿔줘야됌
// import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabHere extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback
        , GoogleMap.OnCameraChangeListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, MainActivity.onKeyBackPressedListener {

    final static int RESULT_OK = -1;
    final static int RESULT_CANCELED = 0;

    /**
     * 내 위치 버튼 애니매이션
     **/
    int animation_cnt = 0;
    Animation animation;
    Animation animation2;

    LocationManager locationManager;
    GoogleApiClient mClient;
    GoogleMap mMap;
    Context context;
    Marker my_marker;
    Marker clickMarker = null;
    MarkerOptions clickMarker_option;
    Location location;

    /**
     * 공역 정보 구분(금지, 제한, 관제권, 위험)
     **/
    KmlLayer prohibit_layer;
    KmlLayer restrict_layer;
    KmlLayer airControlZone_layer;
    KmlLayer danger_layer;

    /**
     * Google Places API 관련
     **/
    PlaceAutocompleteAdapter placeAutocompleteAdapter;
    PlaceDialog placedialog;
    AutoCompleteTextView place_text;
    Button search_place;

    /**
     * ㅁ햄버거바, 내위치 버튼(카메라이동)
     **/
    Button myLocation;
    Button hamberger;

    /**
     * 롱 클릭 마커를 표시 할 때, 클릭마커 위치의 4대비행가능공식 계산 후 마커를 표시해주어야 하므로, 정보 받는동안 딜레이 후 마커표시(delayed_marker_display()함수에서 사용)
     **/
    Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 슬라이딩 패널
     **/
    SlidingUpPanelLayout sliding;
    LinearLayout drag_view;
    LinearLayout tab_news_btn, tab_drone_btn;
    LinearLayout sliding_panel_btn_selector;
    ViewPager tab_pager;
    FragmentTabPager viewPager_adpater;
    Fragment tab_news, tab_drone;

    /**
     * 백버튼, 입력자판 제어 관련
     **/
    InputMethodManager inputMethodManager;
    BackPressCloseHandler backPressCloseHandler;

    int openWeather_count = 0; // 오픈웨더 API 호출 횟수 절약용 카운트

    /**
     * 데이터 수신 완료 동기화 관련
     **/
    boolean getData_success = false;

    /**
     * 4대 비행가능 요인 변수들(자기장, 풍속제한, 일출일몰)
     **/
    int magnetic;
    String sunrise, sunrise_click;
    String sunset, sunset_click;
    String wind, wind_click;
    Double dr_resistance;


    /** lieInside 인덱스 **/
    // [0]: 금지구역
    // [1]: 제한구역
    // [2]: 관제권
    // [3]: 위험구역

    /**
     * 내위치, 롱클릭마커의 위치 구분을 분리 시키기 위해 이차원 배열 생성
     **/
    private boolean liesInside[][] = new boolean[2][4];

    /**
     * 사용자 일련번호(비회원, 사용자 구분 등)
     **/
    String mem_id;

    /**
     * 4대 비행가능 공식(자기장, 풍속제한, 일출일몰, 비행구역) 중 1개라도 부합하지 않을경우, 비행 불가능 표시를 하기위한 변수
     **/
    int[] bool = new int[4]; // 내 위치에 해당하는 지역의 4대비행 가능유무를 표시하기 위한 변수
    int[] bool1 = new int[4]; // 롱클릭할때 해당 지역의 4대비행 가능유무를 표시하기 위한 변수

    /**
     * 유저의 드론 유/무 판단
     **/
    boolean drone_exist;


    /**
     * 프래그먼트 기본 생성자
     **/
    public TabHere() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
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
        if (clickMarker != null) clickMarker.remove();
        context = getContext();
        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
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
    //

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tab_here, container, false);
        animation = AnimationUtils.loadAnimation(context, R.anim.mylocation);
        animation2 = AnimationUtils.loadAnimation(context, R.anim.mylocation_fade);
        myLocation = (Button) view.findViewById(R.id.myLocation);
        myLocation.setVisibility(View.INVISIBLE);
        search_place = (Button) view.findViewById(R.id.search_btn);
        hamberger = (Button) view.findViewById(R.id.hamberger_btn);

        /** 슬라이딩 패널**/
        tab_pager = (ViewPager) view.findViewById(R.id.sliding_viewpager);
          /*  tab_pager.setEnabled(false);*/
        sliding_panel_btn_selector = (LinearLayout) view.findViewById(R.id.sliding_panel_btn_selector);
        sliding = (SlidingUpPanelLayout) view.findViewById(R.id.slidingUpPanel_layout);
        sliding.setAnchorPoint(1.0f);
        sliding.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        /*sliding.setScrollableView(tab_pager);*/


        /** 슬라이딩 패널내에 적용한 뷰페이저 부분 **/
        tab_news = new TabNews();
        tab_drone = new TabDrone();

        final ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(tab_news);
        fragmentList.add(tab_drone);

        viewPager_adpater = new FragmentTabPager(context, getChildFragmentManager(), fragmentList);
        tab_pager.setAdapter(viewPager_adpater);
        //tab_pager.setPagingEnabled(false); // 커스텀뷰페이저의 setpagingEnabled로 page swipe disable.

        tab_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                 /* 슬라이드 패널 올린뒤 스와이프하면 선택되는 페이지
                *  슬라이딩패널 RecyclerView 대응된 버전 사용함. (setScrollableView)
                *  0번,1번 페이지를 선택하면 위아래로 스크롤이 되도록 설정.
                * */
                // sliding.setScrollableView(fragmentList.get(0).getView().findViewById(R.id.newslist));
                /*
                if (position != 0) {
                    sliding.setScrollableView(fragmentList.get(0).getView().findViewById(R.id.nested22));
                } else if (position != 1) {
                    sliding.setScrollableView(fragmentList.get(1).getView().findViewById(R.id.ryview));
                }*/
                //sliding.setScrollableView(fragmentList.get(1).getView().findViewById(R.id.ryview));
                // 위아래 스크롤이 뻑뻑하여 setNestedScrollingEnabled(false)로 설정해주었다.
                //     Log.e("선택한 뷰페이저",""+fragmentList.get(position));
                sliding.setScrollableView((fragmentList.get(position).getView().findViewById(R.id.nested22)));
                sliding.setScrollableView(fragmentList.get(position).getView().findViewById(R.id.ryview));
                /** 페이지 스와이프 시 상단의 메뉴 버튼 활성화된 이미지로 변경**/
                if (position == 0) {
                    sliding_panel_btn_selector.setBackgroundResource(R.drawable.on_dronenews);
                } else if (position == 1) {
                    sliding_panel_btn_selector.setBackgroundResource(R.drawable.on_dronedb);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        sliding.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {


            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                // TODO 슬라이딩패널 랩 가동시 사용! 펼쳐지면 내려가지 않도록 수정하거나 드론뉴스 스크롤 고치기! sliding패널 예제로 진행


        /*        if (previousState != SlidingUpPanelLayout.PanelState.EXPANDED && previousState != SlidingUpPanelLayout.PanelState.ANCHORED){
                    //EXPANDED = 펼침
                    // ANCHORED = 내림
Log.e("앵커포인트",""+sliding.getAnchorPoint());
                    if(sliding.getAnchorPoint() < 0){


                    }


                }*/

                  /*라이브러리의  아래 부분도 확인해볼것!!

                switch (state) {
                case ANCHORED:
                    smoothSlideTo(mAnchorPoint, 0);
                    break;
                case COLLAPSED:
                    smoothSlideTo(0, 0);
                    break;
                case EXPANDED:
                    smoothSlideTo(1.0f, 0);
                    break;
                case HIDDEN:
                    int newTop = computePanelTopPosition(0.0f) + (mIsSlidingUp ? +mPanelHeight : -mPanelHeight);
                    smoothSlideTo(computeSlideOffset(newTop), 0);
                    break;
            }*/

            }
        });


        tab_pager.setCurrentItem(0);
        tab_drone_btn = (LinearLayout) view.findViewById(R.id.tab_drone_btn);
        tab_news_btn = (LinearLayout) view.findViewById(R.id.tab_news_btn);
        drag_view = (LinearLayout) view.findViewById(R.id.drag_view);

        tab_news_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tab_pager.setCurrentItem(0);
                sliding_panel_btn_selector.setBackgroundResource(R.drawable.on_dronenews);
                //lm.scrollToPositionWithOffset(0,0);
            }
        });
        tab_drone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tab_pager.setCurrentItem(1);
                sliding_panel_btn_selector.setBackgroundResource(R.drawable.on_dronedb);
                //lm.scrollToPositionWithOffset(0,0);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentByTag("map");
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map_container1, mapFragment, "map").commit();
            mapFragment.getMapAsync(this);
        }

        // LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //adapter = new SlidingAdapter(context, getChildFragmentManager(), layoutManager, tab_news_btn, tab_drone_btn);
        //recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(layoutManager);

        hamberger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openHamberger();
            }
        });

        /** 버튼 누르면 내위치로 카메라 이동 **/
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (location != null) {
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14f);
                    //37.47547965,126.95924163

                    /** (카메라 무브 시 리스너 작동하므로 순서 중요함 **/
                    if (mMap != null && animation_cnt == 0) {
                        animation_cnt = 1;
                        mMap.moveCamera(update);
                        myLocation.setEnabled(false);
                        myLocation.setVisibility(View.INVISIBLE);
                        myLocation.setAnimation(animation2);
                        animation2.cancel();
                        animation2.reset();
                        animation2 = AnimationUtils.loadAnimation(context, R.anim.mylocation_fade);
                        if (animation != null) animation.start();
                    }
                }
            }
        });

        search_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mem_id == "") {
                    Toast.makeText(context, "로그인 시 사용가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (drone_exist == false) {
                    Toast.makeText(context, "드론 선택 후 사용가능합니다.", Toast.LENGTH_SHORT).show();
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
        sliding.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });
//TODO 패널 클릭 테스트
/*        sliding.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sliding.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });*/
        sliding.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        /*
        sliding.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(sliding.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED)
                {
                    sliding.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
                return true;
            }
        });*/
        sliding.setClipPanel(false);
        sliding.setPanelHeight(convertToPixels(context, 40));
        return view;
    }

    public static int convertToPixels(Context context, int dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).setOnKeyBackPressedListener(this);
    }

    @Override
    public void onBack() {
        if (sliding != null &&
                (sliding.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || sliding.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            sliding.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (sliding.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            backPressCloseHandler.onBackPressed();
        }
    }

    /**
     * 구글맵이 준비되면 4대 공역 레이어 형성, 마커 이벤트리스너 생성 및 대한민국으로 카메라 이동
     **/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        myLocation.setVisibility(View.INVISIBLE);
        myLocation.setEnabled(false);
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setCompassEnabled(false);
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
        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if (animation_cnt == 0) {
                    animation.cancel();
                    animation.reset();
                    animation = AnimationUtils.loadAnimation(context, R.anim.mylocation);
                    myLocation.setAnimation(animation);
                    myLocation.setVisibility(View.VISIBLE);
                    myLocation.setEnabled(true);
                    if (animation != null) animation.start();
                } else animation_cnt = 0;
            }
        });
        /** 맵 롱 클릭 시 이벤트 처리 **/
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                /*
                if (mem_id == "" || drone_exist == false)
                    return;*/
                fly_enable_check_marker(latLng);
                delay_marker_display(latLng);
                /*마커 찍었을때 카메라 포커스도 같이 움직이는 코드
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 11f);
                if (mMap != null) {
                    mMap.moveCamera(update);
                }*/
            }
        });
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myLocation.setVisibility(View.INVISIBLE);
                myLocation.setEnabled(false);
            }
        }, 1500);

    }

    /**
     * 장소검색용 가로 형태 다이얼로그
     **/
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

            /** 어댑터 안에서 지역내용 받아와서 지역내용에 "대한민국"이 포함된 지역만 필터링 **/
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
        request.setInterval(2000);
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

    // 초기 현재 나의 위치로 카메라 이동
    private void displayMessage(Location location) {
        if (location != null) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 11f);

            if (mMap != null) {
                mMap.moveCamera(update);
            }
        }
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
//            GroundOverlayOptions overlayOptions1 = new GroundOverlayOptions().
//                    image(BitmapDescriptorFactory.fromResource(R.drawable.m22)).positionFromBounds(bounds);
//            mMap.addGroundOverlay(overlayOptions1);
    }

    /**
     * 4대 비행가능 공식 계산 후 -> 내 위치 마커에 비행가능 혹은 불가능 표시
     **/
    private void add_marker(Location location, int[] bool) {
        if (my_marker != null) {
            my_marker.remove();
        }
        MarkerOptions options = new MarkerOptions();
        if (location != null) {
            options.position(new LatLng(location.getLatitude(), location.getLongitude()));
        } else {
            getData();
            return;
        }
        // 4대 비행가능 요소 중 1개의 불가능 요소만 있어도, 비행 불가능
        boolean fly = true;
        if (bool != null) {
            for (int j = 0; j < 4; j++) {
                if (bool[j] == 0) {
                    fly = false;
                    break;
                }
            }
        } else {
            Toast.makeText(context, "데이터 읽어오는데 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fly == false) {
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_imp1_11));
        } else {
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_pos1_11));
        }
        /*
        if (mem_id != "" && drone_exist == true) {
            if (fly == false) {
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_imp1_11));
            } else {
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_pos1_11));
            }
        } else if (mem_id != "" && drone_exist == false) { // drone_exist : false면 터치 시 커스텀 다이얼로그 비활성화
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_not_drone));
        } else { // 비회원이면,
            drone_exist = true; // 드론 유/무 확인 후 창 비활성화 용도 인데, 비회원 일때도 커스텀다이얼로그를 활성화 시킨 후 로그인 시켜야하므로 true값을 넣어줌
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_imp1_1_unable));
        }
        */
        my_marker = mMap.addMarker(options);
    }

    /**
     * 마커 클릭 시 이벤트 처리
     **/
    @Override
    public boolean onMarkerClick(Marker marker) {
        flying_state_Dialog my_marker_dialog;
        flying_state_Dialog click_marker_dialog;


        if (drone_exist == true && marker.equals(clickMarker)) { /** 클릭 마커 클릭 시 **/
            click_marker_dialog = new flying_state_Dialog(context, bool1);
            click_marker_dialog.setCanceledOnTouchOutside(true);
            click_marker_dialog.show();
        } else if (drone_exist == true || mem_id == "" && marker.equals(my_marker)) { /** 내 위치 마커 클릭 시 **/ // 드론있으면 다이얼로그 띄워주기, 비회원일때도 띄워줘야함
            my_marker_dialog = new flying_state_Dialog(context, bool);
            my_marker_dialog.setCanceledOnTouchOutside(true);
            my_marker_dialog.show();
        } else if (mem_id != "" && drone_exist == false && marker.equals(my_marker)) { /** 유저가 드론이 없는 상태에서 마커 클릭 시 **/
            AddDrone addDrone; // 사용자의 드론이 없을 시 -> 드론추가 커스텀 다이얼로그 바로 표시
            addDrone = new AddDrone(context, getActivity());
            addDrone.setCanceledOnTouchOutside(true);
            addDrone.show();
        }
        return false;
    }

    /**
     * 롱 클릭 마커 정보창 클릭 시
     **/
    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.equals(clickMarker)) {
            flying_state_Dialog dialog;
            dialog = new flying_state_Dialog(context, bool1);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
    }

    /**
     * 4대 비행가능 요소 가능/불가능 표시 다이얼로그 (마커 클릭시)
     **/
    class flying_state_Dialog extends Dialog {
        ImageView i1, i2, i3, i4;
        ImageView t1, t2, t3, t4;
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
            t1 = (ImageView) findViewById(R.id.dial_text1);
            t2 = (ImageView) findViewById(R.id.dial_text2);
            t3 = (ImageView) findViewById(R.id.dial_text3);
            t4 = (ImageView) findViewById(R.id.dial_text4);
            /*
            t1 = (TextView)findViewById(R.id.text_area);
            t2 = (TextView)findViewById(R.id.text_time);
            t3 = (TextView)findViewById(R.id.text_wind);
            t4 = (TextView)findViewById(R.id.text_magnetic);
            */
            btn = (Button) findViewById(R.id.dial_btn);
            /*exit_btn = (Button) findViewById(R.id.exit_btn);*/
            /*
            contact_btn = (Button) findViewById(R.id.contact_info_btn);
            if(PropertyManager.getInstance().getId() == "") {
                contact_btn.setEnabled(false);
                contact_btn.setVisibility(View.GONE);
            }*/
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mem_id == "") {
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
            if (bool[0] == 1) {
                i1.setImageResource(R.drawable.fly_enable_place_image);
                t1.setImageResource(R.drawable.fly_enable_place_text);
            } else {
                i1.setImageResource(R.drawable.no_fly_place_image);
                t1.setImageResource(R.drawable.no_fly_place_text);
            }

        /*    if (mem_id != "") {
        *
        * 2017-03-24
        * 회원/비회원 구분 없이 4대비행 팝업창 노출! */

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

            if (bool[1] == 1) {
                i2.setImageResource(R.drawable.fly_enable_time_image);
                t2.setImageResource(R.drawable.fly_enable_time_text);
            } else {
                i2.setImageResource(R.drawable.no_fly_time_image);
                t2.setImageResource(R.drawable.no_fly_time_text);
            }
            if (bool[2] == 1) {
                i3.setImageResource(R.drawable.fly_enable_wind_image);
                t3.setImageResource(R.drawable.fly_enable_wind_text);
            } else {
                i3.setImageResource(R.drawable.no_fly_wind_image);
                t3.setImageResource(R.drawable.no_fly_wind_text);
            }
            if (bool[3] == 1) {
                i4.setImageResource(R.drawable.fly_enable_magnetic_image);
                t4.setImageResource(R.drawable.fly_enable_magnetic_text);
            } else {
                i4.setImageResource(R.drawable.no_fly_magnetic_image);
                t4.setImageResource(R.drawable.no_fly_magnetic_text);
            }
            for (int j = 0; j < 4; j++) {
                if (bool[j] == 0) {
                    btn.setBackgroundResource(R.drawable.not_here);
                    return;
                } else btn.setBackgroundResource(R.drawable.fly_enable);
            }
            /* 비회원일때 안보여주는 기능!

            2017-03-24 기획자 요청으로 해당 기능 주석.


            } else {
                i1.setImageResource(R.drawable.not_user_place_image);
                t1.setImageResource(R.drawable.not_user_login_plz);
                i2.setImageResource(R.drawable.not_user_time_image);
                t2.setImageResource(R.drawable.not_user_login_plz);
                i3.setImageResource(R.drawable.not_user_wind_image);
                t3.setImageResource(R.drawable.not_user_login_plz);
                i4.setImageResource(R.drawable.not_user_magnetic_image);
                t4.setImageResource(R.drawable.not_user_login_plz);
                btn.setBackgroundResource(R.drawable.not_user);
            }*/
        }

        public flying_state_Dialog(Context context, int[] bool) {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);
            this.bool = bool;
        }
    }


    /**
     * 현재 내위치의 비행 가능/불가능을 계산
     **/
    public void getData() {
        //비행가능구역
        if (location == null) {
            delay_getData();
        } else {
            // 내 위치 기준에서 4대 비행공식 계산
            fly_enable_check_mylocation();
        }
    }

    /**
     * 4개의 공역안에 존재 하는 지 확인을 위한 KML 함수들
     **/
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

    /**
     * 위치기능(GPS) 활성화 유무 체크
     **/
    public void gps_check() {
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

    /**
     * 해당위치의 비행가능공식계산 후, 클릭 마커표시
     **/
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
                    /** 클릭 마커 옵션 **/
                    clickMarker_option = new MarkerOptions();
                    clickMarker_option.position(latLng);
                    if (liesInside[1][0] == true) clickMarker_option.title("비행금지구역");
                    else if (liesInside[1][1] == true) clickMarker_option.title("비행제한구역");
                    else if (liesInside[1][2] == true) clickMarker_option.title("관제권");
                    else if (liesInside[1][3] == true) clickMarker_option.title("비행위험구역");
                    else clickMarker_option.title("일반공역");

                    clickMarker_option.icon(BitmapDescriptorFactory.fromResource(R.drawable.click_marker));
                    clickMarker_option.snippet("풍속 : " + wind_click + "m/s");
                    clickMarker = mMap.addMarker(clickMarker_option);
                    clickMarker.showInfoWindow();
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            if (marker.equals(clickMarker)) {
                                flying_state_Dialog dialog;
                                dialog = new flying_state_Dialog(context, bool1);
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

    /**
     * 4대 비행공식 계산(내 위치)
     **/
    public void fly_enable_check_mylocation() {
        if (mem_id != "") {
            LatLng latlng;
            try {
                latlng = new LatLng(location.getLatitude(), location.getLongitude());
            } catch (NullPointerException e) {
                e.getMessage();
                return;
            }
            // liesInside[] index is..
            // [0]: 금지구역
            // [1]: 제한구역
            // [2]: 관제권
            // [3]: 위험구역

            /** 비행 구역 확인 **/
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
                    Toast.makeText(context, "데이터를 받아오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            if (openWeather_count == 0) {
                /** 풍속,일출,일몰 값 받아오기 **/
                NetworkManager.getInstance().getWind(MyApplication.getContext(), "" + latlng.latitude, "" + latlng.longitude, new NetworkManager.OnResultListener<WeatherResult>() {
                    @Override
                    public void onSuccess(Request request, WeatherResult result) {
                        wind = result.getWind().getSpeed();
                        sunrise = result.getSun().getSunrise();
                        sunset = result.getSun().getSunset();
                        /** 4대 비행 공식 계산 **/
                        if ((wind != null && sunrise != null && sunset != null)) {
                            NetworkManager.getInstance().getResistance(MyApplication.getContext(), mem_id, new NetworkManager.OnResultListener<DroneResistanceResult>() {
                                @Override
                                public void onSuccess(Request request, DroneResistanceResult result) {
                                    List<MemDrone> memDrone;
                                    DroneResistance dr;
                                    String dr_resistance1;
                                    long now = System.currentTimeMillis() / 1000;
                                    double dr_wind;
                                    long l_sunrise;
                                    long l_sunset;
                                    dr = result.getResult();
                                    memDrone = dr.getMemResultDrone();
                                    boolean dr_empty = memDrone.isEmpty(); // 사용자의 드론이 없으면 true, 있으면 false
                                    if (dr_empty == false) {
                                        drone_exist = true;
                                        dr_resistance1 = dr.getDroneResistance().toString();
                                        dr_resistance = Double.parseDouble(dr_resistance1);
                                        dr_wind = Double.parseDouble(wind);
                                        l_sunrise = Long.parseLong(sunrise);
                                        l_sunset = Long.parseLong(sunset);
                                        if (liesInside[0][0] == false && liesInside[0][1] == false && liesInside[0][2] == false)
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
                                    } else if (dr_empty == true) {
                                        for (int i = 0; i < bool.length; i++) {
                                            bool[i] = 0;
                                        }
                                        drone_exist = false;
                                        if (location != null) {
                                            add_marker(location, bool);
                                        }
                                    }


                                }

                                @Override
                                public void onFail(Request request, IOException exception) {
                                    Toast.makeText(context, "데이터를 받아오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(Request request, IOException exception) {
                        Toast.makeText(context, "데이터를 받아오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                openWeather_count++;
            } else {
                /** 4대 비행 공식 계산 **/
                if ((wind != null && sunrise != null && sunset != null)) {

                    //Log.e("내아이디","내놔"+mem_id);


                    NetworkManager.getInstance().getResistance(MyApplication.getContext(), mem_id, new NetworkManager.OnResultListener<DroneResistanceResult>() {
                        @Override
                        public void onSuccess(Request request, DroneResistanceResult result) {
                            List<MemDrone> memDrone;
                            String dr_resistance1;
                            DroneResistance dr;
                            long now = System.currentTimeMillis() / 1000;
                            double dr_wind;
                            long l_sunrise;
                            long l_sunset;
                            dr = result.getResult();
                            memDrone = dr.getMemResultDrone();
                            boolean dr_empty = memDrone.isEmpty(); // 사용자의 드론이 없으면 true, 있으면 false
                            if (dr_empty == false) {
                                drone_exist = true;
                                dr_resistance1 = dr.getDroneResistance().toString();
                                dr_resistance = Double.parseDouble(dr_resistance1);
                                dr_wind = Double.parseDouble(wind);
                                l_sunrise = Long.parseLong(sunrise);
                                l_sunset = Long.parseLong(sunset);
                                if (liesInside[0][0] == false && liesInside[0][1] == false && liesInside[0][2] == false)
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
                            } else if (dr_empty == true) {
                                for (int i = 0; i < bool.length; i++) {
                                    bool[i] = 0;
                                }
                                drone_exist = false;
                                if (location != null) {
                                    add_marker(location, bool);
                                }
                            }
                        }

                        @Override
                        public void onFail(Request request, IOException exception) {
                            Toast.makeText(context, "데이터를 받아오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                openWeather_count++;
                if (openWeather_count == 3) {
                    openWeather_count = 0;
                }
            }
        } else { // 비회원 일때


/*
            drone_exist = false;
            for (int i = 0; i < 4; i++) {
                bool[i] = 0;
                bool1[i] = 0;
            }
            if (location != null) {
                add_marker(location, bool);
            }*/

/**
 2017-03-24
 기획 변경으로
 비회원도 '드론 유(디폴트:매빅 프로의 resistance)', 4대비행팝업 확인 가능하도록 수정.
 **/

            drone_exist = true;

            LatLng latlng;
            try {
                latlng = new LatLng(location.getLatitude(), location.getLongitude());
            } catch (NullPointerException e) {
                e.getMessage();
                return;
            }
            // liesInside[] index is..
            // [0]: 금지구역
            // [1]: 제한구역
            // [2]: 관제권
            // [3]: 위험구역

            /** 비행 구역 확인 **/
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
                    Toast.makeText(context, "데이터를 받아오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            if (openWeather_count == 0) {
                /** 풍속,일출,일몰 값 받아오기 **/
                NetworkManager.getInstance().getWind(MyApplication.getContext(), "" + latlng.latitude, "" + latlng.longitude, new NetworkManager.OnResultListener<WeatherResult>() {
                    @Override
                    public void onSuccess(Request request, WeatherResult result) {
                        wind = result.getWind().getSpeed();
                        sunrise = result.getSun().getSunrise();
                        sunset = result.getSun().getSunset();
                        /** 4대 비행 공식 계산 **/
                        if ((wind != null && sunrise != null && sunset != null)) {
                            dr_resistance = 10.0; // 비회원은 디폴트로 매빅 프로의 바람저항을 갖고 있다고 하고 4대비행팝업 보여줌.


                            long now = System.currentTimeMillis() / 1000;
                            double dr_wind;
                            long l_sunrise;
                            long l_sunset;
                            dr_wind = Double.parseDouble(wind);
                            l_sunrise = Long.parseLong(sunrise);
                            l_sunset = Long.parseLong(sunset);
                            if (liesInside[0][0] == false && liesInside[0][1] == false && liesInside[0][2] == false)
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
                        }


                    }

                    // 풍속,일출,일물쪽 네트워크 통신
                    @Override
                    public void onFail(Request request, IOException exception) {
                        Toast.makeText(context, "데이터를 받아오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                openWeather_count++;

            } else { // 팝업창 누르면 3번 중 한번만 openWeather API 호출함. //

                /** 4대 비행 공식 계산 **/
                if ((wind != null && sunrise != null && sunset != null)) {
                    long now = System.currentTimeMillis() / 1000;
                    double dr_wind;
                    long l_sunrise;
                    long l_sunset;
                    dr_resistance = 10.0; // 비회원은 디폴트로 값을 정해줘서 4대비행팝업 볼 수 있도록 하였음!
                    dr_wind = Double.parseDouble(wind);
                    l_sunrise = Long.parseLong(sunrise);
                    l_sunset = Long.parseLong(sunset);
                    if (liesInside[0][0] == false && liesInside[0][1] == false && liesInside[0][2] == false)
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
                }
            }
        }

        openWeather_count++;
        if (openWeather_count == 3) {
            openWeather_count = 0;
        }
    }


    /**
     * 4대 비행공식 계산(마커 위치)
     **/

    public void fly_enable_check_marker(LatLng latlng) {
        if (mem_id != "") {
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
                    Toast.makeText(context, "데이터를 받아오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
                    if ((wind_click != null && sunrise_click != null && sunset_click != null)) {
                        NetworkManager.getInstance().getResistance(MyApplication.getContext(), mem_id, new NetworkManager.OnResultListener<DroneResistanceResult>() {
                            @Override
                            public void onSuccess(Request request, DroneResistanceResult result) {
                                List<MemDrone> memDrone;
                                String dr_resistance1;
                                DroneResistance dr;
                                long now = System.currentTimeMillis() / 1000;
                                double dr_wind;
                                long l_sunrise;
                                long l_sunset;
                                dr = result.getResult();
                                memDrone = dr.getMemResultDrone();
                                boolean dr_empty = memDrone.isEmpty(); // 사용자 드론이 없으면 true, 있으면 false
                                if (dr_empty == false) {
                                    drone_exist = true;
                                    dr_resistance1 = dr.getDroneResistance().toString();
                                    dr_resistance = Double.parseDouble(dr_resistance1);
                                    dr_wind = Double.parseDouble(wind_click);
                                    l_sunrise = Long.parseLong(sunrise_click);
                                    l_sunset = Long.parseLong(sunset_click);
                                    //      Log.e("일출",l_sunrise+"");
                                    //     Log.e("일몰",l_sunset+"");
                                    //    Log.e("현탐",now+"");

                                    if (liesInside[1][0] == false && liesInside[1][1] == false && liesInside[1][2] == false)
                                        bool1[0] = 1;
                                    else bool1[0] = 0;

                                    if (l_sunrise < now && now < l_sunset) bool1[1] = 1;
                                    else bool1[1] = 0;

                                    if (dr_wind < dr_resistance) bool1[2] = 1;
                                    else bool1[2] = 0;

                                    if (magnetic < 5) bool1[3] = 1;
                                    else bool1[3] = 0;

                                    // 비행 가능, 불가능 표시 마커
                                    /*
                                    if ((bool1 != null) && location != null) {
                                        add_marker(location, bool1);
                                    }
                                    */
                                } else if (dr_empty == true) {

                                    for (int i = 0; i < bool1.length; i++) {
                                        bool1[i] = 0;
                                    }
                                    drone_exist = false;
                                    /*
                                    if(location != null) {
                                        add_marker(location, bool1);
                                    }
                                    */
                                }
                                getData_success = true;
                            }

                            @Override
                            public void onFail(Request request, IOException exception) {
                                Toast.makeText(context, "데이터를 받아오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFail(Request request, IOException exception) {
                    Toast.makeText(context, "데이터를 받아오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        } else { // 비회원 일때
            drone_exist = true;

            // liesInside[] index is..
            // [0]: 금지구역
            // [1]: 제한구역
            // [2]: 관제권
            // [3]: 위험구역

            /** 비행 구역 확인 **/
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
                    Toast.makeText(context, "데이터를 받아오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            /** 풍속,일출,일몰 값 받아오기 **/
            NetworkManager.getInstance().getWind(MyApplication.getContext(), "" + latlng.latitude, "" + latlng.longitude, new NetworkManager.OnResultListener<WeatherResult>() {
                @Override
                public void onSuccess(Request request, WeatherResult result) {
                    wind_click = result.getWind().getSpeed();
                    sunrise_click = result.getSun().getSunrise();
                    sunset_click = result.getSun().getSunset();
                    /** 4대 비행 공식 계산 **/
                    if ((wind_click != null && sunrise_click != null && sunset_click != null)) {
                        dr_resistance = 10.0; // 비회원은 디폴트로 매빅 프로의 바람저항을 갖고 있다고 하고 4대비행팝업 보여줌.

                        long now = System.currentTimeMillis() / 1000;
                        double dr_wind;
                        long l_sunrise;
                        long l_sunset;
                        dr_wind = Double.parseDouble(wind_click);
                        l_sunrise = Long.parseLong(sunrise_click);
                        l_sunset = Long.parseLong(sunset_click);
                        if (liesInside[1][0] == false && liesInside[1][1] == false && liesInside[1][2] == false)
                            bool1[0] = 1;
                        else bool1[0] = 0;
                        if (l_sunrise < now && now < l_sunset) bool1[1] = 1;
                        else bool1[1] = 0;
                        if (dr_wind < dr_resistance) bool1[2] = 1;
                        else bool1[2] = 0;
                        if (magnetic < 5) bool1[3] = 1;
                        else bool1[3] = 0;
                        // 비행 가능, 불가능 표시 마커
                        /*
                        if ((bool != null) && location != null) {
                            add_marker(location, bool);
                        }*/
                        getData_success = true;
                    }


                }

                // 풍속,일출,일물쪽 네트워크 통신
                @Override
                public void onFail(Request request, IOException exception) {
                    Toast.makeText(context, "데이터를 받아오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }



    /**
     * 장소 검색 다이얼로그에서 위치 클릭 시
     **/
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

    /**
     * 장소 검색 다이얼로그에서 위치 클릭 시 이벤트처리
     **/
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
                Toast.makeText(getContext(), "드론을 선택해주세요", Toast.LENGTH_SHORT).show();
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
