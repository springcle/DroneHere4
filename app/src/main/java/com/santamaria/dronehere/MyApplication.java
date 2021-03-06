package com.santamaria.dronehere;

import android.app.Application;
import android.content.Context;

import com.tsengvn.typekit.Typekit;

/**
 * Created by ksj_notebook on 2016-05-18.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "NanumSquareR.otf"))
                .addBold(Typekit.createFromAsset(this, "NanumSquareB.otf"));
    }

    public static Context getContext() {
        return context;
    }
}
