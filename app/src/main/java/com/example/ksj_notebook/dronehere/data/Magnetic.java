package com.example.ksj_notebook.dronehere.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ksj_notebook on 2016-05-31.
 */
public class Magnetic {
    @SerializedName("currentK")
    int currentK;


    public int getCurrentK() {
        return currentK;
    }

    public void setCurrentK(int currentK) {
        this.currentK = currentK;
    }
}
