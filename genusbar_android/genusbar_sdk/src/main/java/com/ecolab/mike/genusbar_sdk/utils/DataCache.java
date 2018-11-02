package com.ecolab.mike.genusbar_sdk.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.LruCache;

import com.ecolab.mike.genusbar_sdk.api.order.bean.Order;
import com.ecolab.mike.genusbar_sdk.bean.Token;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataCache {

    private static int M = 1024 * 1024;
    private ACache diskCache;
    private LruCache<String, Object> lruCache;

    public DataCache(Context context) {
        diskCache = ACache.get(context);
        lruCache = new LruCache<>(5 * M);
    }

    public <T extends Serializable> void saveListData(String key, List<T> data) {
        ArrayList<T> datas = (ArrayList<T>) data;
        lruCache.put(key, datas);
        diskCache.put(key, datas, ACache.TIME_DAY * 7);
    }

    public <T extends Serializable> void saveData(String key, @NonNull T data) {
        lruCache.put(key, data);
        diskCache.put(key, data, ACache.TIME_DAY * 7);
    }

    public <T extends Serializable> T getData(@NonNull String key) {
        T result = (T) lruCache.get(key);
        if (result == null) {
            result = (T) diskCache.getAsObject(key);
            if (result != null) {
                lruCache.put(key, result);
            }
        }
        return result;
    }

    public void removeDate(String key) {
        diskCache.remove(key);
    }



    // --token

    public void saveToken(@NonNull Token token) {
        saveData("token", token);
    }

    public Token getToken() {
        return getData("token");
    }

    public void clearToken() {
        removeDate("token");
    }


    // --order

    public List<Object> getNotStartedOrderListObj() {
        return getData("not_started_order_list");
    }

    public List<Object> getInProgressOrderListObj() {
        return getData("in_progress_order_list");
    }

    public List<Object> getCompletedOrderListObj() {
        return getData("completed_order_list");
    }

    public List<Object> getDeferredOrderListObj() {
        return getData("deferred_order_list");
    }


    public void saveNotStartedOrderListObj(List<Object> list) {
        saveListData("not_started_order_list", (ArrayList)list);
    }

    public void saveInProgressOrderListObj(List<Object> list) {
        saveListData("not_started_order_list", (ArrayList)list);
    }

    public void saveCompletedOrderListObj(List<Object> list) {
        saveListData("not_started_order_list", (ArrayList)list);
    }

    public void saveDeferredOrderListObj(List<Object> list) {
        saveListData("not_started_order_list", (ArrayList)list);
    }


}
