package com.ecolab.mike.genusbar_sdk.api.order.bean;

import java.io.Serializable;
import java.util.List;

public class OrderListResponse implements Serializable {
    private int count;
    private List<Order> value;

}
