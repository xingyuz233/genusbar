//package com.ecolab.mike.genusbar_app.fragment;
//
//import android.content.Context;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.RequiresApi;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.ecolab.mike.genusbar_app.provider.OrderProvider;
//import com.ecolab.mike.genusbar_app.recycler_view.HeaderFooterAdapter;
//import com.ecolab.mike.genusbar_app.recycler_view.SimpleRefreshRecyclerFragment;
//import com.ecolab.mike.genusbar_sdk.api.order.bean.Order;
//import com.ecolab.mike.genusbar_sdk.api.order.event.GetOrderListEvent;
//
//import java.util.List;
//
//public class NotStartedOrderListFragment extends SimpleRefreshRecyclerFragment<Order, GetOrderListEvent> {
//
//    private boolean isFirstLaunch = true;
//
//    public static NotStartedOrderListFragment newInstance() {
//        Bundle args = new Bundle();
//        NotStartedOrderListFragment fragment = new NotStartedOrderListFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void initData(HeaderFooterAdapter adapter) {
//        // 优先从缓存中获取数据，如果是第一次加载则恢复滚动位置，如果没有缓存则从网络加载
//        List<Object> orders = mDataCache.getNotStartedOrderListObj();
//        if (null != orders && orders.size() > 0) {
////            pageIndex = mConfig.getTopicListPageIndex();
//            adapter.addDatas(orders);
//            if (isFirstLaunch) {
////                int lastPosition = mConfig.getTopicListLastPosition();
////                mRecyclerView.getLayoutManager().scrollToPosition(lastPosition);
//                isFirstAddFooter = false;
//                isFirstLaunch = false;
//            }
//        } else {
//            loadMore();
//        }
//    }
//
//    @Override
//    protected void setAdapterRegister(Context context, RecyclerView recyclerView,
//                                      HeaderFooterAdapter adapter) {
//        adapter.register(Order.class, new OrderProvider(getContext()));
//    }
//
//    @NonNull
//    @Override
//    protected String request(int offset, int limit) {
////        return mGenusbarAPI.getTopicsList(null, null, offset, limit);
//        return "";
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    protected void onRefresh(GetOrderListEvent event, HeaderFooterAdapter adapter) {
//        super.onRefresh(event, adapter);
//        mDataCache.saveNotStartedOrderListObj(adapter.getDatas());
//    }
//
//    @Override
//    protected void onLoadMore(GetOrderListEvent event, HeaderFooterAdapter adapter) {
//        // TODO 排除重复数据
//        super.onLoadMore(event, adapter);
//        mDataCache.saveNotStartedOrderListObj(adapter.getDatas());
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
////        // 存储 PageIndex
////        mConfig.saveTopicListPageIndex(pageIndex);
////        // 存储 RecyclerView 滚动位置
////        View view = mRecyclerView.getLayoutManager().getChildAt(0);
////        int lastPosition = mRecyclerView.getLayoutManager().getPosition(view);
////        mConfig.saveTopicListState(lastPosition, 0);
//    }
//}
