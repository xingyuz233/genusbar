package com.ecolab.mike.genusbar_app.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ecolab.mike.genusbar_app.R;
import com.ecolab.mike.genusbar_app.base.BaseFragment;
import com.ecolab.mike.genusbar_app.base.ViewHolder;
import com.ecolab.mike.genusbar_sdk.api.base.BaseEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public abstract class RefreshRecyclerFragment<T, Event extends BaseEvent<List<T>>> extends BaseFragment {
    // 请求状态 - 下拉刷新，上拉加载更多
    public static final String POST_LOAD_MORE = "load_more";
    public static final String POST_REFRESH = "refresh";
    // 请求类型
    private ArrayMap<String, String> mPostTypes = new ArrayMap<>();

    // 当前状态
    private static final int STATE_NORMAL = 0;  // 正常
    private static final int STATE_NO_MORE = 1; // 正在
    private static final int STATE_LOADING = 2; // 加载
    private static final int STATE_REFRESH = 3; // 刷新
    private int mState = STATE_NORMAL;

    // 分页加载
    protected int pageIndex = 0;    // 当前页面
    protected int pageCount = 20;   // 每页个数

    // View
    private SwipeRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;

    // 状态
    private boolean refreshEnable = true;   // 是否允许刷新
    private boolean loadMoreEnable = true;  // 是否允许加载

    // 适配器
    protected HeaderFooterAdapter mAdapter;
    protected FooterProvider mFooterProvider;

    protected boolean isFirstAddFooter = true;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_recycler;
    }

    @Override
    protected void initViews() {
        // 适配器
        mAdapter = new HeaderFooterAdapter();
        mFooterProvider = new FooterProvider(getContext()) {
            @Override
            public void needLoadMore() {
                if (isFirstAddFooter) {
                    isFirstAddFooter = false;
                    return;
                }
                loadMore();
            }

        };

        mFooterProvider.setFooterNormal();
        mAdapter.registerFooter(new Footer(), mFooterProvider);

        // RefreshLayout
        mRefreshLayout = mViewHolder.get(R.id.refresh_layout);
        mRefreshLayout.setEnabled(true);

        // RecyclerView
        mRecyclerView = mViewHolder.get(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(getRecyclerViewLayoutManager());
        setAdapterRegister(getContext(), mRecyclerView, mAdapter);

        // 监听 RefreshLayout 下拉刷新
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        initData(mAdapter);
    }

    protected void refresh() {
        if (!refreshEnable) {
            return;
        }
        pageIndex = 0;
        String uuid = request(pageIndex, pageCount);
        mPostTypes.put(uuid, POST_REFRESH);
        pageIndex++;
        mState = STATE_REFRESH;
    }


    protected void loadMore() {
        if (!loadMoreEnable) {
            return;
        }
        if (mState == STATE_NO_MORE) {
            return;
        }
        String uuid = request(pageIndex, pageCount);
        mPostTypes.put(uuid, POST_LOAD_MORE);
        pageIndex++;
        mState = STATE_LOADING;
        mFooterProvider.setFooterLoading();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultEvent(Event event) {
        String postType = mPostTypes.get(event.getUuid());
        if (event.isOk()) {
            if (postType.equals(POST_LOAD_MORE)) {
                onLoadMore(event);
            } else if (postType.equals(POST_REFRESH)) {
                onRefresh(event);
            }
        } else {
            onError(event);
        }
        mPostTypes.remove(event.getUuid());
    }

    protected void onRefresh(Event event) {
        mState = STATE_NORMAL;
        mRefreshLayout.setRefreshing(false);
        onRefresh(event, mAdapter);
    }

    protected void onLoadMore(Event event) {
        if (event.getBean().size() < pageCount) {
            mState = STATE_NO_MORE;
            mFooterProvider.setFooterNormal();
        } else {
            mState = STATE_NORMAL;
            mFooterProvider.setFooterNormal();
        }
        onLoadMore(event, mAdapter);
    }

    protected void onError(Event event) {
        // 状态重置为正常, 以便可以重试, 否则进入异常后无法再变为正常状态
        mState = STATE_NORMAL;

        String postType = mPostTypes.get(event.getUuid());
        if (postType.equals(POST_LOAD_MORE)) {
            mFooterProvider.setFooterError(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pageIndex--;
                    loadMore();
                }
            });
        } else if (postType.equals(POST_REFRESH)) {
            mRefreshLayout.setRefreshing(false);
            mFooterProvider.setFooterNormal();
        }
        onError(event, postType);
    }

    public void setRefreshEnable(boolean refreshEnable) {
        this.refreshEnable = refreshEnable;
        mRefreshLayout.setEnabled(refreshEnable);
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    //--- 需要继承类处理的部分 ----------------------------------------------------------------------

    /**
     * 加载数初始化数据，可以从缓存或者其他地方加载，
     * 如果没有初始数据，一般调用 loadMore() 即可。
     *
     * @param adapter 适配器
     */
    public abstract void initData(HeaderFooterAdapter adapter);

    /**
     * 为 RecyclerView 的 Adapter 注册数据类型
     * 例如： adapter.register(Bean.class, new BeanProvider(getContext()));
     *
     * @param context      上下文
     * @param recyclerView RecyclerView
     * @param adapter      Adapter
     */
    protected abstract void setAdapterRegister(Context context, RecyclerView recyclerView,
                                               HeaderFooterAdapter adapter);

    /**
     * 获取 RecyclerView 的 LayoutManager
     * 例如： return new LinerLayoutManager(context);
     *
     * @return LayoutManager
     */
    @NonNull
    protected abstract RecyclerView.LayoutManager getRecyclerViewLayoutManager();

    /**
     * 请求数据，并返回请求的 uuid
     * 例如：return mDiycode.getTopicsList(null, mNodeId, offset, limit);
     *
     * @param pageIndex 页号
     * @param pageCount  页量
     * @return uuid
     */
    @NonNull protected abstract String request(int pageIndex, int pageCount);

    /**
     * 数据刷新成功的回调，由于不同页面可能要对数据进行处理，例如重新排序，清理掉一些无效数据等，所以由子类自己实现，
     * 如果不需要特殊处理，一般像下面这样写就行:
     * adapter.clearDatas();
     * adapter.addDatas(event.geiBean());
     *
     * @param event   Event
     * @param adapter Adapter
     */
    protected abstract void onRefresh(Event event, HeaderFooterAdapter adapter);

    /**
     * 数据加载成功时调用，如果不需要对数据进行特殊处理，这样写就行：
     * adapter.addDatas(event.getBean());
     *
     * @param event   Event
     * @param adapter Adapter
     */
    protected abstract void onLoadMore(Event event, HeaderFooterAdapter adapter);

    /**
     * 数据加载错误时调用，你可以在这里获取错误类型并进行处理，如果不需要特殊处理，弹出一个 toast 提醒用户即可。
     * if (postType.equals(POST_LOAD_MORE)) {
     * toast("加载更多失败");
     * } else if (postType.equals(POST_REFRESH)) {
     * toast("刷新数据失败");
     * }
     *
     * @param event
     * @param postType
     */
    protected abstract void onError(Event event, String postType);
}
