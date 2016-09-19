package com.example.think.waterworksapp.base_intface;

import com.example.think.waterworksapp.bean.InspectionItemBean;

import java.util.ArrayList;

/**
 * Created by Think on 2016/8/22.
 */
public interface GetInspectionItemView extends LoginOutSuperView{
    void getInspectionItemSuccess(ArrayList<InspectionItemBean> data);
    void getInspectionItemFail(String errorMsg);
}
