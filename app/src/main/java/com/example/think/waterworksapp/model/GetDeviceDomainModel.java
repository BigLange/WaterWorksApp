package com.example.think.waterworksapp.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.think.waterworksapp.application.MyApplication;
import com.example.think.waterworksapp.base_intface.GetDeviceDomainView;
import com.example.think.waterworksapp.base_intface.RequestView;
import com.example.think.waterworksapp.bean.DeviceDomainBean;
import com.example.think.waterworksapp.bean.EquipmentMsgBean;
import com.example.think.waterworksapp.utils.ICallBack;
import com.example.think.waterworksapp.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Think on 2016/8/22.
 */
public class GetDeviceDomainModel{

    private final String GET_ACQUISITION_POINT_MSG_URL = "rest/open/userDomainService /queryDomainBySelf";

    private MyApplication myApplication;
    private GetDeviceDomainView getDeviceDomainView;
    private OkHttpUtils okHttpUtils;
    private ICallBack<ArrayList<DeviceDomainBean>> iCallBack;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RequestView.REQUEST_SUCCESS:
                    getDeviceDomainView.getDeviceDomainSuccess(iCallBack.getData());
                    break;
                case RequestView.REQUEST_FAIL:
                    getDeviceDomainView.getDeviceDomainFail(msg.obj+"");
                    break;
                case RequestView.REQUEST_LOGIN_OUT:
                    getDeviceDomainView.loginOut();
                    break;
            }
        }
    };

    public GetDeviceDomainModel(Context context, GetDeviceDomainView getDeviceDomainView){
        this.getDeviceDomainView = getDeviceDomainView;
        this.myApplication = (MyApplication)context.getApplicationContext();
        okHttpUtils = OkHttpUtils.getOkHttpUtils();
    }

    public void getDeviceDomain(){
        String url = GET_ACQUISITION_POINT_MSG_URL+"?"+RequestView.TOKEN+"="+myApplication.getSession().getAccess_token();
        iCallBack = new ICallBack<>(handler,new TypeToken<ArrayList<DeviceDomainBean>>(){}.getType());
        okHttpUtils.doPostAsyn(url,"[]",iCallBack);
    }




}
