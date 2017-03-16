package com.example.ksj_notebook.dronehere;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by NAKNAK on 2017-03-15.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

}
