package com.santamaria.dronehere.data;

import java.util.List;

/**
 * Created by ksj_notebook on 2016-06-05.
 */
public class AddReviewResult {
    int success;
    String message;
    List<Review> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Review> getResult() {
        return result;
    }

    public void setResult(List<Review> result) {
        this.result = result;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
