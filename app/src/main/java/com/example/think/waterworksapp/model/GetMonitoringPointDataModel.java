package com.example.think.waterworksapp.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.think.waterworksapp.application.MyApplication;
import com.example.think.waterworksapp.base_intface.GetMonitoringPointDataView;
import com.example.think.waterworksapp.base_intface.RequestView;
import com.example.think.waterworksapp.bean.AcquisitionSensorBean;
import com.example.think.waterworksapp.bean.AcquisitionSensorDataBean;
import com.example.think.waterworksapp.bean.MonitoringPointDataBean;
import com.example.think.waterworksapp.bean.RequestMonitoringPointBean;
import com.example.think.waterworksapp.utils.ICallBack;
import com.example.think.waterworksapp.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Think on 2016/8/22.
 */
public class GetMonitoringPointDataModel{



    private final String GET_MONITOR_POINT_MSG_URL = "rest/open/kpiDataService/getKpiValueList";

    private MyApplication myApplication;
    private GetMonitoringPointDataView getMonitoringPointDataView;
    private ICallBack<ArrayList<MonitoringPointDataBean>> iCallBack;

    private Gson gson;
    private OkHttpUtils okHttpUtils;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RequestView.REQUEST_SUCCESS:
                    getMonitoringPointDataView.getMonitoringPointDataSuccess(iCallBack.getData());
                    break;
                case RequestView.REQUEST_FAIL:
                    getMonitoringPointDataView.getMonitoringPointDataFail(msg.obj+"");
                    break;
                case RequestView.REQUEST_LOGIN_OUT:
                    getMonitoringPointDataView.loginOut();
                    break;
            }
        }
    };

    public GetMonitoringPointDataModel(Context context, GetMonitoringPointDataView getMonitoringPointDataView){
        this.getMonitoringPointDataView = getMonitoringPointDataView;
        this.myApplication = (MyApplication)context.getApplicationContext();
        okHttpUtils = OkHttpUtils.getOkHttpUtils();
        gson = new Gson();
    }

    public void getMonitoringPointData(long modelId){
        ArrayList<AcquisitionSensorDataBean> acquisitionSensorDataBeens = getMonitoringPointDataView.getKpiCodes(modelId);
        AcquisitionSensorBean acquisitionSensorBean = getMonitoringPointDataView.getNodeIds(modelId);
        RequestMonitoringPointBean data = new RequestMonitoringPointBean();
        data.setNodeIds(acquisitionSensorBean.getId());
        for (AcquisitionSensorDataBean dataBean:acquisitionSensorDataBeens){
            data.setKpiCodes(dataBean.getId());
        }
        String requestJson = gson.toJson(data);
        Log.e("requestJson",requestJson);
        String url = GET_MONITOR_POINT_MSG_URL+"?"+RequestView.TOKEN+"="+myApplication.getSession().getAccess_token();
        iCallBack = new ICallBack<>(handler,new TypeToken<ArrayList<MonitoringPointDataBean>>(){}.getType());
        okHttpUtils.doPostAsyn(url,requestJson,iCallBack);
    }

}
