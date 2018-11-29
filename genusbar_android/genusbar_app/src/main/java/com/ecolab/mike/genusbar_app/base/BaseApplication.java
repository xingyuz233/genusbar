package com.ecolab.mike.genusbar_app.base;

import android.app.Application;

import com.ecolab.mike.genusbar_sdk.api.GenusbarAPI;
import com.ecolab.mike.genusbar_sdk.utils.DataCache;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataCache.init(this);
        GenusbarAPI.init(this);

    }
}
