package com.ecolab.mike.genusbar_sdk.api.base;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import org.greenrobot.eventbus.EventBus;


public class BaseCallback<T> implements Callback<T> {
    protected BaseEvent<T> event;                   // 事件

    public <Event extends BaseEvent<T>> BaseCallback(@NonNull Event event) {
        this.event = event;
    }

    /**
     * Invoked for a received HTTP response.
     * <p>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call {@link Response#isSuccessful()} to determine if the response indicates success.
     *
     * @param call     回调
     * @param response 请求
     */
    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (response.isSuccessful()) {
            event.setCode(response.code());
            event.setBean(response.body());
            event.setOk(true);
        } else {
            event.setCode(response.code());
            event.setBean(null);
            event.setOk(false);
        }
        EventBus.getDefault().post(event);
    }

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call 回调
     * @param t    抛出的异常
     */
    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        event.setCode(-1);
        event.setBean(null);
        event.setOk(false);
        EventBus.getDefault().post(event);
    }
}
