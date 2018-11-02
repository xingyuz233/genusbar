package com.ecolab.mike.genusbar_sdk.api.order.bean;

import java.io.Serializable;

public class Order implements Serializable {

    private String id;
    private String state;
    private String time;

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getTime() {
        return time;
    }
}
