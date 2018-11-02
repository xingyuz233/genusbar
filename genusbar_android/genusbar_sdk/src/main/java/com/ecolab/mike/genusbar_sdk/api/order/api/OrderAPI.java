package com.ecolab.mike.genusbar_sdk.api.order.api;

import android.support.annotation.Nullable;

public interface OrderAPI {

    String getOrderList(String state, @Nullable Integer offset, @Nullable Integer limit);
}
