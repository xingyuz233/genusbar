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

    private volatile static DataCache mDataCache;


    public static DataCache getSingleInstance() {
        return mDataCache;
    }

    private DataCache() {
    }

    public static void init(Context context) {
        mDataCache = new DataCache();
        mDataCache.diskCache = ACache.get(context);
        mDataCache.lruCache = new LruCache<>(5 * M);
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

    public void removeData(String key) {
        diskCache.remove(key);
    }



    // --token

    public void saveAcessToken(@NonNull Token token) {
        saveData("access_token", token);
    }

    public Token getAccessToken() {
        return getData("access_token");
    }

    public void clearAccessToken() {
        removeData("access_token");
    }

    public void saveAuthCenterAPIToken(@NonNull Token token) {
        saveData("auth_center_api_token", token);
    }

    public Token getAuthCenterAPIToken() {
        return getData("auth_center_api_token");
    }

    public void clearAuthCenterAPIToken() {
        removeData("auth_center_api_token");
    }

    public void saveITAppToken(@NonNull Token token) {
        saveData("it_app_token", token);
    }

    public Token getITAppToken() {
        return getData("it_app_token");
    }

    public void clearITAPPToken() {
        removeData("it_app_token");
    }


    // --order
    public List<Object> getOrderListObj() {
        return getData("order_list");
    }

    public void saveOrderListObj(List<Object> list) {
        saveData("order_list", new ArrayList<>(list));
    }

    // --order list pageIndex
    public Integer getOrderListPageIndex() {
        return getData("order_list_page_index");
    }
    public void saveOrderListPageIndex(int pageIndex) {
        saveData("order_list_page_index", pageIndex);
    }

    // --order recycler view position
    public Integer getOrderListLastPosition() {
        return getData("order_list_last_position");
    }
    public void saveOrderListLastPosition(int lastPosition) {
        saveData("order_list_page_index", lastPosition);
    }

    // --email
    public String getEmail() {
        return getData("email");
    }

    public void saveEmail(String email) {
        saveData("email", email);
    }

    public void removeEmail() {
        removeData("email");
    }

    // --name
    public String getName() {
        return getData("name");
    }

    public void saveName(String name) {
        saveData("name", name);
    }

    public void removeName() {
        removeData("name");
    }

//    public List<Object> getNotStartedOrderListObj() {
//        return getData("not_started_order_list");
//    }
//
//    public List<Object> getInProgressOrderListObj() {
//        return getData("in_progress_order_list");
//    }
//
//    public List<Object> getCompletedOrderListObj() {
//        return getData("completed_order_list");
//    }
//
//    public List<Object> getDeferredOrderListObj() {
//        return getData("deferred_order_list");
//    }
//
//
//    public void saveNotStartedOrderListObj(List<Object> list) {
//        saveListData("not_started_order_list", (ArrayList)list);
//    }
//
//    public void saveInProgressOrderListObj(List<Object> list) {
//        saveListData("not_started_order_list", (ArrayList)list);
//    }
//
//    public void saveCompletedOrderListObj(List<Object> list) {
//        saveListData("not_started_order_list", (ArrayList)list);
//    }
//
//    public void saveDeferredOrderListObj(List<Object> list) {
//        saveListData("not_started_order_list", (ArrayList)list);
//    }


}
