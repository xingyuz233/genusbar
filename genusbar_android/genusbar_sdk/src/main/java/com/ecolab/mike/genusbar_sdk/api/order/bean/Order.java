package com.ecolab.mike.genusbar_sdk.api.order.bean;

import java.io.Serializable;
import java.time.LocalDate;

public class Order implements Serializable {

    private int id;
    private String name;
    private String email;
    private int resStatus;
    private String remark;
    private String resTime;
    private String number;

    public Order(int id, String name, String email, int resStatus, String remark, String resTime, String number) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.resStatus = resStatus;
        this.remark = remark;
        this.resTime = resTime;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public int getRestatus() {
        return resStatus;
    }

    public String getResTime() {
        return resTime;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getRemark() {
        return remark;
    }

}
