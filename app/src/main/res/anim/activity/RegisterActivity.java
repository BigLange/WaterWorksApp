package com.lang.big.biglang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lang.big.biglang.R;
import com.lang.big.biglang.utils.MyOkHttp_util;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

/**
 * Created by Administrator on 2016/4/21.
 */
public class RegisterActivity extends Activity {
    EditText edt_inputTel=null;
    EditText edt_verifyCode=null;//输入您的验证码
    String telNumber;//手机号码
   String  verifyCode;//验证码
    HashMap<String,Object> telMap=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_register);
       edt_inputTel= (EditText) findViewById(R.id.edt_inputTel);
       edt_verifyCode= (EditText) findViewById(R.id.edt_verifyCode);
    }



       public void getVerify(View view){//发送验证码
       telNumber=edt_inputTel.getText().toString();
           System.out.println(telNumber);
           telMap=new HashMap<>();
           telMap.put("tell", telNumber);
           verifyHistory();
   }


    public void verifyHistory(){//验证此手机号码是否已经注册过了
        MyOkHttp_util.getMyOkHttp().doPost(MyOkHttp_util.ServicePath + "VerifyTell.do", telMap, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

           @Override
           public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                   final   String   stateCode = response.body().string();
                   System.out.println("返回来的状态码"+stateCode);

                    if (stateCode.equals("404")) {
                        //404通过  之后发送短信验证码到对应号码的手机上
                        BmobSMS.requestSMSCode(com.lang.big.biglang.activity.RegisterActivity.this, telNumber, "注册验证码", new RequestSMSCodeListener() {

                            @Override
                            public void done(Integer smsId, BmobException ex) {
                                System.out.println("执行到哪里了？A");
                                // TODO Auto-generated method stub
                                 if (ex==null) {
                                    System.out.println("执行到哪里了？B");
                                    //验证码发送成功
                                    Log.i("smile", "短信id："+smsId);//用于查询本次短信发送详情
                               }
                            }
                        });
                    }else  if (stateCode.equals("500")){
                        System.out.println("500"+"状态码");
                    }else{
                        System.out.println("200"+"状态码");
                    }
                }
            }
        });

    }


    public void next(View view){//继续下一步
        verifyCode=edt_verifyCode.getText().toString();
        BmobSMS.verifySmsCode(com.lang.big.biglang.activity.RegisterActivity.this, telNumber, verifyCode, new VerifySMSCodeListener() {
            @Override
            public void done(BmobException ex) {
                // TODO Auto-generated method stub
                if (ex == null) {//短信验证码已验证成功
                    Log.i("smile", "验证通过");
                    Toast.makeText(com.lang.big.biglang.activity.RegisterActivity.this, "验证通过！请设置您的个人信息!", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(com.lang.big.biglang.activity.RegisterActivity.this, RegisterLaterActivity.class);
                    it.putExtra("telNumber", telNumber);
                    startActivity(it);
                    finish();
               } else {
                   Toast.makeText(com.lang.big.biglang.activity.RegisterActivity.this, "验证码错误，请重新输入！", Toast.LENGTH_SHORT).show();
                    Log.i("smile", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                }
            }
        });
    }

    public void back(View view){//返回
        finish();
    }

}
