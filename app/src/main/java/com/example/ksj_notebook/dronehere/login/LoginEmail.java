package com.example.ksj_notebook.dronehere.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ksj_notebook.dronehere.BaseActivity;
import com.example.ksj_notebook.dronehere.MainActivity;
import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.LoginResult;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;
import com.example.ksj_notebook.dronehere.manager.PropertyManager;

import java.io.IOException;

import okhttp3.Request;

public class LoginEmail extends BaseActivity {
    Vibrator vibrator;
    EditText email_edit;
    EditText pass_edit;
    ImageButton loginn;
    Button button3;
    String mem_email;
    String mem_pw;
    EditTextEventHandler editTextEventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        email_edit=(EditText)findViewById(R.id.email_edit);
        pass_edit=(EditText)findViewById(R.id.pass_edit);

        editTextEventHandler = new EditTextEventHandler(email_edit, pass_edit);

        loginn=(ImageButton)findViewById(R.id.loginn);
        button3=(Button)findViewById(R.id.button3);

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
                NetworkManager.getInstance().getLogin(MyApplication.getContext(),mem_email,mem_pw, new NetworkManager.OnResultListener<LoginResult>() {

                    @Override
                    public void onSuccess(Request request, LoginResult result) {

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
