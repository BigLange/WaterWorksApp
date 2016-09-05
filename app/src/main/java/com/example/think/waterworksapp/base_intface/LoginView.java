package com.example.think.waterworksapp.base_intface;

/**
 * Created by Think on 2016/6/22.
 */
public interface LoginView {
    String getUserNameValue();
    String getPassWordValues();
    void jumpIntent();
    void loginError(String errorMsg);
}
