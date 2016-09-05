package com.lang.big.biglang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lang.big.biglang.Adapter.ImgPreViewPageAdapter;
import com.lang.big.biglang.R;

import java.util.ArrayList;

public class ImagePreview extends Activity implements ViewPager.OnPageChangeListener{

    private ViewPager mViewPage;
    private TextView imageNumber;
    private Button deleteBtn;

    private int dirCount;
    private int index;

    private ArrayList<String> mDirPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        initValue();
        initView();
        initEvent();
    }

    private void initEvent() {
        mViewPage.setOnPageChangeListener(this);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("index", index);
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.my_null, R.anim.img_pre_close);
            }
        });
    }

    //用于加载数据
    private void initValue() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            mDirPath = (ArrayList<String>)bundle.getSerializable("dirPaths");
            index = bundle.getInt("index");
            dirCount = mDirPath.size();
        }
    }

    private void initView() {
        mViewPage = (ViewPager)findViewById(R.id.pre_image_show_viewPage);
        ImgPreViewPageAdapter imgPreAdapter = new ImgPreViewPageAdapter(this,mDirPath);
        mViewPage.setAdapter(imgPreAdapter);
        mViewPage.setCurrentItem(index);
        imageNumber = (TextView)findViewById(R.id.pre_image_number_txt);
        imageNumber.setText((index+1)+"/"+dirCount);
        deleteBtn = (Button)findViewById(R.id.pre_image_delete_btn);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        index = position;
        imageNumber.setText((index+1)+"/"+dirCount);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.my_null, R.anim.img_pre_close);
    }
}
