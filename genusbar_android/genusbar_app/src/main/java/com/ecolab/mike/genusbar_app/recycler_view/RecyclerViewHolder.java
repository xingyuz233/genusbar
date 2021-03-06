package com.ecolab.mike.genusbar_app.recycler_view;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private View mRootView;

    private final SparseArray<View> mViews = new SparseArray<>();

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mRootView = itemView;
    }

    public View getRootView() {
        return mRootView;
    }

    public <T extends View> T get(int id) {
        return (T) bindView(id);
    }

    public void setOnClickListener(View.OnClickListener l, int... ids) {
        if (ids == null) {
            return;
        }
        for (int id: ids) {
            get(id).setOnClickListener(l);
        }
    }

    public void setText(int id, String text) {
        TextView textView = get(id);
        textView.setText(text);
    }

    private <T extends View> T bindView(int id) {
        T view = (T) mViews.get(id);
        if (view == null) {
            view = (T) mRootView.findViewById(id);
            mViews.put(id, view);
        }
        return view;
    }



}
