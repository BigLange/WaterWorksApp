package com.example.think.waterworksapp.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/4/16.
 */
public class MyThreadPool {

    private ExecutorService pool;
    private static MyThreadPool myThreadPool;

    private MyThreadPool(){
        pool = Executors.newCachedThreadPool();
    }

    public static MyThreadPool getMyThreadPool(){
        if(myThreadPool==null){
            myThreadPool = new MyThreadPool();
        }
        return myThreadPool;
    }

    public ExecutorService getPool() {
        return pool;
    }
}
