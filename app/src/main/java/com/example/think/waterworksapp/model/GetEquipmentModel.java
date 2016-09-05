package com.example.think.waterworksapp.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.think.waterworksapp.application.MyApplication;
import com.example.think.waterworksapp.base_intface.GetEquipmentView;
import com.example.think.waterworksapp.base_intface.RequestView;
import com.example.think.waterworksapp.bean.EquipmentMsgBean;
import com.example.think.waterworksapp.bean.TeamMsgBean;
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
public class GetEquipmentModel implements Callback{

    private final String GET_EQUIPMENT_MSG_URL = "rest/open/teamUIService/getDevicesByTeamId";

    private MyApplication myApplication;
    private GetEquipmentView getEquipmentView;
    private ArrayList<EquipmentMsgBean> dataArr;
    private OkHttpUtils okHttpUtils;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RequestView.REQUEST_SUCCESS:
                    getEquipmentView.LoadOver(dataArr);
                    break;
                case RequestView.REQUEST_FAIL:
                    getEquipmentView.LoadError(msg.obj+"");
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
        HashMap<String,Object> headMap = new HashMap<>();
        headMap.put("token",myApplication.getSession().getAccess_token());
        HashMap<String,Object> bodyMap = new HashMap<>();
        TeamMsgBean teamMsgBean = myApplication.getTeamMsgBean();
        bodyMap.put("teamId",teamMsgBean.getId());
        okHttpUtils.doPostAsynValueToHeader(GET_EQUIPMENT_MSG_URL,headMap,bodyMap,this);
    }

    private void analysisEquipmentMsgBean(String data){
        Gson gson = new Gson();
        Type dataType = new TypeToken<ArrayList<EquipmentMsgBean>>(){}.getType();
        dataArr = gson.fromJson(data,dataType);
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
                analysisEquipmentMsgBean(jsonObject.getString(RequestView.RESPONSE_DATA));
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
