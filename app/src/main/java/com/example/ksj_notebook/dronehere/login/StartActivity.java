package com.example.ksj_notebook.dronehere.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.ksj_notebook.dronehere.BaseActivity;
import com.example.ksj_notebook.dronehere.MainActivity;
import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.EmailResult;
import com.example.ksj_notebook.dronehere.data.LoginResult;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;
import com.example.ksj_notebook.dronehere.manager.PropertyManager;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Request;


public class StartActivity extends BaseActivity {

    private BackPressCloseHandler backPressCloseHandler;
    String em;
    String pw = "none";
    Button emailbtn;
    Button facebookbtn;
    Button joinbtn;
    Button naverbtn;
    Button not_user;
    Handler mHandler = new Handler(Looper.getMainLooper());
    /**  네이버 로그인 모듈 **/
    private final String OAUTH_CLIENT_ID = "xw392fTm7F61rSmsEBbH";
    private final String OAUTH_CLIENT_SECRET = "W7aylWaK6n";
    private final String OAUTH_CLIENT_NAME = "DroneHere";
    private OAuthLogin mOAuthLoginModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        int id = getResources().getIdentifier("config_enableTranslucentDecor", "bool", "android");
        if (id != 0 && getResources().getBoolean(id)) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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
        naverbtn=(Button)findViewById(R.id.naverbtn);
        emailbtn=(Button)findViewById(R.id.emailbtn);
        facebookbtn=(Button)findViewById(R.id.facebookbtn);
        joinbtn=(Button)findViewById(R.id.joinbtn);
        not_user = (Button) findViewById(R.id.member_pass);

        naverbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("네이버클릭", "클릭먹고");
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
        facebookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StartActivity.this, "미구현 입니다.", Toast.LENGTH_SHORT).show();
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
            android.os.Process.killProcess(android.os.Process.myPid() );
            System.exit(0);
        }
        public void showGuide() {
            toast = Toast.makeText(activity, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    /**
     * OAuthLoginHandler를 startOAuthLoginActivity() 메서드 호출 시 파라미터로 전달하거나 OAuthLoginButton
     객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다.
     */
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                Log.w("네이버핸들러", "성공했고");
                final String accessToken = mOAuthLoginModule.getAccessToken(StartActivity.this);
                String refreshToken = mOAuthLoginModule.getRefreshToken(StartActivity.this);
                long expiresAt = mOAuthLoginModule.getExpiresAt(StartActivity.this);
                String tokenType = mOAuthLoginModule.getTokenType(StartActivity.this);
                /*mOauthAT.setText(accessToken);
                mOauthRT.setText(refreshToken);
                mOauthExpires.setText(String.valueOf(expiresAt));
                mOauthTokenType.setText(tokenType);
                mOAuthState.setText(mOAuthLoginModule.getState(StartActivity.this).toString());*/
                new Thread( new Runnable( )
                {
                    @Override
                    public void run( )
                    {
                        String response = mOAuthLoginModule.requestApi(StartActivity.this, accessToken, "https://openapi.naver.com/v1/nid/me" );
                        try
                        {
                            JSONObject json = new JSONObject( response );
                            // response 객체에서 원하는 값 얻어오기
                            em = json.getJSONObject("response").getString("email");
                            Log.w("네이버이메일 확인", em);
                            // 액티비티 이동 등 원하는 함수 호출
                            /** 이메일 중복검사 **/
                            NetworkManager.getInstance().getEmail(MyApplication.getContext(), em, new NetworkManager.OnResultListener<EmailResult>() {
                                @Override
                                public void onSuccess(Request request, EmailResult result) {
                                    Log.w("확인", String.valueOf(result.getResult()));
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
                                    Log.w("서버실패", "실패");
                                }
                            });
                        } catch ( JSONException e )
                        {
                            e.printStackTrace( );
                        }
                    }
                } ).start();

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
                Log.w("완전실패","완전");
                String errorCode = mOAuthLoginModule.getLastErrorCode(StartActivity.this).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(StartActivity.this);
                Toast.makeText(StartActivity.this, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        };
    };
    /** 자동로그인 기능 **/
    private void goLoginActivity() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.w("고로그인 이메일",em);
                NetworkManager.getInstance().getLogin(MyApplication.getContext(),em,pw, new NetworkManager.OnResultListener<LoginResult>() {

                    @Override
                    public void onSuccess(Request request, LoginResult result) {
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

}
