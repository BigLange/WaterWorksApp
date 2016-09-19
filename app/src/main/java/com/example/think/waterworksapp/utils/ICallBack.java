package com.example.think.waterworksapp.utils;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.think.waterworksapp.base_intface.RequestView;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by Think on 2016/9/13.
 */

public class ICallBack<T> implements Callback {

    private T data;
    private Handler handler;
    private Type type;

    public ICallBack(Handler handler,Type type){
        this.handler = handler;
        this.type = type;
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
                analysisData(jsonObject.getString(RequestView.RESPONSE_DATA));
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


    public T getData(){
//        if (type.toString().equals("java.util.ArrayList<com.example.think.waterworksapp.bean.AcquisitionSensorDataBean>"))
            Log.e("getData",data+type.toString());
        return data;
    }
}
