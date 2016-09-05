package com.example.think.waterworksapp.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.think.waterworksapp.application.MyApplication;
import com.example.think.waterworksapp.base_intface.GetTeamMsgView;
import com.example.think.waterworksapp.base_intface.RequestView;
import com.example.think.waterworksapp.bean.Session;
import com.example.think.waterworksapp.bean.TeamMsgBean;
import com.example.think.waterworksapp.utils.OkHttpUtils;
import com.google.gson.Gson;
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
public class GetTeamModel implements Callback{


    private final String GET_TIME_MSG_URL = "rest/open/teamUIService/getTeam";

    private GetTeamMsgView getTeamMsgView;
    private Context context;
    private Session session;
    private MyApplication application;
    private OkHttpUtils okHttpUtils;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RequestView.REQUEST_SUCCESS:
                    getTeamMsgView.LoadOver();
                    break;
                case RequestView.REQUEST_FAIL:
                    getTeamMsgView.LoadError(msg.obj+"");
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
        HashMap<String,Object> requestData = new HashMap<>();
        requestData.put("token",session.getAccess_token());
        okHttpUtils.doPostAsynValueToHeader(GET_TIME_MSG_URL+session.getAccess_token(),requestData,this);
    }

    private void analysisTeamMsgBean(String data){
        Gson gson = new Gson();
        TeamMsgBean teamMsgBean = gson.fromJson(data,TeamMsgBean.class);
        application.setTeamMsgBean(teamMsgBean);
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
                analysisTeamMsgBean(jsonObject.getString(RequestView.RESPONSE_DATA));
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
