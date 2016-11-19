package com.example.ksj_notebook.dronehere.data;

/**
 * Created by ksj_notebook on 2016-05-18.
 */
public class Review {
    String _id;
    String re_memname;
    String re_regdate;
    double re_rate;
    String re_content;
    String re_connection;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRe_connection() {
        return re_connection;
    }

    public void setRe_connection(String re_connection) {
        this.re_connection = re_connection;
    }

    public String getRe_content() {
        return re_content;
    }

    public void setRe_content(String re_content) {
        this.re_content = re_content;
    }

    public String getRe_memname() {
        return re_memname;
    }

    public void setRe_memname(String re_memname) {
        this.re_memname = re_memname;
    }

    public double getRe_rate() {
        return re_rate;
    }

    public void setRe_rate(double re_rate) {
        this.re_rate = re_rate;
    }

    public String getRe_regdate() {
        return re_regdate;
    }

    public void setRe_regdate(String re_regdate) {
        this.re_regdate = re_regdate;
    }
}
