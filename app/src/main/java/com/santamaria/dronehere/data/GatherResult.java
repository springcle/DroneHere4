package com.santamaria.dronehere.data;

/**
 * Created by ksj_notebook on 2016-05-27.
 */
public class GatherResult {
    int succes;
    String message;
    Gathering result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Gathering getResult() {
        return result;
    }

    public void setResult(Gathering result) {
        this.result = result;
    }

    public int getSucces() {
        return succes;
    }

    public void setSucces(int succes) {
        this.succes = succes;
    }
}
