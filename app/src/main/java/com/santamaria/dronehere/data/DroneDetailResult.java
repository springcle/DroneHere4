package com.santamaria.dronehere.data;

/**
 * Created by ksj_notebook on 2016-05-29.
 */
public class DroneDetailResult {
    int succes;
    String message;
    DroneDB result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DroneDB getResult() {
        return result;
    }

    public void setResult(DroneDB result) {
        this.result = result;
    }

    public int getSucces() {
        return succes;
    }

    public void setSucces(int succes) {
        this.succes = succes;
    }
}
