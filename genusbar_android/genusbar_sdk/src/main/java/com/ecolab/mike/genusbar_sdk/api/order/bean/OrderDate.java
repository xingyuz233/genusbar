package com.ecolab.mike.genusbar_sdk.api.order.bean;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDate {

    private LocalDate date;
    private List<String> periodList;

    public OrderDate(LocalDate date, List<String> periodList) {
        this.date = date;
        this.periodList = periodList;
    }

    public List<String> getPeriodList() {
        return periodList;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setPeriodList(List<String> periodList) {
        this.periodList = periodList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<OrderDate> generateDates(int num) {
        List<OrderDate> dateList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                dateList.add(new OrderDate(date,null));
            }
        }
        return dateList;
    }
}
