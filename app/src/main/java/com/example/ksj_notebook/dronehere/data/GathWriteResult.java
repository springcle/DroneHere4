package com.example.ksj_notebook.dronehere.data;

import java.util.List;

/**
 * Created by ksj_notebook on 2016-06-07.
 */
public class GathWriteResult {
    int success;
    String message;
    List<Gathering> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Gathering> getResult() {
        return result;
    }

    public void setResult(List<Gathering> result) {
        this.result = result;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
