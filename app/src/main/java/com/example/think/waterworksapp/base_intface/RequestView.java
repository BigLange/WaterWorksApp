package com.example.think.waterworksapp.base_intface;

import java.util.List;

/**
 * Created by Think on 2016/8/22.
 */
public interface RequestView<T> {

    int REQUEST_SUCCESS = 0;
    int REQUEST_FAIL = 1;
    int REQUEST_LOGIN_OUT = 10020;
    String RESPONSE_SUCCESS = "0";
    String RESPONSE_DATA = "data";
    String RESPONSE_CODE = "code";
    String RESPONSE_MSG = "message";
    String RESPINSE_LOGIN_OUT = "10020";
    String TOKEN = "token";

    void LoadOver(List<T> data);
    void LoadOver();
    void LoadError(String errorMsg);
}
