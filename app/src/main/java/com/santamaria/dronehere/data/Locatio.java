package com.santamaria.dronehere.data;

import java.util.List;

/**
 * Created by ksj_notebook on 2016-05-18.
 */
public class Locatio {
    int loca_num;
    double loca_latitude;
    double loca_longitude;
    String _id;
    String loca_name;
    String loca_address;
    String loca_content;
    List<String> loca_info;
    List<String> loca_photo;

    List<Gathering> gathe_list;
    List<Gathering> gathe_list_past;
    List<Review> loca_review;
    double loca_rate;

    int gathe_list_num;
    int loca_re_num;
    int gathe_num;
    int gathe_past_num;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<Gathering> getGathe_list() {
        return gathe_list;
    }

    public void setGathe_list(List<Gathering> gathe_list) {
        this.gathe_list = gathe_list;
    }

    public List<Gathering> getGathe_list_past() {
        return gathe_list_past;
    }

    public void setGathe_list_past(List<Gathering> gathe_list_past) {
        this.gathe_list_past = gathe_list_past;
    }

    public String getLoca_address() {
        return loca_address;
    }

    public void setLoca_address(String loca_address) {
        this.loca_address = loca_address;
    }

    public String getLoca_content() {
        return loca_content;
    }

    public void setLoca_content(String loca_content) {
        this.loca_content = loca_content;
    }

    public List<String> getLoca_info() {
        return loca_info;
    }

    public void setLoca_info(List<String> loca_info) {
        this.loca_info = loca_info;
    }

    public double getLoca_latitude() {
        return loca_latitude;
    }

    public void setLoca_latitude(double loca_latitude) {
        this.loca_latitude = loca_latitude;
    }

    public double getLoca_longitude() {
        return loca_longitude;
    }

    public void setLoca_longitude(double loca_longitude) {
        this.loca_longitude = loca_longitude;
    }

    public String getLoca_name() {
        return loca_name;
    }

    public void setLoca_name(String loca_name) {
        this.loca_name = loca_name;
    }

    public int getLoca_num() {
        return loca_num;
    }

    public void setLoca_num(int loca_num) {
        this.loca_num = loca_num;
    }

    public List<String> getLoca_photo() {
        return loca_photo;
    }

    public void setLoca_photo(List<String> loca_photo) {
        this.loca_photo = loca_photo;
    }

    public double getLoca_rate() {
        return loca_rate;
    }

    public void setLoca_rate(double loca_rate) {
        this.loca_rate = loca_rate;
    }

    public List<Review> getLoca_review() {
        return loca_review;
    }

    public void setLoca_review(List<Review> loca_review) {
        this.loca_review = loca_review;
    }

    public int getGathe_list_num() {
        return gathe_list_num;
    }

    public void setGathe_list_num(int gathe_list_num) {
        this.gathe_list_num = gathe_list_num;
    }

    public int getGathe_num() {
        return gathe_num;
    }

    public void setGathe_num(int gathe_num) {
        this.gathe_num = gathe_num;
    }

    public int getGathe_past_num() {
        return gathe_past_num;
    }

    public void setGathe_past_num(int gathe_past_num) {
        this.gathe_past_num = gathe_past_num;
    }

    public int getLoca_re_num() {
        return loca_re_num;
    }

    public void setLoca_re_num(int loca_re_num) {
        this.loca_re_num = loca_re_num;
    }
}
