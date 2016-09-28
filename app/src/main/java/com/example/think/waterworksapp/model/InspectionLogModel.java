package com.example.think.waterworksapp.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.think.waterworksapp.application.MyApplication;
import com.example.think.waterworksapp.base_intface.InspectionLogView;
import com.example.think.waterworksapp.base_intface.RequestView;
import com.example.think.waterworksapp.bean.AcquisitionSensorDataBean;
import com.example.think.waterworksapp.bean.InspectionLogBean;
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
 * Created by Think on 2016/9/26.
 */

public class InspectionLogModel implements Callback{
    private final String GET_INSPECTION_LOG_URL = "rest/open/maintenanceUIService/getAllInspectionRecordsWithPage";

    private MyApplication myApplication;
    private InspectionLogView inspectionLogView;
    private OkHttpUtils okHttpUtils;
    private ArrayList<InspectionLogBean> data;
//    private ICallBack<ArrayList<InspectionLogBean>> iCallBack;
    private Type type;
    public static final int ROWS = 10;
    private int page = 0;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RequestView.REQUEST_SUCCESS:
                    inspectionLogView.LoadOver(data);
                    break;
                case RequestView.REQUEST_FAIL:
                    inspectionLogView.LoadError(msg.obj+"");
                    break;
                case RequestView.REQUEST_LOGIN_OUT:
                    inspectionLogView.loginOut();
                    break;
            }
        }
    };

    public InspectionLogModel(Context context, InspectionLogView inspectionLogView){
        this.inspectionLogView = inspectionLogView;
        this.myApplication = (MyApplication)context.getApplicationContext();
        okHttpUtils = OkHttpUtils.getOkHttpUtils();
        type = new TypeToken<ArrayList<InspectionLogBean>>(){}.getType();
    }


    public void getInspectionLog(){
        sendRequest(page);
        page++;
    }

    public void getInspectionLog(int page){
        this.page = page;
        sendRequest(page);
        page++;
    }

    private void sendRequest(int page){
        String url = GET_INSPECTION_LOG_URL+"?"+RequestView.TOKEN+"="+myApplication.getSession().getAccess_token();
        okHttpUtils.doPostAsyn(url,"["+page+","+ROWS+"]",this);
    }

    private void analysisData(String jsonStr){
        Gson gson = new Gson();
        data = gson.fromJson(jsonStr,type);
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
        String getTeamData = response.body().string();
        Log.e(type.toString(),getTeamData);
        try {
            JSONObject jsonObject = new JSONObject(getTeamData);
            String responseCode = jsonObject.getString(RequestView.RESPONSE_CODE);
            String retMessage = "";
            Message msg = Message.obtain();
            if (RequestView.RESPONSE_SUCCESS.equals(responseCode)){
                JSONObject dataJsonObj = jsonObject.getJSONObject(RequestView.RESPONSE_DATA);
                analysisData(dataJsonObj.getString("content"));
                msg.what = RequestView.REQUEST_SUCCESS;
            }else if (RequestView.RESPINSE_LOGIN_OUT.equals(responseCode)){
                msg.what = RequestView.REQUEST_LOGIN_OUT;
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
