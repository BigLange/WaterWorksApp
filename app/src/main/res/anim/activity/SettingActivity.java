package com.lang.big.biglang.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lang.big.biglang.R;
import com.lang.big.biglang.utils.Util_TitleandBack;

/**
 * Created by Administrator on 2016/5/2.
 */
public class SettingActivity extends Activity implements View.OnClickListener {
    Util_TitleandBack util_setting;
    TextView util_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        util_setting= (Util_TitleandBack) findViewById(R.id.util_setting);
        util_right= (TextView) findViewById(R.id.util_right);
        util_setting.settingRight(View.INVISIBLE);
        util_setting.setTitle("设置");
        util_right.setOnClickListener(this);
        util_setting.setbackListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.util_back:
                finish();
                break;
    }
}
}
