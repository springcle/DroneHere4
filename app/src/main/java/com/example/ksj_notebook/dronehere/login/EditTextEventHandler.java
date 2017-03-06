package com.example.ksj_notebook.dronehere.login;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.ksj_notebook.dronehere.R;

import java.util.regex.Pattern;

/**
 * Created by NAKNAK on 2017-03-06.
 */
public class EditTextEventHandler{

    EditText editText1, editText2, editText3;

    public EditTextEventHandler(EditText editText1, EditText editText2){
        this.editText1 = editText1;
        this.editText2 = editText2;
    }
    public EditTextEventHandler(EditText editText1, EditText editText2, EditText editText3){
        this.editText1 = editText1;
        this.editText2 = editText2;
        this.editText3 = editText3;
    }

    public void text_event(){
        if(editText1 != null) {
            editText1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    String str1 = editText1.getText().toString();
                    if (isEmail(str1) == true) {
                        editText1.setBackgroundResource(R.drawable.b_04);
                    } else editText1.setBackgroundResource(R.drawable.b_04_1);


                    String str3;
                    if (editText3 != null) {
                        str3 = editText3.getText().toString();
                        if (str3.isEmpty() == true) {
                            editText3.setBackgroundResource(R.drawable.b_04_1);
                        } else editText3.setBackgroundResource(R.drawable.b_04);
                    }
                }
            });
        }
        if(editText2 != null) {
            editText2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String str2 = editText2.getText().toString();
                    if (str2.isEmpty() == true) {
                        editText2.setBackgroundResource(R.drawable.b_04_1);
                    } else editText2.setBackgroundResource(R.drawable.b_04);
                }
            });
        }
        if(editText3 != null){
            editText3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String str3 = editText3.getText().toString();
                    if (str3.isEmpty() == true) {
                        editText3.setBackgroundResource(R.drawable.b_04_1);
                    } else editText3.setBackgroundResource(R.drawable.b_04);
                }
            });
        }
    }
    public static boolean isEmail(String email) {
        if (email==null) return false;
        boolean b = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", email.trim());
        return b;
    }
}
