package com.ecolab.mike.genusbar_sdk.api.order.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ecolab.mike.genusbar_sdk.api.base.BaseEvent;
import com.ecolab.mike.genusbar_sdk.api.order.bean.Order;

import java.util.List;

public class GetOrderListEvent extends BaseEvent<List<Order>> {

    /**
     * @param uuid 唯一识别码
     */
    public GetOrderListEvent(@Nullable String uuid) {
        super(uuid);
    }

    /**
     * @param uuid   唯一识别码
     * @param code   网络返回码
     * @param orders 实体数据
     */
    public GetOrderListEvent(@Nullable String uuid, @NonNull Integer code, @Nullable List<Order> orders) {
        super(uuid, code, orders);
    }

}
