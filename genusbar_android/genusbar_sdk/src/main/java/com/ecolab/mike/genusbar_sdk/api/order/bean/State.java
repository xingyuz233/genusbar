package com.ecolab.mike.genusbar_sdk.api.order.bean;

import java.io.Serializable;

public class State implements Serializable {
    private String status;
    private String message;

    public String getMessage() {
        return message;
    }

    public boolean success() {
        return status != null && status.substring(0,7).equals("Success");
    }
}
