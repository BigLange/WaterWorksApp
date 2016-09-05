package com.lang.big.biglang.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lang.big.biglang.R;
import com.lang.big.biglang.bean.User;
import com.lang.big.biglang.utils.MyOkHttp_util;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/4/21.
 */
public class LoginActivity extends Activity implements View.OnClickListener{
    EditText edt_username;
    EditText edt_pwd;
    Button btn_login;
    TextView tv_quit;
    TextView tv_register;
    private SharedPreferences pref;//用于记录登录字段，记住登录用户信息
    private SharedPreferences.Editor editor;
    MyApplication myApp=null;//用于保存登录成功的用户信息！
    JSONObject jsonObj=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register);
        initView();
    }

    @Override
    public void onClick(View v) {
 switch (v.getId()){
     case R.id.btn_login :
         doLogin();//向服务发送登录请求
         break;
     case R.id.tv_quit:
         finish();//退出
         break;
     case R.id.tv_register:
         Intent it=new Intent(com.lang.big.biglang.activity.LoginActivity.this,RegisterActivity.class);//跳转到注册
         startActivity(it);
         break;
       }
    }



    private String utilCheck(){//用户名，密码的合法性判断
        String str=null;
        String username=edt_username.getText().toString();
        String password=edt_pwd.getText().toString();

        if (username.length()<20&&username.length()>=6){
            if (password.length()>=6&&password.length()<=16) {
            }else{
                show("密码不能大于20位小于6位！");
            }
        }else{
            show("用户名不能大于20位小于6位！");
        }
        str=username+","+password;
        return str;
    }

    private void initView(){
    edt_username= (EditText) findViewById(R.id.edt_username);
    edt_pwd= (EditText) findViewById(R.id.edt_pwd);
    btn_login= (Button) findViewById(R.id.btn_login);
    tv_quit= (TextView) findViewById(R.id.tv_quit);
    tv_register= (TextView) findViewById(R.id.tv_register);
    btn_login.setOnClickListener(this);
    tv_quit.setOnClickListener(this);
    tv_register.setOnClickListener(this);
}
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 500:
                    show("账号或密码错误！");
                    break;
                case 404:
                    show("账号或密码错误！");
                    break;
            }
        }
    };

private void saveUserInfo(String msg){
    //解析Json数据
    String userInfo= null;
    try {
        userInfo = jsonObj.get(msg).toString();
        System.out.println(userInfo);
        Gson gson=new Gson();
        User user=gson.fromJson(userInfo,User.class);
        myApp= (MyApplication) getApplication();
        myApp.setMyUser(user);
        editor.putString("userInfo",userInfo);
        editor.commit();
        finish();
    } catch (JSONException e) {
        e.printStackTrace();
    }

}

public void show(String msg){//显示消息使用！
    Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
}

      private void doLogin(){
      final   String[]  userInfo=utilCheck().split(",");
        HashMap<String,Object> userInfoMap=new HashMap<>();
        userInfoMap.put("username",userInfo[0]);
        userInfoMap.put("pwd",userInfo[1]);
        MyOkHttp_util.getMyOkHttp().doPost(MyOkHttp_util.ServicePath+"Login.do", userInfoMap, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()){
                    String jsonStr=response.body().string();
                    System.out.println("服务器的返回的数据:"+jsonStr);
//                      {"stateCode":200,
//                        "user":{"id":57,"userName":"阿浪哥","uSex":"男",
//                        "uemial":"839653288@qq.com","uArea":"湖南长沙",
//                        "uTell":"18874803397","uAddress":"雨花区，香樟路",
//                        "udate":"四月 24, 2016"}
//                }
//404：出现错误用户提交的数据错误
//500:用户无注册，数据库没有字段
//200：请求成功
                        try {
                            jsonObj=new JSONObject(jsonStr);
                            String state=jsonObj.get("stateCode").toString();
                            int stateCode=Integer.parseInt(state);
                            switch (stateCode){
                                case 200:
                                    Log.w("f", "success!");
                                    pref= PreferenceManager.getDefaultSharedPreferences(com.lang.big.biglang.activity.LoginActivity.this);
                                    editor=pref.edit();
                                    //登录成功之后将用户信息保存到SharedPreferences里面
                                    //当用户登录成功之后将用户信息保存在Application中
                                    editor.putBoolean("judge", false);
                                    editor.putString("account", userInfo[0]);
                                    editor.putString("password", userInfo[1]);
                                    editor.commit();//保存现今的用户信息
                                    saveUserInfo("user");
                                    break;
                                case 500:
                                    Log.w("f", "user not exist!");
                                    Message msg500=new Message();
                                    msg500.what=500;
                                    handler.sendMessage(msg500);
                                    break;
                                case 404:
                                    Message msg404=new Message();
                                    msg404.what=500;
                                    Log.w("f", "data mistake!");
                                    handler.sendMessage(msg404);
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        throw new IOException("GUnexpected code " + response);
                    }
                }

        });
    }
}
