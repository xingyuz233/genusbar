package com.ecolab.mike.genusbar_app.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TableLayout;

import com.ecolab.mike.genusbar_app.R;
import com.ecolab.mike.genusbar_app.base.BaseActivity;
import com.ecolab.mike.genusbar_app.entity.TabEntity;
import com.ecolab.mike.genusbar_app.fragment.OrderFragment;
import com.ecolab.mike.genusbar_app.fragment.SimpleCardFragment;
import com.ecolab.mike.genusbar_app.fragment.UserFragment;
import com.ecolab.mike.genusbar_app.utils.ViewFindUtils;
import com.ecolab.mike.genusbar_sdk.utils.DataCache;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.flyco.tablayout.widget.MsgView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Random;

public class HomeActivity extends BaseActivity {

    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntryList = new ArrayList<>();


    private View mDecorView;
//    private ViewPager mViewPager;
    private CommonTabLayout mTabLayout;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initDatas() {
//        mCache = new DataCache(this);
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void initViews() {
        FrameLayout mFrameLayout = mViewHolder.get(R.id.frame_layout);
        CommonTabLayout mTabLayout = mViewHolder.get(R.id.tab_layout);


        // 初始化 TabEntries && fragments
        String[] mTitles = {"我的预约", "账号设置"};

        int[] mIconUnselectIds = {
                R.mipmap.tab_speech_unselect,
                R.mipmap.tab_contact_unselect};
        int[] mIconSelectIds = {
                R.mipmap.tab_speech_select,
                R.mipmap.tab_contact_select};
        Fragment[] mFragments = {
                OrderFragment.getInstance(),
                UserFragment.getInstance()
        };

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntryList.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
            mFragmentList.add(mFragments[i]);
        }

        // 绑定fragment
        mTabLayout.setTabData(mTabEntryList, this, R.id.frame_layout, mFragmentList);
    }

}
