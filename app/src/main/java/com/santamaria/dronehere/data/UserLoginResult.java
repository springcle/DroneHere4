package com.santamaria.dronehere.data;

/**
 * Created by ksj_notebook on 2016-06-10.
 */
public class UserLoginResult {
    int success;
    String message;
    Member result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Member getResult() {
        return result;
    }

    public void setResult(Member result) {
        this.result = result;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
