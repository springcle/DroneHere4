package com.santamaria.dronehere.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.santamaria.dronehere.BaseActivity;
import com.santamaria.dronehere.MainActivity;
import com.santamaria.dronehere.MyApplication;
import com.santamaria.dronehere.R;
import com.santamaria.dronehere.data.EmailResult;
import com.santamaria.dronehere.data.UserLoginResult;
import com.santamaria.dronehere.manager.NetworkManager;
import com.santamaria.dronehere.manager.PropertyManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Request;


public class StartActivity extends BaseActivity {

    private BackPressCloseHandler backPressCloseHandler;
    String em;
    String pw = "none";
    Button emailbtn;
    Button facebookbtn;
    LinearLayout joinbtn;
    Button naverbtn;
    LinearLayout not_user;
    Handler mHandler = new Handler(Looper.getMainLooper());
    /**
     * 네이버 로그인 모듈
     **/
    private final String OAUTH_CLIENT_ID = "xw392fTm7F61rSmsEBbH";
    private final String OAUTH_CLIENT_SECRET = "W7aylWaK6n";
    private final String OAUTH_CLIENT_NAME = "DroneHere";
    private OAuthLogin mOAuthLoginModule;

    /**
     * 페이스북 로그인 모듈
     **/
    private CallbackManager fbCallbackManager;
    private ProfileTracker fbProfileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_start);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.status2));
        }
        backPressCloseHandler = new BackPressCloseHandler(this);
        /**  네이버 로그인 모듈 **/
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                StartActivity.this
                , OAUTH_CLIENT_ID
                , OAUTH_CLIENT_SECRET
                , OAUTH_CLIENT_NAME
                //,OAUTH_CALLBACK_INTENT
                // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
        );
        naverbtn = (Button) findViewById(R.id.naverbtn);
        emailbtn = (Button) findViewById(R.id.emailbtn);
        facebookbtn = (Button) findViewById(R.id.facebookbtn);
        joinbtn = (LinearLayout) findViewById(R.id.joinbtn);
        not_user = (LinearLayout) findViewById(R.id.member_pass);

        facebookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFacebookButton();
            }
        });

        naverbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Log.w("네이버클릭", "클릭먹고");
                mOAuthLoginModule.startOauthLoginActivity(StartActivity.this, mOAuthLoginHandler);
            }
        });
        emailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginEmail.class);
                startActivity(intent);
            }
        });
        joinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinEmail.class);
                startActivity(intent);
            }
        });

        not_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    public class BackPressCloseHandler {

        private long backKeyPressedTime = 0;
        private Toast toast;

        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
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
            activity.moveTaskToBack(true);
            activity.finish();
            toast.cancel();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }

        public void showGuide() {
            toast = Toast.makeText(activity, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * OAuthLoginHandler를 startOAuthLoginActivity() 메서드 호출 시 파라미터로 전달하거나 OAuthLoginButton
     * 객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다.
     */
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                //  Log.w("네이버핸들러", "성공했고");
                final String accessToken = mOAuthLoginModule.getAccessToken(StartActivity.this);
                String refreshToken = mOAuthLoginModule.getRefreshToken(StartActivity.this);
                long expiresAt = mOAuthLoginModule.getExpiresAt(StartActivity.this);
                String tokenType = mOAuthLoginModule.getTokenType(StartActivity.this);
                /*mOauthAT.setText(accessToken);
                mOauthRT.setText(refreshToken);
                mOauthExpires.setText(String.valueOf(expiresAt));
                mOauthTokenType.setText(tokenType);
                mOAuthState.setText(mOAuthLoginModule.getState(StartActivity.this).toString());*/
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response = mOAuthLoginModule.requestApi(StartActivity.this, accessToken, "https://openapi.naver.com/v1/nid/me");
                        try {
                            JSONObject json = new JSONObject(response);
                            // response 객체에서 원하는 값 얻어오기
                            em = json.getJSONObject("response").getString("email");
                            //    Log.w("네이버이메일 확인", em);
                            // 액티비티 이동 등 원하는 함수 호출

                            /** 이메일 중복검사 **/
                            NetworkManager.getInstance().getEmail(MyApplication.getContext(), em, new NetworkManager.OnResultListener<EmailResult>() {
                                @Override
                                public void onSuccess(Request request, EmailResult result) {
                                    //         Log.w("확인", String.valueOf(result.getResult()));
                                    if (result.getResult() == 1) { /** 최초 로그인시 드론, 닉네임 설정 액티비티로 **/
                                        Intent intent = new Intent(getApplicationContext(), JoinDronePick.class);
                                        intent.putExtra("email", em);
                                        intent.putExtra("pass", pw);
                                        startActivity(intent);
                                        finish();
                                    } else {  /** 이미 가입되었으면, 바로 메인액티비티로 **/
                                        goLoginActivity();
                                    }
                                }

                                @Override
                                public void onFail(Request request, IOException exception) {
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                /** 로그인정보 서버에 넘겨주고 비교 후에 메인액티비티로 갈지 회원가입 루틴 실행할지 결정 **/

                /** 서버에 아이디 있으면 그 아이디로 메인액티비티로 **/
                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                /** 서버에 아이디 없으면(최초 로그인 시) **/
                //Intent intent = new Intent(getApplicationContext(), JoinDronePick.class);
                /*intent.putExtra("email",em); em에 로그인정보 회원이메일주소 넣어주기
                pw = "none";
                intent.putExtra("pass",pw);*/
                //startActivity(intent);
                //finish();
            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(StartActivity.this).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(StartActivity.this);
            }
        }

        ;
    };

    /**
     * 자동로그인 기능
     **/
    private void goLoginActivity() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //  Log.w("고로그인 이메일",em);
                NetworkManager.getInstance().getLogin(MyApplication.getContext(), em, pw, new NetworkManager.OnResultListener<UserLoginResult>() {

                    @Override
                    public void onSuccess(Request request, UserLoginResult result) {
                        PropertyManager.getInstance().setId(result.getResult().getMem_id());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFail(Request request, IOException exception) {
                        Toast.makeText(getApplication(), "이메일과 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                });


//                if (PropertyManager.getInstance().getId()!=null){
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }else{
//                    startActivity(new Intent(SplashActivity.this, StartActivity.class));
                finish();
                // }


            }
        }, 2000);

    }

    private void LoginFacebookButton() {
        fbCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                try {
                                    String id = object.getString("id");
                                    em = "facebook/" + id;
                                    pw = "facebook/" + object.getString("name");
                                    NetworkManager.getInstance().getEmail(MyApplication.getContext(), em, new NetworkManager.OnResultListener<EmailResult>() {
                                        @Override
                                        public void onSuccess(Request request, EmailResult result) {
                                            //         Log.w("확인", String.valueOf(result.getResult()));
                                            if (result.getResult() == 1) { /** 최초 로그인시 드론, 닉네임 설정 액티비티로 **/
                                                Intent intent = new Intent(getApplicationContext(), JoinDronePick.class);
                                                intent.putExtra("email", em);
                                                intent.putExtra("pass", pw);
                                                startActivity(intent);
                                                finish();
                                            } else {  /** 이미 가입되었으면, 바로 메인액티비티로 **/
                                                goLoginActivity();
                                            }
                                        }
                                        @Override
                                        public void onFail(Request request, IOException exception) {
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "페이스북 로그인 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /** 페이스북 **/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
