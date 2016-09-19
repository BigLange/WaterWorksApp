package com.example.think.waterworksapp.base_intface;

import com.example.think.waterworksapp.bean.AcquisitionSensorBean;

import java.util.ArrayList;

/**
 * Created by Think on 2016/9/8.
 */
public interface GetAcquisitionSensorView extends LoginOutSuperView{

    void getAcquisitionSensorSuccess(ArrayList<AcquisitionSensorBean> data);
    void getAcquisitionSensorFail(String errorMsg);
}
