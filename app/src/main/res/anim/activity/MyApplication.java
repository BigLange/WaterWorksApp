package com.lang.big.biglang.activity;

import android.app.Application;

import com.lang.big.biglang.bean.User;

/**
 * Created by Administrator on 2016/4/25.
 */
public class MyApplication extends Application {
   static com.lang.big.biglang.activity.MyApplication myApp;

    public static com.lang.big.biglang.activity.MyApplication getMyApp(){
        if (myApp == null) {
            synchronized (com.lang.big.biglang.activity.MyApplication.class) {
                if (myApp == null) {
                    myApp = new com.lang.big.biglang.activity.MyApplication();
                }
            }
        }
        return  myApp;
    };


    User myUser;
    public User getMyUser() {
        return myUser;
    }

    public void setMyUser(User myUser) {
        this.myUser = myUser;
    }



    @Override
    public void onCreate() {
        super.onCreate();
    }
//    当终止应用程序对象时调用，不保证一定被调用，当程序是被内核终止以便为其他应用程序释放资源，那
//    么将不会提醒，并且不调用应用程序的对象的onTerminate方法而直接终止进 程
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
//当后台程序已经终止资源还匮乏时会调用这个方法
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }



}
