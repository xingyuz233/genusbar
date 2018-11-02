package com.ecolab.mike.genusbar_sdk.api.base;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ecolab.mike.genusbar_sdk.utils.ACache;
import com.ecolab.mike.genusbar_sdk.utils.DataCache;
import com.ecolab.mike.genusbar_sdk.utils.Constant;
import com.ecolab.mike.genusbar_sdk.utils.OAuth;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.TimeUnit;


import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BaseImpl<Service>{
    protected DataCache mDataCache;
    private static Retrofit mRetrofit;
    protected Service mService;

    public BaseImpl(@NonNull Context context) {
        mDataCache = new DataCache(context.getApplicationContext());
        initRetrofit();
        this.mService = mRetrofit.create(getServiceClass());
    }

    private Class<Service> getServiceClass() {
        return (Class<Service>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private void initRetrofit() {
        if (mRetrofit != null) {
            return;
        }

        // 设置Log拦截器，用于以后处理异常情况
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 为所有请求加上token
        Interceptor mTokenInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                // 如果当前没有缓存token或者请求已经附带了token,就不再添加
                if (mDataCache.getToken() == null || alreadyHasAuthorizationHeader(originalRequest)) {
                    return chain.proceed(originalRequest);
                }
                String token = OAuth.TOKEN_PREFIX + mDataCache.getToken().getTokenString();

                // 为请求添加token
                Request authorised = originalRequest.newBuilder()
                        .header(OAuth.KEY_TOKEN, token)
                        .build();
                return chain.proceed(authorised);

            }
        };

        // 配置client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .addNetworkInterceptor(mTokenInterceptor)
                .build();


        // 配置Retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private boolean alreadyHasAuthorizationHeader(Request originalRequest) {
        String token = originalRequest.header(OAuth.KEY_TOKEN);
        return token != null && !token.isEmpty();
    }
}
