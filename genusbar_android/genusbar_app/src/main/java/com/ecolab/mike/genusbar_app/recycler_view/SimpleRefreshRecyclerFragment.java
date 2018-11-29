package com.ecolab.mike.genusbar_app.recycler_view;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;

import com.ecolab.mike.genusbar_sdk.api.base.BaseEvent;

import java.util.List;

public abstract class SimpleRefreshRecyclerFragment<T, Event extends BaseEvent<List<T>>> extends
        RefreshRecyclerFragment<T, Event> {

    @NonNull @Override protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new SpeedyLinearLayoutManager(getContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override protected void onRefresh(Event event, HeaderFooterAdapter adapter) {
        adapter.clearDatas();
        adapter.addDatas(event.getBean());
//        toast("刷新成功");
    }

    @Override protected void onLoadMore(Event event, HeaderFooterAdapter adapter) {
        adapter.addDatas(event.getBean());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override protected void onError(Event event, String postType) {
        if (postType.equals(POST_LOAD_MORE)) {
            toast("加载更多失败, "+event.getCodeDescription());
        } else if (postType.equals(POST_REFRESH)) {
            toast("刷新数据失败, "+event.getCodeDescription());
        }
    }
}