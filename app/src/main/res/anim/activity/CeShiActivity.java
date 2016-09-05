package com.lang.big.biglang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lang.big.biglang.R;
import com.lang.big.biglang.utils.MyMapUilts;


public class CeShiActivity extends AppCompatActivity {


    private Handler mHandler;
    private MyMapUilts myMapUilts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ce_shi);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MyMapUilts.GETLOCATION_IS_OK:
                        Toast.makeText(com.lang.big.biglang.activity.CeShiActivity.this, myMapUilts.getLocal(), Toast.LENGTH_SHORT).show();
                        break;
                    case MyMapUilts.GETLOCATION_IS_ERROR:
                        Intent intent = new Intent(com.lang.big.biglang.activity.CeShiActivity.this,AreasSelectActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };
        myMapUilts = new MyMapUilts(getApplicationContext(),mHandler);
    }
}
