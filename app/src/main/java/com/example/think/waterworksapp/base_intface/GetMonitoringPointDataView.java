package com.example.think.waterworksapp.base_intface;

import com.example.think.waterworksapp.bean.MonitoringPointDataBean;

import java.util.ArrayList;

/**
 * Created by Think on 2016/8/22.
 */
public interface GetMonitoringPointDataView {
    void getMonitoringPointDataSuccess(ArrayList<MonitoringPointDataBean> data);
    void getMonitoringPointDataFail(String errorMsg);
}
