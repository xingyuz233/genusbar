package com.ecolab.mike.genusbar_app.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ecolab.mike.genusbar_app.R;
import com.ecolab.mike.genusbar_app.activity.CreateOrderActivity;
import com.ecolab.mike.genusbar_app.provider.OrderProvider;
import com.ecolab.mike.genusbar_app.recycler_view.HeaderFooterAdapter;
import com.ecolab.mike.genusbar_app.recycler_view.SimpleRefreshRecyclerFragment;
import com.ecolab.mike.genusbar_sdk.api.order.bean.Order;
import com.ecolab.mike.genusbar_sdk.api.order.event.GetOrderListEvent;

import java.util.List;

public class OrderFragment extends SimpleRefreshRecyclerFragment<Order, GetOrderListEvent> implements View.OnClickListener {

    private boolean isFirstLaunch = true;
    private String email;

    public static OrderFragment getInstance() {
        Bundle args = new Bundle();
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initViews() {
        super.initViews();
        mViewHolder.setOnClickListener(this, R.id.add_order_button);
    }

    @Override
    public void initData(HeaderFooterAdapter adapter) {
        // 优先从缓存中获取数据，如果是第一次加载则恢复滚动位置，如果没有缓存则从网络加载
        List<Object> orders = mDataCache.getOrderListObj();
        email = mDataCache.getEmail();
        if (null != orders && orders.size() > 0) {

            pageIndex = mDataCache.getOrderListPageIndex();
            adapter.addDatas(orders);
            if (isFirstLaunch) {

                Integer lastPosition = mDataCache.getOrderListLastPosition();
                if (lastPosition == null) {
                    lastPosition = 0;
                }
                mRecyclerView.getLayoutManager().scrollToPosition(lastPosition);
                isFirstAddFooter = false;
                isFirstLaunch = false;
            }
        } else {
            loadMore();
        }
    }

    @Override
    protected void setAdapterRegister(Context context, RecyclerView recyclerView,
                                      HeaderFooterAdapter adapter) {
        adapter.register(Order.class, new OrderProvider(getContext()));
    }

    @NonNull
    @Override
    protected String request(int pageIndex, int pageCount) {
        return mGenusbarAPI.getOrderList(email, pageIndex, pageCount);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onRefresh(GetOrderListEvent event, HeaderFooterAdapter adapter) {
        super.onRefresh(event, adapter);
        mDataCache.saveOrderListObj(adapter.getDatas());
    }

    @Override
    protected void onLoadMore(GetOrderListEvent event, HeaderFooterAdapter adapter) {
        // TODO 排除重复数据
        super.onLoadMore(event, adapter);
        mDataCache.saveOrderListObj(adapter.getDatas());
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 存储 PageIndex
        mDataCache.saveOrderListPageIndex(pageIndex);
        // 存储 RecyclerView 滚动位置
        View view = mRecyclerView.getLayoutManager().getChildAt(0);
        int lastPosition = mRecyclerView.getLayoutManager().getPosition(view);
        mDataCache.saveOrderListLastPosition(lastPosition);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_order_button:
                openActivity(CreateOrderActivity.class);

        }
    }
}