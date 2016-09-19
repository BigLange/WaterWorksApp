package com.example.think.waterworksapp.base_intface;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Think on 2016/8/23.
 */
public interface EquipmentMsgSubmitView extends LoginOutSuperView{

    long getDeviceID();
    Map<String,Boolean> getEquipmentInformation();
    String getEquipmentDetailedInformation();
    ArrayList<String> getEquipmentImageInformation();
    void submitSuccess();
    void submitFail(String errorMsg);
}
