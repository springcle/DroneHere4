package com.santamaria.dronehere.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.santamaria.dronehere.BaseActivity;
import com.santamaria.dronehere.MainActivity;
import com.santamaria.dronehere.MyApplication;
import com.santamaria.dronehere.R;
import com.santamaria.dronehere.data.UserLoginResult;
import com.santamaria.dronehere.manager.NetworkManager;
import com.santamaria.dronehere.manager.PropertyManager;

import java.io.IOException;

import okhttp3.Request;

public class LoginEmail extends BaseActivity {
    Vibrator vibrator;
    EditText email_edit;
    EditText pass_edit;
    ImageButton loginn;
    LinearLayout button3;
    String mem_email;
    String mem_pw;
    EditTextEventHandler editTextEventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.status2));
        }
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        email_edit=(EditText)findViewById(R.id.email_edit);
        pass_edit=(EditText)findViewById(R.id.pass_edit);
        pass_edit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pass_edit.setTransformationMethod(new PasswordTransformationMethod());
        editTextEventHandler = new EditTextEventHandler(email_edit, pass_edit);

        loginn=(ImageButton)findViewById(R.id.loginn);
        button3=(LinearLayout)findViewById(R.id.button3);

        /** 에디트 텍스트 폰트 미적용 **/
        email_edit.setTypeface(Typeface.DEFAULT);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinEmail.class);
                startActivity(intent);
            }
        });
        email_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextEventHandler.text_event();
            }
        });
        pass_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextEventHandler.text_event();
            }
        });

        loginn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mem_email=email_edit.getText().toString();
                mem_pw=pass_edit.getText().toString();
                NetworkManager.getInstance().getLogin(MyApplication.getContext(),mem_email,mem_pw, new NetworkManager.OnResultListener<UserLoginResult>() {

                    @Override
                    public void onSuccess(Request request, UserLoginResult result) {

                           if(result.getMessage().equals("FAIL")){
                               Toast.makeText(LoginEmail.this, "이메일과 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                               vibrator.vibrate(100);
                           }else{
                               PropertyManager.getInstance().setId(result.getResult().getMem_id());
                               Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                               startActivity(intent);
                               finish();
                           }

                    }
                    @Override
                    public void onFail(Request request, IOException exception) {
                    }
                });
            }
        });

    }
}
