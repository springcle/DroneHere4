package com.example.ksj_notebook.dronehere.data;

import java.util.List;

/**
 * Created by ksj_notebook on 2016-05-29.
 */
public class DroneRecommendResult {
    int succes;
    String message;
    List<DroneDB> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DroneDB> getResult() {
        return result;
    }

    public void setResult(List<DroneDB> result) {
        this.result = result;
    }

    public int getSucces() {
        return succes;
    }

    public void setSucces(int succes) {
        this.succes = succes;
    }
}
