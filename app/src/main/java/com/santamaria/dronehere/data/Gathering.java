package com.santamaria.dronehere.data;

import java.util.List;

/**
 * Created by ksj_notebook on 2016-05-18.
 */
public class Gathering {
    String _id;
    String gathe_name;
    String gathe_memname;
    String gathe_regdate;
    String gathe_location;
    String gathe_content;
    List<String> gathe_photo;
    List<Review> gathe_review;
    int gathe_past_re_num;
    int gathe_re_num;
    int dday;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getDday() {
        return dday;
    }

    public void setDday(int dday) {
        this.dday = dday;
    }

    public String getGathe_content() {
        return gathe_content;
    }

    public void setGathe_content(String gathe_content) {
        this.gathe_content = gathe_content;
    }

    public String getGathe_location() {
        return gathe_location;
    }

    public void setGathe_location(String gathe_location) {
        this.gathe_location = gathe_location;
    }

    public String getGathe_memname() {
        return gathe_memname;
    }

    public void setGathe_memname(String gathe_memname) {
        this.gathe_memname = gathe_memname;
    }

    public String getGathe_name() {
        return gathe_name;
    }

    public void setGathe_name(String gathe_name) {
        this.gathe_name = gathe_name;
    }

    public List<String> getGathe_photo() {
        return gathe_photo;
    }

    public void setGathe_photo(List<String> gathe_photo) {
        this.gathe_photo = gathe_photo;
    }

    public String getGathe_regdate() {
        return gathe_regdate;
    }

    public void setGathe_regdate(String gathe_regdate) {
        this.gathe_regdate = gathe_regdate;
    }

    public List<Review> getGathe_review() {
        return gathe_review;
    }

    public void setGathe_review(List<Review> gathe_review) {
        this.gathe_review = gathe_review;
    }

    public int getGathe_past_re_num() {
        return gathe_past_re_num;
    }

    public void setGathe_past_re_num(int gathe_past_re_num) {
        this.gathe_past_re_num = gathe_past_re_num;
    }

    public int getGathe_re_num() {
        return gathe_re_num;
    }

    public void setGathe_re_num(int gathe_re_num) {
        this.gathe_re_num = gathe_re_num;
    }
}

