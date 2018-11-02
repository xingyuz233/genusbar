package com.ecolab.mike.genusbar_sdk.api.order.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ecolab.mike.genusbar_sdk.api.base.BaseCallback;
import com.ecolab.mike.genusbar_sdk.api.base.BaseImpl;
import com.ecolab.mike.genusbar_sdk.api.order.bean.Order;
import com.ecolab.mike.genusbar_sdk.api.order.event.GetOrderListEvent;
import com.ecolab.mike.genusbar_sdk.utils.UUIDGenerator;

import java.util.List;

public class OrderImpl extends BaseImpl<OrderService> implements OrderAPI {

    public OrderImpl(@NonNull Context context) {
        super(context);
    }

    @Override
    public String getOrderList(String state, @Nullable Integer offset, @Nullable Integer limit) {
        final String uuid = UUIDGenerator.getUUID();
        mService.getOrderList(state, offset, limit)
                .enqueue(new BaseCallback<>(new GetOrderListEvent(uuid)));
        return uuid;
    }
}
