package com.ecolab.mike.genusbar_app.base;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ecolab.mike.genusbar_sdk.api.GenusbarAPI;
import com.ecolab.mike.genusbar_sdk.utils.DataCache;

import java.io.Serializable;

public abstract class BaseFragment extends Fragment {
    protected ViewHolder mViewHolder;
    protected DataCache mDataCache;
    protected GenusbarAPI mGenusbarAPI;
    protected Toast mToast;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGenusbarAPI = GenusbarAPI.getSingleInstance();
        mDataCache = DataCache.getSingleInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        mViewHolder = new ViewHolder(inflater, container, getLayoutId());
        return mViewHolder.getRootView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void toast(final String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initViews();


    protected void openActivity(Class<?> cls) {
        openActivity(getActivity(), cls);
    }


    public static void openActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    /**
     * 打开 Activity 的同时传递一个数据
     */
    protected <V extends Serializable> void openActivity(Class<?> cls, String key, V value) {
        openActivity(getActivity(), cls, key, value);
    }


    /**
     * 打开 Activity 的同时传递一个数据
     */
    public <V extends Serializable> void openActivity(Context context, Class<?> cls, String key, V value) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(key, value);
        context.startActivity(intent);
    }


}

