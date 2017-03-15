package com.example.ksj_notebook.dronehere.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ksj_notebook.dronehere.BaseActivity;
import com.example.ksj_notebook.dronehere.R;

import java.util.regex.Pattern;

public class JoinEmail extends BaseActivity {

    Vibrator vibrator;

    EditText email_text;
    EditText pass1;
    EditText pass2;
    ImageButton btn;

    String em;

    Button lolol;
    EditTextEventHandler editTextEventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_join);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        email_text=(EditText)findViewById(R.id.email);
        pass1=(EditText)findViewById(R.id.pass1);
        btn=(ImageButton)findViewById(R.id.email_join_btn);
        pass2=(EditText)findViewById(R.id.pass2);
        //lolol=(Button)findViewById(R.id.lolol);
        email_text.setHint(R.string.edit_text_email);
        pass1.setHint(R.string.edit_text_pass1);
        pass2.setHint(R.string.edit_text_pass2);
        pass1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pass2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editTextEventHandler = new EditTextEventHandler(email_text, pass1, pass2);
        email_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextEventHandler.text_event();
            }
        });
        pass1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextEventHandler.text_event();
            }
        });
        pass2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextEventHandler.text_event();
            }
        });


        /*lolol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        */

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
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
*/
                em = "" + email_text.getText();
                if (isEmail(em)) {
                    String email;
                    String pw, pw1;
                    email = email_text.getText().toString();
                    pw = pass1.getText().toString();
                    pw1 = pass2.getText().toString();

                    if (email.isEmpty() == true || pw.isEmpty() == true || pw1.isEmpty() == true || email.contains("@") == false) {
                        Toast.makeText(getApplicationContext(), "형식에 맞게 입력해주세요", Toast.LENGTH_SHORT).show();
                        vibrator.vibrate(100);
                    }
                    if (pw.equals(pw1) == false) {
                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                        vibrator.vibrate(100);
                    } else if (email.isEmpty() != true) {
                        Intent intent = new Intent(getApplicationContext(), JoinDronePick.class);
                        intent.putExtra("email", em);
                        intent.putExtra("pass", pass1.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    Toast.makeText(JoinEmail.this, "email이 맞지 않는 형식입니다.", Toast.LENGTH_SHORT).show();
                    vibrator.vibrate(100);
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
