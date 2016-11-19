package com.example.ksj_notebook.dronehere;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //startActivity(new Intent(getApplicationContext(), MainActivity.class)); // 앱 실행 후 바로 로그인없이 메인액티비티로
       // finish();
        goLoginActivity();
    }
    private void goLoginActivity() {
            mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                /** 최초 로그인 시(디폴트값이 "" 라서) **/
               /*if (PropertyManager.getInstance().getId() == ""){
                    NetworkManager.getInstance().getLogin(MyApplication.getContext(), "a@a.com", "a", new NetworkManager.OnResultListener<LoginResult>() {

                        @Override
                        public void onSuccess(Request request, LoginResult result) {

                            PropertyManager.getInstance().setId(result.getResult().getMem_id());
                            Log.w("화악인", PropertyManager.getInstance().getId());
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        public void onFail(Request request, IOException exception) {
                            Toast.makeText(getApplication(), "이메일과 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
*/

//                if (PropertyManager.getInstance().getId()!=null){
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }else{
//                    startActivity(new Intent(SplashActivity.this, StartActivity.class));
                //   finish();
                // }
//            }else{ /** 두 번째 로그인부터 **/
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

  //          }
       }, 2000);




    }
}
