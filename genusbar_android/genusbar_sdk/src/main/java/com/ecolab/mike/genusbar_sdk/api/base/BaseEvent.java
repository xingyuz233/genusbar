package com.ecolab.mike.genusbar_sdk.api.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class BaseEvent<T> {



    protected String uuid;
    protected boolean ok;
    protected  Integer code;
    protected  T t;

    public BaseEvent(@Nullable String uuid) {
        this(uuid, -1, null);
    }

    public BaseEvent(@Nullable String uuid, @NonNull Integer code, @Nullable T t) {
        this.ok = t != null;
        this.uuid = uuid;
        this.code = code;
        this.t = t;
    }

    public T getBean() {
        return t;
    }

    public Integer getCode() {
        return code;
    }

    public String getUuid() {
        return uuid;
    }

    public boolean isOk() {
        return ok;
    }

    public void setBean(T t) {
        this.t = t;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getCodeDescription() {
        switch (code) {
            case -1:
                return "可能是网络未连接，或者数据转化失败。";
            case 200:
            case 201:
                return "请求成功，或者执行成功。";
            case 400:
                return "参数不符合API要求，或者数据格式验证没有通过。";
            case 401:
                return "用户认证失败，或缺少认证信息，比如 access_token 过期，或没传，可以尝试用 refresh_token 方式获得新的 access_token";
            case 402:
                return "用户尚未登录";
            case 403:
                return "当前用户对资源没有操作权限";
            case 404:
                return "资源不存在";
            case 500:
                return "服务器异常";
                default:
                    return "未知异常(" + code + ")";

        }
    }
}

