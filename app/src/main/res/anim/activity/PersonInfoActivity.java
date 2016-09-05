package com.lang.big.biglang.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.lang.big.biglang.Adapter.PersonalInfoAdapter;
import com.lang.big.biglang.R;
import com.lang.big.biglang.utils.Util_TitleandBack;

/**
 * Created by Administrator on 2016/5/1.
 */
public class PersonInfoActivity extends Activity implements View.OnClickListener {
    ListView lv_personInfo=null;
    PersonalInfoAdapter adapter=null;
    Util_TitleandBack personInfo_titleAndBack=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personinfo);
        initView();
        adapter=new PersonalInfoAdapter();
        lv_personInfo.setAdapter(adapter);
    }

    private void initView() {
        lv_personInfo= (ListView) findViewById(R.id.lv_personInfo);
        personInfo_titleAndBack= (Util_TitleandBack) findViewById(R.id.personInfo_titleAndBack);
        personInfo_titleAndBack.setbackListener(this);
        personInfo_titleAndBack.setTitle("个人资料");
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
