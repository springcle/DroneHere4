package com.example.ksj_notebook.dronehere.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ksj_notebook.dronehere.MainActivity;
import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.LoginResult;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;
import com.example.ksj_notebook.dronehere.manager.PropertyManager;

import java.io.IOException;

import okhttp3.Request;

public class LoginEmail extends AppCompatActivity {

    EditText email_edit;
    EditText pass_edit;
    Button loginn;

    Button button3;

    String mem_email;
    String mem_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        email_edit=(EditText)findViewById(R.id.email_edit);
        pass_edit=(EditText)findViewById(R.id.pass_edit);

        loginn=(Button)findViewById(R.id.loginn);
        button3=(Button)findViewById(R.id.button3);



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


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinEmail.class);
                startActivity(intent);
            }
        });

    }
}
