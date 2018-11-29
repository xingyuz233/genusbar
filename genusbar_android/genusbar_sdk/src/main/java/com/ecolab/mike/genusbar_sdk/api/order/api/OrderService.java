package com.ecolab.mike.genusbar_sdk.api.order.api;

import com.ecolab.mike.genusbar_sdk.api.order.bean.Order;
import com.ecolab.mike.genusbar_sdk.api.order.bean.OrderListResponse;
import com.ecolab.mike.genusbar_sdk.api.order.bean.OrderRequest;
import com.ecolab.mike.genusbar_sdk.api.order.bean.State;

import java.util.List;

import okhttp3.Interceptor;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderService {
    /**
     * 获取order列表
     * @param
     * @return
     */
    @GET(value = "/api/res/pagedbyemail")
    Call<List<Order>> getOrderList(@Query("Email") String email, @Query("Pageindex") Integer pageIndex, @Query("Pagesize") Integer pageSize);

    @GET(value = "/api/Period/{dateString}")
    Call<List<String>> getOrderPeriod(@Path("dateString") String dateString);

    @POST(value = "/api/Res")
    Call<State> createOrder(@Body OrderRequest order);
}

