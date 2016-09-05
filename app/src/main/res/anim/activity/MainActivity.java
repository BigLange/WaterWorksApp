package com.lang.big.biglang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.lang.big.biglang.R;
import com.lang.big.biglang.utils.MyThreadPool;

import java.util.concurrent.ExecutorService;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button index_jume;
    private Handler mHandler;
    private int num = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        index_jume = (Button)findViewById(R.id.id_index_jump);
        index_jume.setOnClickListener(this);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1) {
                    index_jume.setText(num + "秒后跳转,点击马上进入");
                }else{
                    jump();
                }
            }
        };


    }

    @Override
    protected void onResume() {
        super.onResume();
        ExecutorService pool = MyThreadPool.getMyThreadPool().getPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                while (num>0) {
                    num--;
                    mHandler.sendEmptyMessage(1);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.sendEmptyMessage(2);
            }
        });
    }

    @Override
    public void onClick(View v) {
        num = 0;
    }

    public void jump(){
        Intent intent = new Intent(this,homepage.class);
        startActivity(intent);
        this.finish();
    }
}
