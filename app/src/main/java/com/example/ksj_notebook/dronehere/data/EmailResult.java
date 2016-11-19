package com.example.ksj_notebook.dronehere.data;

/**
 * Created by ksj_notebook on 2016-06-10.
 */
public class EmailResult {
    int succes;
    String message;
    int result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getSucces() {
        return succes;
    }

    public void setSucces(int succes) {
        this.succes = succes;
    }
}
