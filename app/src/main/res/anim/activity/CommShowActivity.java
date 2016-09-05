package com.lang.big.biglang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.lang.big.biglang.Adapter.CommShowViewPageAdapter;
import com.lang.big.biglang.R;
import com.lang.big.biglang.bean.Commodity;

public class CommShowActivity extends Activity  implements ViewPager.OnPageChangeListener{

    private ImageButton upNext;
    private ImageButton weiXin;
    private ImageButton kongJian;
    private LinearLayout xiangYaoLayout;
    private LinearLayout lianXiLayout;
    private Button buyBtn;
    private ViewPager viewPager;

    private CommShowViewPageAdapter mAdapter;

    private Commodity mCommodity;

    private int dirCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_show);
        initValue();
        initView();
    }

    private void initView() {
        upNext = (ImageButton)findViewById(R.id.comm_show_head_up_img_btn);
        weiXin = (ImageButton)findViewById(R.id.comm_show_head_weixin_img_btn);
        kongJian = (ImageButton)findViewById(R.id.comm_show_head_kongjian_img_btn);
        xiangYaoLayout = (LinearLayout)findViewById(R.id.comm_show_botton_xiangyao_layout);
        lianXiLayout = (LinearLayout)findViewById(R.id.comm_show_botton_lianxi_layout);
        buyBtn = (Button)findViewById(R.id.comm_show_botton_buy);
        viewPager = (ViewPager)findViewById(R.id.comm_show_center_viewPagr);
        mAdapter = new CommShowViewPageAdapter(this,mCommodity.getAbsolutePath());
        dirCount = mCommodity.getImgPaths().length;
        viewPager.setAdapter(mAdapter);
    }

    public void initValue() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mCommodity = bundle.getParcelable("comm");
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
