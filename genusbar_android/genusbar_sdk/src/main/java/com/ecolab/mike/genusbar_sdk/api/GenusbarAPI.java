package com.ecolab.mike.genusbar_sdk.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ecolab.mike.genusbar_sdk.api.order.api.OrderAPI;
import com.ecolab.mike.genusbar_sdk.api.order.api.OrderImpl;
import com.ecolab.mike.genusbar_sdk.api.order.bean.Order;
import com.ecolab.mike.genusbar_sdk.api.order.bean.OrderRequest;

public class GenusbarAPI implements OrderAPI {

    private static OrderImpl sOrderImpl;

    private volatile static GenusbarAPI mGenusbarAPI;

    private GenusbarAPI() {
    }

    public static GenusbarAPI getSingleInstance() {
        if (mGenusbarAPI == null) {
            synchronized (GenusbarAPI.class) {
                if (mGenusbarAPI == null) {
                    mGenusbarAPI = new GenusbarAPI();
                }
            }
        }
        return mGenusbarAPI;
    }

    // 初始化
    public static GenusbarAPI init(@NonNull Context context) {
        initImplement(context);
        return getSingleInstance();
    }

    private static void initImplement(Context context) {
        sOrderImpl = new OrderImpl(context);
    }

    //--- order ---------------------------
    @Override
    public String getOrderList(@NonNull String email, Integer pageIndex, Integer pageSize) {
        return sOrderImpl.getOrderList(email, pageIndex, pageSize);
    }

    @Override
    public String getOrderPeriod(@NonNull String dateString) {
        return sOrderImpl.getOrderPeriod(dateString);
    }

    @Override
    public String createOrder(@NonNull OrderRequest order) {
        return sOrderImpl.createOrder(order);
    }

}
