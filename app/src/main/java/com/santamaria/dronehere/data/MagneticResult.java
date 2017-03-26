package com.santamaria.dronehere.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ksj_notebook on 2016-05-31.
 */
public class MagneticResult {
    @SerializedName("kindex")
    Magnetic kindex;

    public Magnetic getKindex() {
        return kindex;
    }

    public void setKindex(Magnetic kindex) {
        this.kindex = kindex;
    }
}
