package com.ecolab.mike.genusbar_app.base;

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

public abstract class BaseFragment extends Fragment {
    protected ViewHolder mViewHolder;
    protected DataCache mDataCache;
    protected GenusbarAPI mGenusbarAPI;
    protected Toast mToast;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataCache = new DataCache(getContext());
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

}

