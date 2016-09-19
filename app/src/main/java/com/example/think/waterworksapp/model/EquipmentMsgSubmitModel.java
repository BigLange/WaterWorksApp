package com.example.think.waterworksapp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.think.waterworksapp.application.MyApplication;
import com.example.think.waterworksapp.base_intface.EquipmentMsgSubmitView;
import com.example.think.waterworksapp.base_intface.RequestView;
import com.example.think.waterworksapp.utils.ImageLoad;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Think on 2016/8/23.
 */
public class EquipmentMsgSubmitModel implements Callback{

    private final String UP_LOAD_MSG_URL = "rest/upload/maintenanceUIService/addInspectionRecord";

    private final String DEVICE_ID = "deviceId";
    private final String DEVICE_STATUS = "deviceStatus";
    private final String SUBMIT_DATA_INSPCE = "submitDataByInputKey";
    private final String INSPECTION_DATE = "inspectionDate";
    private final String INSPECTOIN_CONTEXT = "inspectionContext";
    private final String IMAGE_URL = "imageUrl";

    private OkHttpUtils okHttpUtils;
    private EquipmentMsgSubmitView equipmentMsgSubmitView;
    private MyApplication application;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RequestView.REQUEST_SUCCESS:
                    equipmentMsgSubmitView.submitSuccess();
                    break;
                case RequestView.REQUEST_FAIL:
                    equipmentMsgSubmitView.submitFail(msg.obj+"");
                    break;
                case RequestView.REQUEST_LOGIN_OUT:
                    equipmentMsgSubmitView.loginOut();
            }
        }
    };

    public EquipmentMsgSubmitModel(Context context, EquipmentMsgSubmitView equipmentMsgSubmitView){
        application = (MyApplication) context.getApplicationContext();
        this.equipmentMsgSubmitView = equipmentMsgSubmitView;
        okHttpUtils = OkHttpUtils.getOkHttpUtils();
    }


    public void submitMsg(){
        String url =UP_LOAD_MSG_URL +"?"+RequestView.TOKEN+"="+application.getSession().getAccess_token();
        ArrayList<String> imgUrls = equipmentMsgSubmitView.getEquipmentImageInformation();
        ArrayList<Bitmap> upLoadBitmaps = ImageLoad.getImageLoad().fromPathGetBitmap(imgUrls);
        Map<String,Boolean> equipmentInformationMap = equipmentMsgSubmitView.getEquipmentInformation();
        String equipmentInformationStr = uoLoadDataToJson(equipmentInformationMap);
        String equipmentDetailedInformationStr = equipmentMsgSubmitView.getEquipmentDetailedInformation();
        HashMap<String,Object> dataMap = new HashMap();
        dataMap.put(DEVICE_ID,equipmentMsgSubmitView.getDeviceID());
        dataMap.put(DEVICE_STATUS,equipmentInformationStr);
        dataMap.put(INSPECTION_DATE,getCurrentSimpleData());
        dataMap.put(INSPECTOIN_CONTEXT,equipmentDetailedInformationStr);
        okHttpUtils.upLoadImg(url,upLoadBitmaps,IMAGE_URL,dataMap,this);
    }




    private String uoLoadDataToJson(Map<String,Boolean> data){
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String,Boolean>>(){}.getType();
        return gson.toJson(data,type);
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
            Log.e("submit",responseCode);
            Message msg = Message.obtain();
            if (RequestView.RESPONSE_SUCCESS.equals(responseCode)){
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


    private String getCurrentSimpleData(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
