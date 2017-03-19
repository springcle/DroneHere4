package com.santamaria.dronehere.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.santamaria.dronehere.MyApplication;
import com.santamaria.dronehere.data.AddGatheringResult;
import com.santamaria.dronehere.data.AddReviewResult;
import com.santamaria.dronehere.data.Dc1Result;
import com.santamaria.dronehere.data.DrawerResult;
import com.santamaria.dronehere.data.DroneDetailResult;
import com.santamaria.dronehere.data.DroneFlightResult;
import com.santamaria.dronehere.data.DroneRecommendResult;
import com.santamaria.dronehere.data.DroneResistanceResult;
import com.santamaria.dronehere.data.DroneSearchResult;
import com.santamaria.dronehere.data.EmailResult;
import com.santamaria.dronehere.data.GathWriteResult;
import com.santamaria.dronehere.data.GatherResult;
import com.santamaria.dronehere.data.LocaContentResult;
import com.santamaria.dronehere.data.LocaListResult;
import com.santamaria.dronehere.data.LoginResult;
import com.santamaria.dronehere.data.MagneticResult;
import com.santamaria.dronehere.data.MemberResult;
import com.santamaria.dronehere.data.NewsResult;
import com.santamaria.dronehere.data.WeatherResult;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dongja94 on 2016-05-09.
 */
public class NetworkManager {
    private static NetworkManager instance;
    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    private static final int DEFAULT_CACHE_SIZE = 50 * 1024 * 1024;
    private static final String DEFAULT_CACHE_DIR = "miniapp";
    OkHttpClient mClient;
    private NetworkManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Context context = MyApplication.getContext();
        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));

        File dir = new File(context.getExternalCacheDir(), DEFAULT_CACHE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        builder.cache(new Cache(dir, DEFAULT_CACHE_SIZE));

        builder.connectTimeout(300, TimeUnit.SECONDS);
        builder.readTimeout(300, TimeUnit.SECONDS);
        builder.writeTimeout(300, TimeUnit.SECONDS);

        mClient = builder.build();
    }

    public interface OnResultListener<T> {
        public void onSuccess(Request request, T result);
        public void onFail(Request request, IOException exception);
    }

    private static final int MESSAGE_SUCCESS = 1;
    private static final int MESSAGE_FAIL = 2;

    class NetworkHandler extends Handler {
        public NetworkHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NetworkResult result = (NetworkResult)msg.obj;
            switch (msg.what) {
                case MESSAGE_SUCCESS :
                    result.listener.onSuccess(result.request, result.result);
                    break;
                case MESSAGE_FAIL :
                    result.listener.onFail(result.request, result.excpetion);
                    break;
            }
        }
    }

    NetworkHandler mHandler = new NetworkHandler(Looper.getMainLooper());

    static class NetworkResult<T> {
        Request request;
        OnResultListener<T> listener;
        IOException excpetion;
        T result;
    }

    Gson gson = new Gson();

    //서버URL
    private static final String DRONE_HERE="http://52.78.125.154:3000";

    //이메일중복검사
    private static final String DRONE_EMAIL = DRONE_HERE + "/signup/overlap";
    public Request getEmail(Object tag, String mem_email, OnResultListener<EmailResult> listener) {

        RequestBody body = new FormBody.Builder()
                .add("mem_email", mem_email)
                .build();

        String url = String.format(DRONE_EMAIL);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        final NetworkResult<EmailResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    EmailResult data = gson.fromJson(response.body().charStream(), EmailResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }


    //로그인
    private static final String DRONE_LOGIN = DRONE_HERE + "/login";
    public Request getLogin(Object tag, String mem_email,String mem_pw, OnResultListener<LoginResult> listener) {

        RequestBody body = new FormBody.Builder()
                .add("mem_email", mem_email)
                .add("mem_pw", mem_pw)
                .build();

        String url = String.format(DRONE_LOGIN);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        final NetworkResult<LoginResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    LoginResult data = gson.fromJson(response.body().charStream(), LoginResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }




    //드론아예없
    private static final String DRONE_JOIN2 = DRONE_HERE + "/signup/search/nodrone";
    public Request getJoin2(Object tag, String mem_pw,String mem_name,String mem_email, OnResultListener listener) {

        RequestBody body = new FormBody.Builder()
                .add("mem_pw", mem_pw)
                .add("mem_name", mem_name)
                .add("mem_email", mem_email)
                .build();

        String url = String.format(DRONE_JOIN2);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        final NetworkResult result = new NetworkResult();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }
    //드론있음
    private static final String DRONE_JOIN3 = DRONE_HERE + "/signup/search/choice";
    public Request getJoin3(Object tag, String mem_pw,String mem_name,String mem_email,String dr_id, OnResultListener listener) {

        RequestBody body = new FormBody.Builder()
                .add("mem_pw", mem_pw)
                .add("mem_name", mem_name)
                .add("mem_email", mem_email)
                .add("dr_id", dr_id)
                .build();

        String url = String.format(DRONE_JOIN3);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        final NetworkResult result = new NetworkResult();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }
    //드론 검색 후 없으면
    private static final String DRONE_JOIN4 = DRONE_HERE + "/signup/search/insert";
    public Request getJoin4(Object tag, String mem_pw,String mem_name,String mem_email,String dr_name,String Number, OnResultListener listener) {

        RequestBody body = new FormBody.Builder()
                .add("mem_pw", mem_pw)
                .add("mem_name", mem_name)
                .add("mem_email", mem_email)
                .add("dr_name", dr_name)
                .add("dr_weight1", String.valueOf(Number))
                .build();

        String url = String.format(DRONE_JOIN4);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        final NetworkResult result = new NetworkResult();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }



    //햄버거 드론추가
    private static final String DRONE_ADDDRONE = DRONE_HERE + "/hambuger/user/edit/user_droneplus";
    public Request getDadd(Object tag, String mem_id,String dr_id, OnResultListener listener) {

        RequestBody body = new FormBody.Builder()
                .add("mem_id", mem_id)
                .add("dr_id", dr_id)
                .build();

        String url = String.format(DRONE_ADDDRONE);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        final NetworkResult result = new NetworkResult();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }


    // 24. 햄버거메뉴 -> 개인 정보 수정화면 조회
    private static final String DRONE_FIX = DRONE_HERE + "/hambuger/user/view";
    public Request getFix(Object tag,String mem_id, OnResultListener<MemberResult> listener) {

        RequestBody body = new FormBody.Builder()
                .add("mem_id", mem_id)
                .build();

        String url = String.format(DRONE_FIX);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        final NetworkResult<MemberResult> result = new NetworkResult();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    MemberResult data = gson.fromJson(response.body().charStream(), MemberResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }

    //풍속저항 값
    private static final String DRONE_RESISTANCE = DRONE_HERE + "/hambuger/user/view";
    public Request getResistance(Object tag,String mem_id, OnResultListener<DroneResistanceResult> listener) {

        RequestBody body = new FormBody.Builder()
                .add("mem_id", mem_id)
                .build();

        String url = String.format(DRONE_RESISTANCE);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        final NetworkResult<DroneResistanceResult> result = new NetworkResult();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    DroneResistanceResult data = gson.fromJson(response.body().charStream(), DroneResistanceResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }




    //수정하기
    private static final String DRONE_FIX2 = DRONE_HERE + "/hambuger/user/edit";
    public Request getFix2(Object tag,String mem_id,String mem_name,List<String> dr_delete,String dr_select, OnResultListener listener) {

//        RequestBody body = new FormBody.Builder()
//                .add("mem_id", mem_id)
//                .add("mem_name",mem_name)
//                //      .add("dr_delete",dr_delete)
//                .add("dr_select",dr_select)
//                .build();
        FormBody.Builder builder = new FormBody.Builder()
                .add("mem_id", mem_id)
                .add("mem_name",mem_name);
                //      .add("dr_delete",dr_delete)
                if(dr_select != null) builder.add("dr_select",dr_select); // 지정한 드론이 없으면, 주력드론을 전달할 필요가 없음

        for(int i = 0; i<dr_delete.size(); i++){
            builder.add("dr_delete",dr_delete.get(i));
        }
        RequestBody body = builder.build();
        String url = String.format(DRONE_FIX2);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        final NetworkResult result = new NetworkResult();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }




    //장소리스트
    private static final String DRONE_LOCATION_LIST = DRONE_HERE + "/location/list";
    public Request getLocaList(Object tag, OnResultListener<LocaListResult> listener) {
        Request request = new Request.Builder()
                .url(DRONE_LOCATION_LIST)
                .get()
                .build();
        final NetworkResult<LocaListResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    LocaListResult data = gson.fromJson(response.body().charStream(), LocaListResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }






    //상세장소내용
    private static final String DRONE_LOCATION_CONTENT = DRONE_HERE + "/location/list/%s";
    public Request getLocaContent(Object tag, int loca_num, OnResultListener<LocaContentResult> listener) {

        String url = String.format(DRONE_LOCATION_CONTENT, ""+loca_num);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        final NetworkResult<LocaContentResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    LocaContentResult data = gson.fromJson(response.body().charStream(), LocaContentResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }


    //상세모임내용
    private static final String DRONE_GATHERING = DRONE_HERE + "/gathering/%s";
    public Request getGather(Object tag, String id, OnResultListener<GatherResult> listener) {

        String url = String.format(DRONE_GATHERING, ""+id);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        final NetworkResult<GatherResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    GatherResult data = gson.fromJson(response.body().charStream(), GatherResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }


    //뉴스
    private static final String NEWS = DRONE_HERE + "/news";
    public Request getNews(Object tag, OnResultListener<NewsResult> listener) {

        String url = String.format(NEWS);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        final NetworkResult<NewsResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    NewsResult data = gson.fromJson(response.body().charStream(), NewsResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }


    //드론추천
    private static final String DRONE_RECOMMEND = DRONE_HERE + "/dronedb/recommend";
    public Request getDbRecommend(Object tag, OnResultListener<DroneRecommendResult> listener) {

        String url = String.format(DRONE_RECOMMEND);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        final NetworkResult<DroneRecommendResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    DroneRecommendResult data = gson.fromJson(response.body().charStream(), DroneRecommendResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }

    //드론추천(전체, 별점순)
    private static final String DRONE_RECOMMEDN_RATE = DRONE_HERE + "/dronedb/listall/1";
    public Request getDroneRecommendRate(Object tag, OnResultListener<DroneRecommendResult> listener) {
        String url = String.format(DRONE_RECOMMEDN_RATE);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        final NetworkResult<DroneRecommendResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    DroneRecommendResult data = gson.fromJson(response.body().charStream(), DroneRecommendResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }
    //드론추천(전체, 이름순)
    private static final String DRONE_RECOMMEDN_NAME = DRONE_HERE + "/dronedb/listall/2";
    public Request getDroneRecommendName(Object tag, OnResultListener<DroneRecommendResult> listener) {
        String url = String.format(DRONE_RECOMMEDN_NAME);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        final NetworkResult<DroneRecommendResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    DroneRecommendResult data = gson.fromJson(response.body().charStream(), DroneRecommendResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }
    //드론추천(전체, 제조사 순)
    private static final String DRONE_RECOMMEDN_BRAND = DRONE_HERE + "/dronedb/listall/3";
    public Request getDroneRecommendBrand(Object tag, OnResultListener<DroneRecommendResult> listener) {
        String url = String.format(DRONE_RECOMMEDN_BRAND);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        final NetworkResult<DroneRecommendResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    DroneRecommendResult data = gson.fromJson(response.body().charStream(), DroneRecommendResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }
    //드론추천(전체, 용도 순)
    private static final String DRONE_RECOMMEDN_USAGE = DRONE_HERE + "/dronedb/listall/4";
    public Request getDroneRecommendUsage(Object tag, OnResultListener<DroneRecommendResult> listener) {
        String url = String.format(DRONE_RECOMMEDN_USAGE);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        final NetworkResult<DroneRecommendResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    DroneRecommendResult data = gson.fromJson(response.body().charStream(), DroneRecommendResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }

    //드론검색
    private static final String DRONE_SEARCH = DRONE_HERE + "/dronedb/search/%s";
    public Request getDroneSearch(Object tag,String dr_name,OnResultListener<DroneSearchResult> listener) {


        String url = String.format(DRONE_SEARCH,dr_name);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        final NetworkResult<DroneSearchResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    DroneSearchResult data = gson.fromJson(response.body().charStream(), DroneSearchResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }


    //드론상세
    private static final String DRONE_DETAIL = DRONE_HERE + "/dronedb/search/select/%s";  //스트링이사라짐
    public Request getDroneDetail(Object tag,String _id,OnResultListener<DroneDetailResult> listener) {

        String url = String.format(DRONE_DETAIL,_id);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        final NetworkResult<DroneDetailResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    DroneDetailResult data = gson.fromJson(response.body().charStream(), DroneDetailResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }


    //장소리뷰더보기
    private static final String DRONE_ADDREVIEW = DRONE_HERE + "/location/list/%s/morereview/%s";
    public Request getAddReview(Object tag,int loca_num,int page,OnResultListener<AddReviewResult> listener) {

        String url = String.format(DRONE_ADDREVIEW,loca_num,page);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        final NetworkResult<AddReviewResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    AddReviewResult data = gson.fromJson(response.body().charStream(), AddReviewResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }


    //드론댓글더보기
    private static final String DRONE_ADDDBRV = DRONE_HERE + "/dronedb/search/select/%s/morereview/%s";
    public Request getDbAdd1(Object tag,String dr_id,int page,OnResultListener<AddReviewResult> listener) {

        String url = String.format(DRONE_ADDDBRV,dr_id,page);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        final NetworkResult<AddReviewResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    AddReviewResult data = gson.fromJson(response.body().charStream(), AddReviewResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }



    //모임댓글더보기
    private static final String DRONE_ADDREVIEW2 = DRONE_HERE + "/gathering/%s/morereview/%s";
    public Request getAddReview2(Object tag,String gath_id,int page,OnResultListener<AddReviewResult> listener) {

        String url = String.format(DRONE_ADDREVIEW2,gath_id,page);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        final NetworkResult<AddReviewResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    AddReviewResult data = gson.fromJson(response.body().charStream(), AddReviewResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }



    //다가오는모임더보기
    private static final String DRONE_ADDGATHER = DRONE_HERE + "/location/list/%s/moregathe_coming/%s";
    public Request getAddGath(Object tag, int loca_num,int page ,OnResultListener<AddGatheringResult> listener) {

        String url = String.format(DRONE_ADDGATHER,loca_num,page);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        final NetworkResult<AddGatheringResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    AddGatheringResult data = gson.fromJson(response.body().charStream(), AddGatheringResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }


    //지난모임더보기
    private static final String DRONE_ADDGATHER2 = DRONE_HERE + "/location/list/%s/moregathe_past/%s";
    public Request getAddGath2(Object tag, int loca_num,int page ,OnResultListener<AddGatheringResult> listener) {

        String url = String.format(DRONE_ADDGATHER2,loca_num,page);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        final NetworkResult<AddGatheringResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    AddGatheringResult data = gson.fromJson(response.body().charStream(), AddGatheringResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }


    //모임추가
    private static final String DRONE_GATH_WRITE = DRONE_HERE + "/gathering/write";
    public Request getWriteGath(Object tag, String loca_id, String mem_id, String gathe_name, String  gathe_regdate, String gathe_location, String gathe_content, List<File> gathe_photo, OnResultListener listener) {


        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("loca_id", loca_id)
                .addFormDataPart("mem_id", mem_id)
                .addFormDataPart("gathe_regdate", gathe_regdate)
                .addFormDataPart("gathe_name", gathe_name)
                .addFormDataPart("gathe_location", gathe_location)
                .addFormDataPart("gathe_content", gathe_content);

        if(gathe_photo!=null){
            for (int i = 0; i < gathe_photo.size(); i++) {
                builder
                        .addFormDataPart("gathe_photo", gathe_photo.get(i).getName(),
                                RequestBody.create(MediaType.parse("image/jpeg"), gathe_photo.get(i)));
            }
        }
        RequestBody body = builder.build();


//        MultipartBody body = new MultipartBody.Builder()
//                .add("loca_id",loca_id)
//                .add("mem_id",mem_id)
//                .add("gathe_regdate",gathe_regdate)
//                .add("gathe_name",gathe_name)

//                .add("gathe_location",gathe_location)
//                .add("gathe_content",gathe_content)
//                .add("gathe_photo", gathe_photo)
//                .build();

        String url = String.format(DRONE_GATH_WRITE);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        final NetworkResult<GathWriteResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }




    //모임댓글남기기
    private static final String DRONE_GATH_REVIEW = DRONE_HERE + "/gathering/review";
    public Request getWriteGR(Object tag ,String gathe_id,String mem_id,String re_content, OnResultListener listener) {

        RequestBody body = new FormBody.Builder()
                .add("gathe_id",gathe_id)
                .add("mem_id",mem_id)
                .add("re_content", re_content)
                .build();

        String url = String.format(DRONE_GATH_REVIEW);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        final NetworkResult result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }




    //장소댓글남기기
    private static final String DRONE_LocaReview_Write = DRONE_HERE + "/location/list/review";
    public Request getWriteLocaReview(Object tag , String loca_id, String mem_id, float re_rate, String  re_content, OnResultListener listener) {

        RequestBody body = new FormBody.Builder()
                .add("loca_id",loca_id)
                .add("mem_id", mem_id)
                .add("re_rate", String.valueOf(re_rate))
                .add("re_content", re_content)
                .build();

        String url = String.format(DRONE_LocaReview_Write);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        final NetworkResult<GathWriteResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }

    //db댓글남기기
    private static final String DRONE_DB_RV_WRITE = DRONE_HERE + "/dronedb/review";
    public Request getWriteDbRv(Object tag , String dr_id, String mem_id, float re_rate, String  re_content, OnResultListener listener) {

        RequestBody body = new FormBody.Builder()
                .add("dr_id",dr_id)
                .add("mem_id",mem_id)
                .add("re_rate", String.valueOf(re_rate))
                .add("re_content", re_content)
                .build();

        String url = String.format(DRONE_DB_RV_WRITE);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        final NetworkResult result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }

    //드로어
    private static final String DRONE_DRAWER = DRONE_HERE + "/hambuger/inquiry";
    public Request getDrawer(Object tag, String mem_id, OnResultListener<DrawerResult> listener) {

        RequestBody body = new FormBody.Builder()
                .add("mem_id", mem_id)
                .build();

        String url = String.format(DRONE_DRAWER);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        final NetworkResult<DrawerResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    DrawerResult data = gson.fromJson(response.body().charStream(), DrawerResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }


    //자기장
    private static final String MAG = "http://spaceweather.rra.go.kr/api/kindex";
    public Request getMag(Object tag,OnResultListener<MagneticResult> listener) {
        String url = String.format(MAG);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        final NetworkResult<MagneticResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        MagneticResult data = gson.fromJson(response.body().charStream(), MagneticResult.class);
                        result.result = data;
                        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                    } else {
                        throw new IOException(response.message());
                    }
                } catch(JsonSyntaxException e){
                    e.getMessage();
                    return;
                }
            }
        });
        return request;
    }

    /** OpenWeather **/
    //일출,바람세기
            private final String appkey = "ca3bccfc3febd319a3343e18dfd8d548";
    private static final String WIND1 = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s";
    public Request getWind(Object tag,String lat,String lon,OnResultListener<WeatherResult> listener) {
        String url = String.format(WIND1, lat, lon, appkey);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Content-type", "application/json")
                .build();

        final NetworkResult<WeatherResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    WeatherResult data = gson.fromJson(response.body().charStream(), WeatherResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });

        return request;
    }
    /** 예전 일출, 바람 받아오는 메서드
    //일출,바람세기
    private static final String WIND = "http://apis.skplanetx.com/gweather/current?version=1&lat=%s&lon=%s&units=&timezone=local";
    public Request getWind(Object tag,String lat,String lon,OnResultListener<WeatherResult> listener) {
        String url = String.format(WIND,lat,lon);
        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("appkey","a8fdf923-2377-3a46-9484-7fc1549e6bbc")
                .build();

        final NetworkResult<WeatherResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    WeatherResult data = gson.fromJson(response.body().charStream(), WeatherResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });

        return request;
    }
     **/




    //장소리뷰썻던거 보기1
    private static final String DRONE_DC1  = DRONE_HERE + "/hambuger/location_re";
    public Request getDc1(Object tag,String mem_id,OnResultListener<Dc1Result> listener) {

        RequestBody body = new FormBody.Builder()
                .add("mem_id",mem_id)
                .build();

        String url = String.format(DRONE_DC1);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        final NetworkResult<Dc1Result> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Dc1Result data = gson.fromJson(response.body().charStream(), Dc1Result.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }

//dc2
    private static final String DRONE_DC2  = DRONE_HERE + "/hambuger/gathering_re";
    public Request getDc2(Object tag,String mem_id,OnResultListener<Dc1Result> listener) {

        RequestBody body = new FormBody.Builder()
                .add("mem_id",mem_id)
                .build();

        String url = String.format(DRONE_DC2);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        final NetworkResult<Dc1Result> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Dc1Result data = gson.fromJson(response.body().charStream(), Dc1Result.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }


//dc3
    private static final String DRONE_DC3  = DRONE_HERE + "/hambuger/dronedb_re";
    public Request getDc3(Object tag,String mem_id,OnResultListener<Dc1Result> listener) {

        RequestBody body = new FormBody.Builder()
                .add("mem_id",mem_id)
                .build();

        String url = String.format(DRONE_DC3);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        final NetworkResult<Dc1Result> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Dc1Result data = gson.fromJson(response.body().charStream(), Dc1Result.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }


    //비행가능불가능
    private static final String DRONE_DRONE  = DRONE_HERE + "/flightmap";
    public Request getFlight(Object tag,String mem_id,String flight_area,String resistance,String magneticfield,String sunrise,String sunset,OnResultListener<DroneFlightResult> listener) {

        RequestBody body = new FormBody.Builder()
                .add("mem_id",mem_id)
                .add("flight_area",flight_area)
                .add("resistance",resistance)
                .add("magneticfield",magneticfield)
                .add("sunrise",sunrise)
                .add("sunset",sunset)
                .build();

        String url = String.format(DRONE_DRONE);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        final NetworkResult<DroneFlightResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    DroneFlightResult data = gson.fromJson(response.body().charStream(), DroneFlightResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }
}
