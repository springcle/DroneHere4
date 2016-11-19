package com.example.ksj_notebook.dronehere.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.EmailResult;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.Request;

public class JoinEmail extends AppCompatActivity {

    EditText email;
    EditText pass1;
    EditText pass2;
    Button btn;

    String em;

    Button lolol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_join);

        email=(EditText)findViewById(R.id.email);
        pass1=(EditText)findViewById(R.id.pass);
        btn=(Button)findViewById(R.id.gopick);
        pass2=(EditText)findViewById(R.id.pass2);
        lolol=(Button)findViewById(R.id.lolol);

        email.setHint(R.string.edit_text_email);
        pass1.setHint(R.string.edit_text_pass1);
        pass2.setHint(R.string.edit_text_pass2);


        lolol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                em= ""+ email.getText();
               if(isEmail(em)){

                   if(pass1.getText().toString().equals(pass2.getText().toString())){

                       NetworkManager.getInstance().getEmail(MyApplication.getContext(),em, new NetworkManager.OnResultListener<EmailResult>() {


                           @Override
                           public void onSuccess(Request request, EmailResult result) {

                               if(result.getResult()==1){
                                   Intent intent = new Intent(getApplicationContext(), JoinDronePick.class);
                                   intent.putExtra("email",em);
                                   intent.putExtra("pass",pass1.getText().toString());
                                   startActivity(intent);
                                   finish();
                               }else{
                                   Toast.makeText(JoinEmail.this, "이미 등록되어 있는 메일입니다.", Toast.LENGTH_SHORT).show();
                               }

                           }

                           @Override
                           public void onFail(Request request, IOException exception) {

                           }
                       });



                   }else{
                       Toast.makeText(JoinEmail.this, "비밀번호가 서로 맞지않습니다.", Toast.LENGTH_SHORT).show();
                   }


               }else{
                   Toast.makeText(JoinEmail.this, "email이 맞지 않는 형식입니다.", Toast.LENGTH_SHORT).show();
               }








            }
        });
    }
    public static boolean isEmail(String email) {
        if (email==null) return false;
        boolean b = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+",email.trim());
        return b;
    }
}
