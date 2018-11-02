package com.ecolab.mike.genusbar_sdk.api.order.api;

import com.ecolab.mike.genusbar_sdk.api.order.bean.Order;

import java.util.List;

import okhttp3.Interceptor;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OrderService {
    /**
     * 获取order列表
     * @param state the order's state [not_started, in_progress, completed, deferred]
     * @param offset start index
     * @param limit end index
     * @return
     */
    @GET("order/list")
    Call<List<Order>> getOrderList(@Query("state") String state,
                                   @Query("offset") Integer offset, @Query("limit") Integer limit);

}

