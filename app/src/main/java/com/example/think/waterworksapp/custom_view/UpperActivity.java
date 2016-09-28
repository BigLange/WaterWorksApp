package com.example.think.waterworksapp.custom_view;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.example.think.waterworksapp.utils.ActivityPreservationUtils;


/**
 * Created by Think on 2016/8/11.
 */
public abstract class UpperActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityPreservationUtils.setActivity(this);
        initView();
        initEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityPreservationUtils.cleanActivity(this);
    }

    protected abstract void initView();

    protected abstract void initEvent();

    protected abstract int getLayoutId();

    protected <T extends View>  T findView(int id){
        return (T) findViewById(id);
    }


}
