package com.example.ksj_notebook.dronehere;

import android.app.Application;
import android.content.Context;

/**
 * Created by ksj_notebook on 2016-05-18.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
