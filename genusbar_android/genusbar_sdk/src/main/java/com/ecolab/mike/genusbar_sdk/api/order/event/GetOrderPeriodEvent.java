package com.ecolab.mike.genusbar_sdk.api.order.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ecolab.mike.genusbar_sdk.api.base.BaseEvent;
import com.ecolab.mike.genusbar_sdk.api.order.bean.Order;

import java.util.List;

public class GetOrderPeriodEvent extends BaseEvent<List<String>> {
    /**
     * @param uuid 唯一识别码
     */
    public GetOrderPeriodEvent(@Nullable String uuid) {
        super(uuid);
    }

    /**
     * @param uuid   唯一识别码
     * @param code   网络返回码
     * @param periods 实体数据
     */
    public GetOrderPeriodEvent(@Nullable String uuid, @NonNull Integer code, @Nullable List<String> periods) {
        super(uuid, code, periods);
    }

}
