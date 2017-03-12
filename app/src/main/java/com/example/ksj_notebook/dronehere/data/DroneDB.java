package com.example.ksj_notebook.dronehere.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ksj_notebook on 2016-05-18.
 */
public class DroneDB {
    int[] dr_array;

/*    public ArrayList<String> dr_image = new ArrayList<>(); */
    String _id;
    String dr_name;
    String dr_use;
    String dr_manufacture;
    ArrayList<String> dr_photoArr = new ArrayList<>();
    String dr_photo; // 드론백과의 리스트에서 드론 아이템의 이미지 부분.
    double dr_rate;
    String dr_price;
    String dr_source;
    String dr_spec;
    double dr_resistance;
    String dr_temp;
    String dr_impossible;
    String dr_reference;
    double dr_megapixels;
    double dr_receiptrange;
    double dr_flighttime;
    double dr_weight;

    double dr_pk;
    int dr_re_num;
    double dr_mail;

    List<Review> dr_review;



/*    public ArrayList<String> getDr_image() {
        return dr_image;
    }

    public void setDr_image(ArrayList<String> dr_image) {
        this.dr_image = dr_image;
    }*/
    public double getDr_flighttime() {
        return dr_flighttime;
    }

    public void setDr_flighttime(double dr_flighttime) {
        this.dr_flighttime = dr_flighttime;
    }

    public double getDr_megapixels() {
        return dr_megapixels;
    }

    public void setDr_megapixels(double dr_megapixels) {
        this.dr_megapixels = dr_megapixels;
    }

    public double getDr_receiptrange() {
        return dr_receiptrange;
    }

    public void setDr_receiptrange(double dr_receiptrange) {
        this.dr_receiptrange = dr_receiptrange;
    }

    public double getDr_weight() {
        return dr_weight;
    }

    public void setDr_weight(double dr_weight) {
        this.dr_weight = dr_weight;
    }

    public double getDr_mail() {
        return dr_mail;
    }

    public void setDr_mail(double dr_mail) {
        this.dr_mail = dr_mail;
    }

    public int[] getDr_array() {
        return dr_array;
    }

    public void setDr_array(int[] dr_array) {
        this.dr_array = dr_array;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDr_impossible() {
        return dr_impossible;
    }

    public void setDr_impossible(String dr_impossible) {
        this.dr_impossible = dr_impossible;
    }

    public String getDr_manufacture() {
        return dr_manufacture;
    }

    public void setDr_manufacture(String dr_manufacture) {
        this.dr_manufacture = dr_manufacture;
    }

    public String getDr_name() {
        return dr_name;
    }

    public void setDr_name(String dr_name) {
        this.dr_name = dr_name;
    }

    public String getDr_photo() {
        return dr_photo;
    }

    public void setDr_photo(String dr_photo) {
        this.dr_photo = dr_photo;
    }

    public ArrayList<String> getDr_photoArr() {
        return dr_photoArr;
    }

    public void setDr_photoArr(ArrayList<String> dr_photoArr) {
        this.dr_photoArr = dr_photoArr;
    }

    public double getDr_pk() {
        return dr_pk;
    }

    public void setDr_pk(double dr_pk) {
        this.dr_pk = dr_pk;
    }

    public String getDr_price() {
        return dr_price;
    }

    public void setDr_price(String dr_price) {
        this.dr_price = dr_price;
    }

    public double getDr_rate() {
        return dr_rate;
    }

    public void setDr_rate(double dr_rate) {
        this.dr_rate = dr_rate;
    }

    public String getDr_reference() {
        return dr_reference;
    }

    public void setDr_reference(String dr_reference) {
        this.dr_reference = dr_reference;
    }


    public double getDr_resistance() {
        return dr_resistance;
    }

    public void setDr_resistance(double dr_resistance) {
        this.dr_resistance = dr_resistance;
    }

    public List<Review> getDr_review() {
        return dr_review;
    }

    public void setDr_review(List<Review> dr_review) {
        this.dr_review = dr_review;
    }

    public String getDr_source() {
        return dr_source;
    }

    public void setDr_source(String dr_source) {
        this.dr_source = dr_source;
    }

    public String getDr_spec() {
        return dr_spec;
    }

    public void setDr_spec(String dr_spec) {
        this.dr_spec = dr_spec;
    }

    public String getDr_temp() {
        return dr_temp;
    }

    public void setDr_temp(String dr_temp) {
        this.dr_temp = dr_temp;
    }

    public String getDr_use() {
        return dr_use;
    }

    public void setDr_use(String dr_use) {
        this.dr_use = dr_use;
    }

    public int getDr_re_num() {
        return dr_re_num;
    }

    public void setDr_re_num(int dr_re_num) {
        this.dr_re_num = dr_re_num;
    }
}
