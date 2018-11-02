package com.ecolab.mike.genusbar_sdk.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ecolab.mike.genusbar_sdk.api.order.api.OrderAPI;
import com.ecolab.mike.genusbar_sdk.api.order.api.OrderImpl;

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
    public String getOrderList(@NonNull String state, Integer offset, Integer limit) {
        return sOrderImpl.getOrderList(state, offset, limit);
    }

}
