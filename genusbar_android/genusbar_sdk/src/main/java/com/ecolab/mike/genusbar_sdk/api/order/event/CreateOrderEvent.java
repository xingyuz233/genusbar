package com.ecolab.mike.genusbar_sdk.api.order.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ecolab.mike.genusbar_sdk.api.base.BaseEvent;
import com.ecolab.mike.genusbar_sdk.api.order.bean.Order;
import com.ecolab.mike.genusbar_sdk.api.order.bean.State;

import java.util.List;

public class CreateOrderEvent extends BaseEvent<State> {
    /**
     * @param uuid 唯一识别码
     */
    public CreateOrderEvent(@Nullable String uuid) {
        super(uuid);
    }

    /**
     * @param uuid   唯一识别码
     * @param code   网络返回码
     * @param state 实体数据
     */
    public CreateOrderEvent(@Nullable String uuid, @NonNull Integer code, @Nullable State state) {
        super(uuid, code, state);
    }
}
