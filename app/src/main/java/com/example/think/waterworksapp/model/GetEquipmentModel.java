package com.example.think.waterworksapp.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.think.waterworksapp.application.MyApplication;
import com.example.think.waterworksapp.base_intface.GetEquipmentView;
import com.example.think.waterworksapp.base_intface.RequestView;
import com.example.think.waterworksapp.bean.EquipmentMsgBean;
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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Think on 2016/8/22.
 */
public class GetEquipmentModel{

    private final String GET_EQUIPMENT_MSG_URL = "rest/open/maintenanceUIService/getDevicesByTeamId";

    private MyApplication myApplication;
    private GetEquipmentView getEquipmentView;
    private ICallBack<ArrayList<EquipmentMsgBean>> iCallBack;
    private OkHttpUtils okHttpUtils;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RequestView.REQUEST_SUCCESS:
                    getEquipmentView.LoadOver(iCallBack.getData());
                    break;
                case RequestView.REQUEST_FAIL:
                    getEquipmentView.LoadError(msg.obj+"");
                    break;
                case RequestView.REQUEST_LOGIN_OUT:
                    getEquipmentView.loginOut();
                    break;
            }
        }
    };

    public GetEquipmentModel(Context context,GetEquipmentView getEquipmentView){
        this.getEquipmentView = getEquipmentView;
        this.myApplication = (MyApplication)context.getApplicationContext();
        okHttpUtils = OkHttpUtils.getOkHttpUtils();
    }

    public void getEquipmentMsg(){
        TeamMsgBean teamMsgBean = myApplication.getTeamMsgBean();
        String url = GET_EQUIPMENT_MSG_URL+"?"+RequestView.TOKEN+"="+myApplication.getSession().getAccess_token();
        long teamId = teamMsgBean.getId();
        iCallBack = new ICallBack<>(handler,new TypeToken<ArrayList<EquipmentMsgBean>>(){}.getType());
        okHttpUtils.doPostAsyn(url,"["+teamId+"]",iCallBack);
    }

}
