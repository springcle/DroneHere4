package com.example.ksj_notebook.dronehere.data;

import java.util.List;

/**
 * Created by ksj_notebook on 2016-06-15.
 */
public class Dc1Result {
    int success;
    String message;
    List<Dc1d> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Dc1d> getResult() {
        return result;
    }

    public void setResult(List<Dc1d> result) {
        this.result = result;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
