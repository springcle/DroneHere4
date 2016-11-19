package com.example.ksj_notebook.dronehere.data;

/**
 * Created by ksj_notebook on 2016-05-25.
 */
public class LocaContentResult {
    int succes;
    String message;
    Locatio result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Locatio getResult() {
        return result;
    }

    public void setResult(Locatio result) {
        this.result = result;
    }

    public int getSucces() {
        return succes;
    }

    public void setSucces(int succes) {
        this.succes = succes;
    }
}

