package com.santamaria.dronehere.manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.santamaria.dronehere.MyApplication;

/**
 * Created by ksj_notebook on 2016-06-09.
 */
public class PropertyManager {
    private static PropertyManager instance;
    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PropertyManager() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        mEditor = mPrefs.edit();
    }

    private static final String MEM_ID = "mem_id";
    public void setId(String mem_id) {
        mEditor.putString(MEM_ID, mem_id);
        mEditor.commit();
    }
    public String getId() {
        return mPrefs.getString(MEM_ID,"");
    }




}