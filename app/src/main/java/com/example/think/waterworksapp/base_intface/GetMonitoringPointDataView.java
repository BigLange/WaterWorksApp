package com.example.think.waterworksapp.base_intface;

import com.example.think.waterworksapp.bean.AcquisitionSensorBean;
import com.example.think.waterworksapp.bean.AcquisitionSensorDataBean;
import com.example.think.waterworksapp.bean.MonitoringPointDataBean;

import java.util.ArrayList;

/**
 * Created by Think on 2016/8/22.
 */
public interface GetMonitoringPointDataView extends LoginOutSuperView{
    AcquisitionSensorBean getNodeIds(Long modelId);
    ArrayList<AcquisitionSensorDataBean> getKpiCodes(Long modelId);
    void getMonitoringPointDataSuccess(ArrayList<MonitoringPointDataBean> data);
    void getMonitoringPointDataFail(String errorMsg);
}
