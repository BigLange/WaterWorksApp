package com.example.think.waterworksapp.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.think.waterworksapp.application.MyApplication;
import com.example.think.waterworksapp.base_intface.GetAcquisitionSensorDataView;
import com.example.think.waterworksapp.base_intface.GetAcquisitionSensorView;
import com.example.think.waterworksapp.base_intface.RequestView;
import com.example.think.waterworksapp.bean.AcquisitionSensorBean;
import com.example.think.waterworksapp.bean.AcquisitionSensorDataBean;
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
 * Created by Think on 2016/9/8.
 */
public class GetAcquisitionSensorDataModel{

    private final String GET_ACQUISITION_DATA_SENSOR_URL = "rest/open/resourceUIService/getKpisByModelId";

    private GetAcquisitionSensorDataView getAcquisitionSensorDataView;
    private MyApplication myApplication;
    private OkHttpUtils okHttpUtils;
    private ICallBack<ArrayList<AcquisitionSensorDataBean>> iCallBack;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RequestView.REQUEST_SUCCESS:
                    getAcquisitionSensorDataView.getAcquisitionSensorDataSuccess(iCallBack.getData());
                    break;
                case RequestView.REQUEST_FAIL:
                    getAcquisitionSensorDataView.getAcquisitionSensorDataFail(msg.obj+"");
                    break;
                case RequestView.REQUEST_LOGIN_OUT:
                    getAcquisitionSensorDataView.loginOut();
                    break;
            }
        }
    };

    public GetAcquisitionSensorDataModel(Context context, GetAcquisitionSensorDataView getAcquisitionSensorDataView){
        this.getAcquisitionSensorDataView = getAcquisitionSensorDataView;
        this.myApplication = (MyApplication)context.getApplicationContext();
        okHttpUtils = OkHttpUtils.getOkHttpUtils();
    }


    public void getAcquisitionData(long deviceId){
        String url = GET_ACQUISITION_DATA_SENSOR_URL+"?"+RequestView.TOKEN+"="+myApplication.getSession().getAccess_token();
        iCallBack = new ICallBack<>(handler,new TypeToken<ArrayList<AcquisitionSensorDataBean>>(){}.getType());
        okHttpUtils.doPostAsyn(url,"["+deviceId+"]",iCallBack);
    }

}
