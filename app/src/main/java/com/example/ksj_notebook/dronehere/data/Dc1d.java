package com.example.ksj_notebook.dronehere.data;

import java.util.Date;

/**
 * Created by ksj_notebook on 2016-06-15.
 */
public class Dc1d {
    String loca_id;
    String loca_name;
    Date re_regdate;
    double re_rate;
    String re_content;

    String gathe_id;
    String gathe_name;
    String dr_id;
    String dr_name;


    public String getDr_id() {
        return dr_id;
    }

    public void setDr_id(String dr_id) {
        this.dr_id = dr_id;
    }

    public String getDr_name() {
        return dr_name;
    }

    public void setDr_name(String dr_name) {
        this.dr_name = dr_name;
    }

    public String getLoca_id() {
        return loca_id;
    }

    public void setLoca_id(String loca_id) {
        this.loca_id = loca_id;
    }

    public String getLoca_name() {
        return loca_name;
    }

    public void setLoca_name(String loca_name) {
        this.loca_name = loca_name;
    }

    public String getRe_content() {
        return re_content;
    }

    public void setRe_content(String re_content) {
        this.re_content = re_content;
    }

    public double getRe_rate() {
        return re_rate;
    }

    public void setRe_rate(Double re_rate) {
        this.re_rate = re_rate;
    }

    public Date getRe_regdate() {
        return re_regdate;
    }

    public void setRe_regdate(Date re_regdate) {
        this.re_regdate = re_regdate;
    }

    public String getGathe_id() {
        return gathe_id;
    }

    public void setGathe_id(String gathe_id) {
        this.gathe_id = gathe_id;
    }

    public String getGathe_name() {
        return gathe_name;
    }

    public void setGathe_name(String gathe_name) {
        this.gathe_name = gathe_name;
    }
}

