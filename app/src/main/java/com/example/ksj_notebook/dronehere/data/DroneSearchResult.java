package com.example.ksj_notebook.dronehere.data;

import java.util.ArrayList;

/**
 * Created by ksj_notebook on 2016-05-29.
 */
public class DroneSearchResult {
    int succes;
    String message;
    ArrayList<DroneDB> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<DroneDB> getResult() {
        return result;
    }

    public void setResult(ArrayList<DroneDB> result) {
        this.result = result;
    }

    public int getSucces() {
        return succes;
    }

    public void setSucces(int succes) {
        this.succes = succes;
    }
}
