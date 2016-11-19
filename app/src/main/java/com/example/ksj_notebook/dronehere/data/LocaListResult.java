package com.example.ksj_notebook.dronehere.data;

import java.util.List;

/**
 * Created by ksj_notebook on 2016-05-25.
 */
public class LocaListResult {
    int succes;
    String message;
    List<Locatio> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Locatio> getResult() {
        return result;
    }

    public void setResult(List<Locatio> result) {
        this.result = result;
    }

    public int getSucces() {
        return succes;
    }

    public void setSucces(int succes) {
        this.succes = succes;
    }
}
