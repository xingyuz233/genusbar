package com.ecolab.mike.genusbar_app.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.ecolab.mike.genusbar_app.OrderContentActivity;
import com.ecolab.mike.genusbar_app.R;
import com.ecolab.mike.genusbar_app.recycler_view.BaseViewProvider;
import com.ecolab.mike.genusbar_app.recycler_view.RecyclerViewHolder;
import com.ecolab.mike.genusbar_sdk.api.order.bean.Order;

public class OrderProvider extends BaseViewProvider<Order> {

    private static final String[] STATE_LIST = {"未开始", "进行中", "已完成", "已逾期"};

    public OrderProvider(@NonNull Context context) {
        super(context, R.layout.item_order);
    }

    @Override
    public void onViewHolderIsCreated(RecyclerViewHolder holder) {

    }

    /**
     * 在绑定数据时调用，需要用户自己处理
     *
     * @param holder ViewHolder
     * @param bean   数据
     */
    @Override
    public void onBindView(RecyclerViewHolder holder, final Order bean) {

        holder.setText(R.id.order_number, bean.getNumber());
        holder.setText(R.id.order_state, STATE_LIST[bean.getRestatus()]);
        holder.setText(R.id.order_time, bean.getResTime());


        View.OnClickListener listener = new View.OnClickListener() {
            @Override public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.item:
                        OrderContentActivity.newInstance(mContext, bean);
                        break;
                }
            }
        };

        holder.setOnClickListener(listener, R.id.avatar, R.id.username, R.id.item, R.id.node_name);
    }
}
