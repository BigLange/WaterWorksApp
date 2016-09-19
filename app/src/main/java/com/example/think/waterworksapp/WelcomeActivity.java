package com.example.think.waterworksapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.think.waterworksapp.application.MyApplication;
import com.example.think.waterworksapp.base_intface.RequestView;
import com.example.think.waterworksapp.bean.Session;
import com.example.think.waterworksapp.dialog.DialogManager;
import com.example.think.waterworksapp.model.LoginModel;
import com.example.think.waterworksapp.utils.OkHttpUtils;
import com.example.think.waterworksapp.utils.SharedPreferencesUtils;
import com.example.think.waterworksapp.utils.ToastUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


public class WelcomeActivity extends Activity implements Callback,DialogManager.DialogShowOverListener {


    public static final String SAVE_ACCESS_TOKEN = "accessToken";//保存access_token的key
    public static final String SAVE_REFRESH_TOKEN = "refreshToken";//保存refresh_token的key
    public static final String SAVE_EXPIRES_IN = "expiresIn";//保存expires_in的key

    public static final int NETWORK_ERROR = 3;//网络错误

    private final String REQUEST_PARAMETER_1 = "grant_type";
    private final String REQUEST_PARAMETER_2 = "refresh_token";

    private String Sesstion;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private String refreshToken;
//    private DialogManager dialogManager;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LoginModel.LOGIN_SUCCESS:
                    intentJump(SelectOperationActivity.class);
                    break;
                case LoginModel.LOGIN_FAIL:
                    intentJump(LoginActivity.class);
                    break;
                case NETWORK_ERROR:
//                    if (dialogManager==null)
//                        dialogManager = new DialogManager(WelcomeActivity.this);
//                    dialogManager.setDialogOverListener(WelcomeActivity.this);
//                    dialogManager.showRecordingDialog("网络错误");
                    ToastUtils.showToast(WelcomeActivity.this,"网络错误");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getToken();
        tokenLogin();
    }

    private void getToken() {
        sharedPreferencesUtils = new SharedPreferencesUtils(this,LoginModel.SHARED_PREFERENCES_TITLE_NAME);
        refreshToken = sharedPreferencesUtils.takeOutString(SAVE_REFRESH_TOKEN);
        if (refreshToken==null)
            intentJump(LoginActivity.class);
    }

    private void tokenLogin(){
        OkHttpUtils utils = OkHttpUtils.getOkHttpUtils();
        HashMap<String,Object> requestParameter = new HashMap<>();
        requestParameter.put(REQUEST_PARAMETER_1,"refresh_token");
        requestParameter.put(REQUEST_PARAMETER_2,refreshToken);
        utils.doPostAsyn("oauth2/access_token",requestParameter,this);
    }

    /**
     * 保存token
     */
    private void saveToken(Session session){
        SharedPreferencesUtils utils = new SharedPreferencesUtils(this,LoginModel.SHARED_PREFERENCES_TITLE_NAME);
        utils.preservationStringData(WelcomeActivity.SAVE_REFRESH_TOKEN,session.getRefresh_token());
        utils.preservationStringData(WelcomeActivity.SAVE_ACCESS_TOKEN,session.getAccess_token());
        utils.preservatioLongData(WelcomeActivity.SAVE_EXPIRES_IN,session.getExpires_in());
    }

    /**
     * 用于将session解析出来
     */
    private void analysisSession(String userDate) {
        Gson gson = new Gson();
        Session mSession = gson.fromJson(userDate.toString(), Session.class);
        MyApplication mApplication = (MyApplication) this.getApplicationContext();
        mApplication.setSession(mSession);
        saveToken(mSession);
    }

    @Override
    public void onFailure(Request request, IOException e) {
       handler.sendEmptyMessage(NETWORK_ERROR);
    }

    @Override
    public void onResponse(Response response) throws IOException {
        String loginResult = response.body().string();
        Log.e("LoginToken",loginResult);
        try {
            JSONObject loginJson = new JSONObject(loginResult);
            String retCode = loginJson.getString(RequestView.RESPONSE_CODE);
            String retMessage = "";
            Message msg = Message.obtain();
            if (RequestView.RESPONSE_SUCCESS.equals(retCode)) {
                analysisSession(loginJson.getString(RequestView.RESPONSE_DATA));
                retMessage = "登录成功";
                msg.what = LoginModel.LOGIN_SUCCESS;
            }else{
                retMessage = loginJson.getString(RequestView.RESPONSE_MSG);
                msg.what = LoginModel.LOGIN_FAIL;
            }
            msg.obj = retMessage;
            handler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            Message msg = Message.obtain();
            msg.what = LoginModel.LOGIN_FAIL;
            msg.obj = "数据解析异常";
            handler.sendMessage(msg);
        }


    }

    private void intentJump(Class<?> selectPersonnelActivityClass) {
        Intent intent = new Intent(this,selectPersonnelActivityClass);
        startActivity(intent);
        finish();
    }



    @Override
    public void dialogShowOver() {
        finish();
    }
}
