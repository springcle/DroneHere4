package com.santamaria.dronehere.data;

/**
 * Created by ksj_notebook on 2016-06-10.
 */
public class DroneFlightResult {
    int success;
    String message;
    int[] result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int[] getResult() {
        return result;
    }

    public void setResult(int[] result) {
        this.result = result;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
