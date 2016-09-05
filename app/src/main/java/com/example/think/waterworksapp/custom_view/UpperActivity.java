package com.example.think.waterworksapp.custom_view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


/**
 * Created by Think on 2016/8/11.
 */
public abstract class UpperActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initEvent();
    }


    protected abstract void initView();

    protected abstract void initEvent();

    protected abstract int getLayoutId();

    protected <T extends View>  T findView(int id){
        return (T) findViewById(id);
    }

}
