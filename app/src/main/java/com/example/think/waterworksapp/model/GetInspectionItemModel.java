package com.example.think.waterworksapp.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.think.waterworksapp.application.MyApplication;
import com.example.think.waterworksapp.base_intface.GetEquipmentView;
import com.example.think.waterworksapp.base_intface.GetInspectionItemView;
import com.example.think.waterworksapp.base_intface.RequestView;
import com.example.think.waterworksapp.bean.EquipmentMsgBean;
import com.example.think.waterworksapp.bean.InspectionItemBean;
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
 * 获得巡检项目
 */
public class GetInspectionItemModel{

    private final String GET_INSPECTION_ITEM_MSG_URL = "rest/open/maintenanceUIService / getInspectionItemsByModelId";

    private MyApplication myApplication;
    private GetInspectionItemView getInspectionItemView;
    private OkHttpUtils okHttpUtils;
    private ICallBack<ArrayList<InspectionItemBean>> iCallBack;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RequestView.REQUEST_SUCCESS:
                    getInspectionItemView.getInspectionItemSuccess(iCallBack.getData());
                    break;
                case RequestView.REQUEST_FAIL:
                    getInspectionItemView.getInspectionItemFail(msg.obj+"");
                    break;
                case RequestView.REQUEST_LOGIN_OUT:
                    getInspectionItemView.loginOut();
                    break;
            }
        }
    };

    public GetInspectionItemModel(Context context, GetInspectionItemView getInspectionItemView){
        this.getInspectionItemView = getInspectionItemView;
        this.myApplication = (MyApplication)context.getApplicationContext();
        okHttpUtils = OkHttpUtils.getOkHttpUtils();
    }

    public void getInspectionItem(long modelId){
        String url = GET_INSPECTION_ITEM_MSG_URL+"?"+RequestView.TOKEN+"="+myApplication.getSession().getAccess_token();
        iCallBack = new ICallBack<>(handler,new TypeToken<ArrayList<InspectionItemBean>>(){}.getType());
        okHttpUtils.doPostAsyn(url,"["+modelId+"]",iCallBack);
    }
}
