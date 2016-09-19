package com.example.think.waterworksapp.base_intface;

import com.example.think.waterworksapp.bean.AcquisitionSensorDataBean;

import java.util.ArrayList;

/**
 * Created by Think on 2016/9/8.
 */
public interface GetAcquisitionSensorDataView extends LoginOutSuperView{

    void getAcquisitionSensorDataSuccess(ArrayList<AcquisitionSensorDataBean> data);
    void getAcquisitionSensorDataFail(String errorMsg);
}
