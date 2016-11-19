package com.example.ksj_notebook.dronehere.data;

/**
 * Created by NAKNAK on 2016-11-19.
 */
public class DroneResistanceResult {
    int success;
    String message;
    DroneResistance result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DroneResistance getResult() {
        return result;
    }

    public void setResult(DroneResistance result) {
        this.result = result;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
