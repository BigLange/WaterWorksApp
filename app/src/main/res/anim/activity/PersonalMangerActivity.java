package com.lang.big.biglang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.lang.big.biglang.Adapter.PersonalMgrAdapter;
import com.lang.big.biglang.MyView.CircleView;
import com.lang.big.biglang.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/4/20.
 */
public class PersonalMangerActivity extends Activity implements View.OnClickListener {//个人管理中心
    ImageView imv_back;
    ImageView imv_edit;
    ImageView imv_share;
    ListView  lv_personalconditio;
    CircleView person_head=null;
    Intent it=null;
    PersonalMgrAdapter adapter;
    ArrayList<HashMap<String,Object>> arydatas=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalmanger);
        initView();
        loaddatas();
        adapter =new PersonalMgrAdapter(com.lang.big.biglang.activity.PersonalMangerActivity.this,arydatas);
        lv_personalconditio.setAdapter(adapter);
    }

    private void loaddatas() {
        String[] name1 = {"非常好","verygood","perpect","nice","上无古人后来者！"};
        Object[] names= {name1};
        for (int i=0;i<names.length;i++){
            HashMap<String,Object> rowMap=new HashMap<>();
            rowMap.put("comment",names[i]);
            arydatas.add(rowMap);
        }
    }


    private void initView(){//初始化控件
        imv_back= (ImageView) findViewById(R.id.imv_back);
        imv_edit= (ImageView) findViewById(R.id.imv_edit);
        imv_share= (ImageView) findViewById(R.id.imv_share);
        person_head= (CircleView) findViewById(R.id.head_picture);
        System.out.println("GG"+person_head);
       // person_head.setImageResource(R.drawable.q1);//设置头像

        lv_personalconditio= (ListView) findViewById(R.id.lv_personalcondition);
        imv_back.setOnClickListener(this);
        imv_edit.setOnClickListener(this);
        imv_share.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imv_back:
                finish();
                break;
            case R.id.imv_edit:
                it=new Intent(this, com.lang.big.biglang.activity.PersonInfoActivity.class);
                startActivity(it);
                break;
            case R.id.imv_share:

                break;
        }
    }
}
