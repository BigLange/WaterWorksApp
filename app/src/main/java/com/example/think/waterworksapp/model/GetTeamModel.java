package com.example.think.waterworksapp.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.think.waterworksapp.application.MyApplication;
import com.example.think.waterworksapp.base_intface.GetTeamMsgView;
import com.example.think.waterworksapp.base_intface.RequestView;
import com.example.think.waterworksapp.bean.Session;
import com.example.think.waterworksapp.bean.TeamMsgBean;
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
import java.util.HashMap;

/**
 * Created by Think on 2016/8/22.
 */
public class GetTeamModel{


    private final String GET_TIME_MSG_URL = "rest/open/maintenanceUIService/getTeam";

    private GetTeamMsgView getTeamMsgView;
    private Context context;
    private Session session;
    private MyApplication application;
    private OkHttpUtils okHttpUtils;
    private ICallBack<TeamMsgBean> iCallBack;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RequestView.REQUEST_SUCCESS:
                    application.setTeamMsgBean(iCallBack.getData());
                    getTeamMsgView.LoadOver();
                    break;
                case RequestView.REQUEST_FAIL:
                    getTeamMsgView.LoadError(msg.obj+"");
                    break;
                case RequestView.REQUEST_LOGIN_OUT:
                    getTeamMsgView.loginOut();
                    break;
            }
        }
    };

    public GetTeamModel(Context context, GetTeamMsgView view){
        this.context = context;
        application = (MyApplication)context.getApplicationContext();
        session = application.getSession();
        this.getTeamMsgView = view;
        okHttpUtils = OkHttpUtils.getOkHttpUtils();
    }


    public void getTeamMsg(){
        if (session==null)
            return;
        String url = GET_TIME_MSG_URL+"?token="+session.getAccess_token();
        iCallBack = new ICallBack<>(handler,new TypeToken<TeamMsgBean>(){}.getType());
        okHttpUtils.doPostAsyn(url,"[]",iCallBack);
    }




}
