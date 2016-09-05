package com.example.think.waterworksapp.base_intface;

import com.example.think.waterworksapp.bean.InspectionItemBean;

/**
 * Created by Think on 2016/8/22.
 */
public interface GetInspectionItemView {
    void getInspectionItemSuccess(InspectionItemBean data);
    void getInspectionItemFail(String errorMsg);
}
