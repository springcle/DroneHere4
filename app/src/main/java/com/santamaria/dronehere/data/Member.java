package com.santamaria.dronehere.data;

import java.util.List;

/**
 * Created by ksj_notebook on 2016-05-18.
 */
public class Member {
    /** 유저 아이디, 유저 비번, 유저 이름, 유저 이메일, 등록한 드론 사진, 등록한 드론리스트**/
    String mem_id;
    String mem_pw;
    String mem_name;
    String mem_email;
    String dr_photo;
    List<DroneDB> mem_drone;


    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public List<DroneDB> getMem_drone() {
        return mem_drone;
    }

    public void setMem_drone(List<DroneDB> mem_drone) {
        this.mem_drone = mem_drone;
    }

    public String getMem_email() {
        return mem_email;
    }

    public String getDr_photo() {
        return dr_photo;
    }

    public void setDr_photo(String dr_photo) {
        this.dr_photo = dr_photo;
    }

    public void setMem_email(String mem_email) {
        this.mem_email = mem_email;
    }

    public String getMem_name() {
        return mem_name;
    }

    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }

    public String getMem_pw() {
        return mem_pw;
    }

    public void setMem_pw(String mem_pw) {
        this.mem_pw = mem_pw;
    }
}
