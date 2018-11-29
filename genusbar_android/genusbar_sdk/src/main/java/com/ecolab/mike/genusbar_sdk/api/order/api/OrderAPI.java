package com.ecolab.mike.genusbar_sdk.api.order.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ecolab.mike.genusbar_sdk.api.order.bean.OrderRequest;

public interface OrderAPI {

    String getOrderList(@NonNull String email, @Nullable Integer pageIndex, @Nullable Integer pageCount);

    String getOrderPeriod(@NonNull String dateString);

    String createOrder(@NonNull OrderRequest order);
}
