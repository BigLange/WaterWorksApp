package com.example.think.waterworksapp.base_intface;


import com.example.think.waterworksapp.bean.DeviceDomainBean;

import java.util.ArrayList;

/**
 * Created by Think on 2016/8/22.
 */
public interface GetDeviceDomainView extends LoginOutSuperView{

    void getDeviceDomainSuccess(ArrayList<DeviceDomainBean> data);
    void getDeviceDomainFail(String errorMsg);
}
