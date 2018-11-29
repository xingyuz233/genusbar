package com.ecolab.mike.genusbar_sdk.api.order.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ecolab.mike.genusbar_sdk.api.base.BaseCallback;
import com.ecolab.mike.genusbar_sdk.api.base.BaseImpl;
import com.ecolab.mike.genusbar_sdk.api.order.bean.Order;
import com.ecolab.mike.genusbar_sdk.api.order.bean.OrderRequest;
import com.ecolab.mike.genusbar_sdk.api.order.bean.State;
import com.ecolab.mike.genusbar_sdk.api.order.event.CreateOrderEvent;
import com.ecolab.mike.genusbar_sdk.api.order.event.GetOrderListEvent;
import com.ecolab.mike.genusbar_sdk.api.order.event.GetOrderPeriodEvent;
import com.ecolab.mike.genusbar_sdk.utils.UUIDGenerator;

import java.util.List;

public class OrderImpl extends BaseImpl<OrderService> implements OrderAPI {

    public OrderImpl(@NonNull Context context) {
        super(context);
    }

    @Override
    public String getOrderList(@NonNull String email, @Nullable Integer pageIndex, @Nullable Integer pageCount) {
        final String uuid = UUIDGenerator.getUUID();
        mService.getOrderList(email, pageIndex + 1, pageCount)
                .enqueue(new BaseCallback<>(new GetOrderListEvent(uuid)));
        return uuid;
    }

    @Override
    public String getOrderPeriod(@NonNull String dateString) {
        final String uuid = UUIDGenerator.getUUID();
        mService.getOrderPeriod(dateString)
                .enqueue(new BaseCallback<>(new GetOrderPeriodEvent(uuid)));
        return uuid;
    }

    @Override
    public String createOrder(@NonNull OrderRequest order) {
        final String uuid = UUIDGenerator.getUUID();
        mService.createOrder(order)
                .enqueue(new BaseCallback<>(new CreateOrderEvent(uuid)));
        return uuid;
    }
}
