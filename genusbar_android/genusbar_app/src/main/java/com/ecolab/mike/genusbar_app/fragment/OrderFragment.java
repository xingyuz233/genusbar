package com.ecolab.mike.genusbar_app.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecolab.mike.genusbar_app.R;
import com.ecolab.mike.genusbar_app.base.BaseFragment;
import com.ecolab.mike.genusbar_app.entity.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;


@SuppressLint("ValidFragment")
public class OrderFragment extends BaseFragment {
    private String mTitle;

    public static OrderFragment getInstance() {
        OrderFragment sf = new OrderFragment();
        sf.mTitle = "我的预约";
        return sf;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initViews() {
        final ViewPager mViewPager = mViewHolder.get(R.id.view_pager);
        final CommonTabLayout mTableLayout = mViewHolder.get(R.id.tab_layout);

        String[] mTitles = {"未开始", "进行中", "已完成", "已逾期"};
        ArrayList<CustomTabEntity> mTabEntityList = new ArrayList<>();
        final ArrayList<Fragment> mFragmentList = new ArrayList<>();

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntityList.add(new TabEntity(mTitles[i], R.mipmap.tab_speech_select, R.mipmap.tab_speech_unselect));
            mFragmentList.add(SimpleCardFragment.getInstance(mTitles[i]));
        }

        mTableLayout.setTabData(mTabEntityList);
        mTableLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTableLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}