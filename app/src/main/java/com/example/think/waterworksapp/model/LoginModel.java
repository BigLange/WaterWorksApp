package com.example.think.waterworksapp.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.think.waterworksapp.WelcomeActivity;
import com.example.think.waterworksapp.application.MyApplication;
import com.example.think.waterworksapp.base_intface.LoginView;
import com.example.think.waterworksapp.base_intface.RequestView;
import com.example.think.waterworksapp.bean.MonitoringPointDataBean;
import com.example.think.waterworksapp.bean.Session;
import com.example.think.waterworksapp.dialog.DialogManager;
import com.example.think.waterworksapp.dialog.SimperDialog;
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

/**
 * Created by Think on 2016/6/21.
 */
public class LoginModel implements Callback {
    private final String LOGIN_URL="oauth2/access_token?";
    public static final String SHARED_PREFERENCES_TITLE_NAME = "login_token_key";

    private String userName;

    public static final int LOGIN_SUCCESS = 0;//登录成功状态标识
    public static final int LOGIN_FAIL = 1;//登录失败状态标识


    private LoginView mView;
    private Context context;
    private OkHttpUtils mHttpUtils;
    private SimperDialog mDialog;
//    private DialogManager dm;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mDialog.hideDialog();
            switch (msg.what){
                case LOGIN_SUCCESS:
                    mView.jumpIntent();
                    break;
                case LOGIN_FAIL:
                    mView.loginError(msg.obj+"");
            }
        }
    };

    public LoginModel(LoginView loginView, Context context){
        mView = loginView;
        this.context = context;
//        dm = new DialogManager(context);
    }

    /**
     * 这这个方法用于检测用户输入数据是否正确同时直接进入login方法
     */
    public void loginInspect(){
        userName = mView.getUserNameValue();
        String passWord = mView.getPassWordValues();
        if(userName==null){
            ToastUtils.showToast(context,"请输入账号");
            return;
        }

        if (passWord==null){
            ToastUtils.showToast(context,"请输入密码");
            return;
        }

        if(userName.length()<6){
            ToastUtils.showToast(context,"账号长度过短");
            return;
        }

        if(passWord.length()<6){
            ToastUtils.showToast(context,"密码长度过短");
            return;
        }

        login(userName,passWord);
    }


    private void login(String userName,String passWord){
        mDialog =  new SimperDialog(context).setContent("正在登录,请稍后...");
        mDialog.showDialog();
        HashMap<String,Object> loginMap = new HashMap<>();
        loginMap.put("grant_type","password");
        loginMap.put("username",userName);
        loginMap.put("password",passWord);
        if(mHttpUtils==null)
            mHttpUtils = OkHttpUtils.getOkHttpUtils();
        mHttpUtils.doPostAsyn(LOGIN_URL,loginMap,this);
    }

    /**
     * 保存token
     */
    private void saveToken(Session session){
        SharedPreferencesUtils utils = new SharedPreferencesUtils(context,SHARED_PREFERENCES_TITLE_NAME);
        utils.preservationStringData(WelcomeActivity.SAVE_REFRESH_TOKEN,session.getRefresh_token());
        utils.preservationStringData(WelcomeActivity.SAVE_ACCESS_TOKEN,session.getAccess_token());
        utils.preservatioLongData(WelcomeActivity.SAVE_EXPIRES_IN,session.getExpires_in());
    }

    /**
     * 用于将session解析出来
     */
    private void analysisSession(String userDate) {
        Gson gson = new Gson();
        Session mSession = gson.fromJson(userDate, Session.class);
        MyApplication mApplication = (MyApplication) context.getApplicationContext();
        mApplication.setSession(mSession);
        saveToken(mSession);
    }


    /**
     * 下面两个方法是OKHttp请求的回调
     * @param request
     * @param e
     */
    @Override
    public void onFailure(Request request, IOException e) {
        Message msg = Message.obtain();
        msg.what = LOGIN_FAIL;
        msg.obj = "网络错误";
        handler.sendMessage(msg);
    }

    @Override
    public void onResponse(Response response) throws IOException {
        String loginResult = response.body().string();
        Log.e("login",loginResult+"namemsmdhiahidahidsahi");
        try {
            JSONObject loginJson = new JSONObject(loginResult);
            String retCode = loginJson.getString(RequestView.RESPONSE_CODE);
            String retMessage = "";
            Message msg = Message.obtain();
            if (RequestView.RESPONSE_SUCCESS.equals(retCode)) {
                analysisSession(loginJson.getString(RequestView.RESPONSE_DATA));
                retMessage = "登录成功";
                msg.what = LOGIN_SUCCESS;
            }else{
                retMessage = loginJson.getString(RequestView.RESPONSE_MSG);
                msg.what = LOGIN_FAIL;
            }
            msg.obj = retMessage;
            handler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            Message msg = Message.obtain();
            msg.what = LOGIN_FAIL;
            msg.obj = "数据解析异常";
            handler.sendMessage(msg);
        }
    }



}
