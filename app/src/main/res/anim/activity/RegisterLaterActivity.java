package com.lang.big.biglang.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.lang.big.biglang.R;
import com.lang.big.biglang.utils.Util_TitleandBack;

/**
 * Created by Administrator on 2016/4/28.
 */
public class RegisterLaterActivity extends Activity implements View.OnClickListener {
    Util_TitleandBack later_backAndTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_later);
        initView();
    }



    private void initView() {
   later_backAndTitle= (Util_TitleandBack) findViewById(R.id.later_backAndTitle);
   later_backAndTitle.setbackListener(this);
   later_backAndTitle.setTitle("设置个人信息");
    }


    @Override
    public void onClick(View v) {
     finish();
    }
}
