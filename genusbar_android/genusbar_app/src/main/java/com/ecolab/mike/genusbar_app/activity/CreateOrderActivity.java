package com.ecolab.mike.genusbar_app.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ecolab.mike.genusbar_app.R;
import com.ecolab.mike.genusbar_app.base.BaseActivity;
import com.ecolab.mike.genusbar_sdk.api.order.bean.Order;
import com.ecolab.mike.genusbar_sdk.api.order.bean.OrderDate;
import com.ecolab.mike.genusbar_sdk.api.order.bean.OrderRequest;
import com.ecolab.mike.genusbar_sdk.api.order.bean.State;
import com.ecolab.mike.genusbar_sdk.api.order.event.CreateOrderEvent;
import com.ecolab.mike.genusbar_sdk.api.order.event.GetOrderPeriodEvent;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.microsoft.aad.adal.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreateOrderActivity extends BaseActivity implements View.OnClickListener{

    private List<LocalDate> orderDateList;

    private MaterialSpinner mDateSpinner;

    private MaterialSpinner mTimeSpinner;

    private EditText mOrderDescription;

    private static final int MAX_DISPLAY_DATES = 100;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_order;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void initDatas() {
        initDays(MAX_DISPLAY_DATES);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void initViews() {
        mDateSpinner = mViewHolder.get(R.id.date_spinner);
        mDateSpinner.setItems(orderDateList);
        mDateSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Snackbar.make(view, "选择了 "+item, Snackbar.LENGTH_LONG).show();
                loadPeriod((LocalDate) item);
            }
        });

        mTimeSpinner = mViewHolder.get(R.id.time_spinner);
        mOrderDescription = mViewHolder.get(R.id.order_description);

        mViewHolder.setOnClickListener(this, R.id.create_order);

//        final ArrayAdapter<LocalDate> mDateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, localDateList);
//        mDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mDateSpinner.setAdapter(mDateAdapter);

//        mDateSpinner.setOnItemClickListener(new Spinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });


//        mDateSpinner.setOnItemClickListener(new );

    }

    private void showPeriod(List<String> periodList) {
        mTimeSpinner.setItems(periodList);
        mTimeSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Snackbar.make(view, "选择了 "+item, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void loadPeriod(LocalDate date) {
        mGenusbarAPI.getOrderPeriod(date.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initDays(int num) {
        orderDateList = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for (int i = 0; i < num; i++) {
            LocalDate date = now.plusDays(i);
            if (!hasOrdered(date) && date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                orderDateList.add(date);
            }
        }

    }

    private boolean hasOrdered(LocalDate localDate) {
        for (Object object: mDataCache.getOrderListObj()) {
            if (((Order)object).getResTime().substring(0,10).equals(localDate.toString().substring(0,10))) {
                return true;
            }
        }
        return false;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPeriod(GetOrderPeriodEvent event) {
        if (event.isOk()) {
            showPeriod(event.getBean());
        } else {
            toastShort("获取时间段失败，请检查网络设置");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreateOrder(CreateOrderEvent event) {
        if (event.isOk()) {
            State state = event.getBean();
            if (state.success()) {
                toastShort(state.getMessage());
                finish();
            } else {
                toastShort(state.getMessage());
            }
        } else {
            toastShort(event.getCodeDescription());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if (!mDateSpinner.getItems().isEmpty()) {
            loadPeriod((LocalDate) mDateSpinner.getItems().get(mDateSpinner.getSelectedIndex()));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_order:
                LocalDate date = (LocalDate) mDateSpinner.getItems().get(mDateSpinner.getSelectedIndex());
                String time = (String) mTimeSpinner.getItems().get(mTimeSpinner.getSelectedIndex());
                String content = mOrderDescription.getText().toString();
                if (date == null || time == null || time.isEmpty() || content.isEmpty()) {
                    toastShort("内容不能有空");
                } else {
                    String name = mDataCache.getName();
                    String email = mDataCache.getEmail();
                    if (name == null || email == null) {
                        toastShort("未检测到用户登陆，请重新登录");
                    } else {
                        OrderRequest newOrder = new OrderRequest(name, email, content, date.toString() + " " + time.substring(0,5));
                        mGenusbarAPI.createOrder(newOrder);
                    }
                }
                break;
        }
    }


}
