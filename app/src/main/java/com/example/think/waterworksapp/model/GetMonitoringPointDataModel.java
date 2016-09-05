package com.example.think.waterworksapp.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.think.waterworksapp.application.MyApplication;
import com.example.think.waterworksapp.base_intface.GetInspectionItemView;
import com.example.think.waterworksapp.base_intface.GetMonitoringPointDataView;
import com.example.think.waterworksapp.base_intface.RequestView;
import com.example.think.waterworksapp.bean.InspectionItemBean;
import com.example.think.waterworksapp.bean.MonitoringPointDataBean;
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
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Think on 2016/8/22.
 */
public class GetMonitoringPointDataModel implements Callback{

    public static final String NODE_IDS_KEY = "nodeIds";
    public static final String KPI_CODES_key = "kpiCodes";
    public static final String IS_REALTIMEDATA_key = "isRealTimeData";
    public static final String TIME_PERIOD_key = "timePeriod";


    private final String GET_MONITOR_POINT_MSG_URL = "api/rest/open/kpiDataService/getKpiValueList";

    private MyApplication myApplication;
    private GetMonitoringPointDataView getMonitoringPointDataView;
    private ArrayList<MonitoringPointDataBean> data;

    private OkHttpUtils okHttpUtils;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RequestView.REQUEST_SUCCESS:
                    getMonitoringPointDataView.getMonitoringPointDataSuccess(data);
                    break;
                case RequestView.REQUEST_FAIL:
                    getMonitoringPointDataView.getMonitoringPointDataFail(msg.obj+"");
                    break;
            }
        }
    };

    public GetMonitoringPointDataModel(Context context, GetMonitoringPointDataView getMonitoringPointDataView){
        this.getMonitoringPointDataView = getMonitoringPointDataView;
        this.myApplication = (MyApplication)context.getApplicationContext();
        okHttpUtils = OkHttpUtils.getOkHttpUtils();
    }

    public void getMonitoringPointData(HashMap<String,Object> bodyMap){
        HashMap<String,Object> headMap = new HashMap<>();
        headMap.put("token",myApplication.getSession().getAccess_token());
        okHttpUtils.doPostAsynValueToHeader(GET_MONITOR_POINT_MSG_URL,headMap,bodyMap,this);
    }

    private void analysisMonitoringPointDataViewBean(String dataJson){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<MonitoringPointDataBean>>(){}.getType();
        data = gson.fromJson(dataJson,type);
    }


    @Override
    public void onFailure(Request request, IOException e) {
        Message msg = Message.obtain();
        msg.what = RequestView.REQUEST_FAIL;
        msg.obj = "网络错误";
        handler.sendMessage(msg);
    }

    @Override
    public void onResponse(Response response) throws IOException {
        String loginResult = response.body().string();
        try {
            JSONObject jsonObject = new JSONObject(loginResult);
            String responseCode = jsonObject.getString(RequestView.RESPONSE_CODE);
            String retMessage = "";
            Message msg = Message.obtain();
            if (RequestView.RESPONSE_SUCCESS.equals(responseCode)){
                analysisMonitoringPointDataViewBean(jsonObject.getString(RequestView.RESPONSE_DATA));
                msg.what = RequestView.REQUEST_SUCCESS;
            }else {
                retMessage = jsonObject.getString(RequestView.RESPONSE_MSG);
                msg.what = RequestView.REQUEST_FAIL;
            }
            msg.obj = retMessage;
            handler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            Message msg = Message.obtain();
            msg.what = RequestView.REQUEST_FAIL;
            msg.obj = "数据解析异常";
            handler.sendMessage(msg);
        }
    }
}
