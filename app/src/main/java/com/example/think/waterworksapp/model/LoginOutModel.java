package com.example.think.waterworksapp.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.think.waterworksapp.application.MyApplication;
import com.example.think.waterworksapp.base_intface.GetMonitoringPointDataView;
import com.example.think.waterworksapp.base_intface.LoginOutView;
import com.example.think.waterworksapp.base_intface.RequestView;
import com.example.think.waterworksapp.utils.OkHttpUtils;
import com.example.think.waterworksapp.utils.SharedPreferencesUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Think on 2016/8/23.
 */
public class LoginOutModel implements Callback{

    private final String LOGIN_OUT_URL = "api/rest/open/kpiDataService/getKpiValueList";

    private MyApplication myApplication;
    private LoginOutView loginOutView;
    private OkHttpUtils okHttpUtils;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RequestView.REQUEST_SUCCESS:
                    loginOutView.LoadOver();
                    break;
                case RequestView.REQUEST_FAIL:
                    loginOutView.LoadError(msg.obj+"");
                    break;
            }
        }
    };

    public LoginOutModel(Context context, LoginOutView loginOutView){
        this.loginOutView = loginOutView;
        this.myApplication = (MyApplication)context.getApplicationContext();
        okHttpUtils = OkHttpUtils.getOkHttpUtils();
    }

    public void loginOut(){
        okHttpUtils.doGetAsyn(LOGIN_OUT_URL,this);
        deleteToken();
    }

    private void deleteToken() {
        myApplication.setSession(null);
        SharedPreferencesUtils uitls = new SharedPreferencesUtils(myApplication,LoginModel.SHARED_PREFERENCES_TITLE_NAME);
        uitls.deleteString();
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
