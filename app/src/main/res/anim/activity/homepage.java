package com.lang.big.biglang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lang.big.biglang.R;
import com.lang.big.biglang.bean.Commodity;
import com.lang.big.biglang.bean.ShopClass;
import com.lang.big.biglang.fragment.ClassificationFragment;
import com.lang.big.biglang.fragment.HgFragment;
import com.lang.big.biglang.fragment.IngLoadFragment;
import com.lang.big.biglang.fragment.MessagesFragment;
import com.lang.big.biglang.fragment.ReleaseFragment;
import com.lang.big.biglang.fragment.TuiJianFragment;
import com.lang.big.biglang.utils.Login_util;
import com.lang.big.biglang.utils.MyAnimation;

public class homepage extends FragmentActivity implements View.OnClickListener {

    private Button btn_hg;
    private Button btn_classification;
    private Button btn_dope;
    private Button btn_release;
    private ImageButton imgb_add_shop;
    private Button btn_album;
    private Button btn_cim;

    //加载成功
    public final static int LOADED = 112;
    //正在加载
    public final static int BEGING_LOADED = 111;
    //加载失败(算是网络异常)
    public final static int FAILED_TO_LOAD = 110;
    //还没有加载(没有点击到那个上面去)
    public final static int NO_LOAD = 109;

    //代表着正在加载商品分类的fragment的代码
    public final static int CASSFRAGMENTLOAD = 1111;
    //...
    public final static int MSGFRAGMENTLOAD = 1112;
    public final static int RELEFRAGMENTLOAD = 1113;

    //进入到商品详情界面的返回码
    private static final int COMMSHOWRESULTCODE = 9527;


    private Button currentClickBtn;

    private View layout_select_img_fg;
    private View layout_select_img_bs;

    private HgFragment fragment_hg;
    private ClassificationFragment fragment_canss;
    private MessagesFragment fragment_message;
    private ReleaseFragment fragment_relese;
    private IngLoadFragment ingLoadFragment;


    //第二个商品分类fragment的状态
    private int cass_fragment_state = NO_LOAD;
    //第三个购物消息fragment的状态
    private int msg_fragment_state = NO_LOAD;
    //第四个用户个人信息的fragment状态
    private int rele_fragment_state = NO_LOAD;

    //代表当前正在加载的页面对应的编号
    private int currentFragmentNumber;


    private ShopClass shopClass2;


    private Fragment currentFragment;

    private FragmentManager mFManaage;
    private FragmentTransaction mFTransaction;

    private boolean isSelectImg;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == LOADED) {
                //判断，返回来的是数据加载完毕
                switch (msg.arg1) {
                    case CASSFRAGMENTLOAD:
                        //如果当前是商品分类的fragment加载完成了的话，就进行对应的操作
                        fragment_canssLoad();
                        break;
                    case MSGFRAGMENTLOAD:
                        //...
                        break;
                    case RELEFRAGMENTLOAD:
                        break;
                }

            } else if (msg.what == FAILED_TO_LOAD) {
                //或者是数据加载异常
                switch (msg.arg1) {
                    case CASSFRAGMENTLOAD:
                        Toast.makeText(com.lang.big.biglang.activity.homepage.this, "网络请求出错,加载失败", Toast.LENGTH_SHORT).show();
                        break;
                    case MSGFRAGMENTLOAD:
                        //...
                        break;
                    case RELEFRAGMENTLOAD:
                        break;
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        intoView();
        intoEvent();
        intoFragment();
    }

    private void intoFragment() {
        mFManaage = getSupportFragmentManager();
        fragment_hgLoad();
    }

    private void intoEvent() {
        btn_hg.setOnClickListener(this);
        btn_classification.setOnClickListener(this);
        btn_dope.setOnClickListener(this);
        btn_release.setOnClickListener(this);
        imgb_add_shop.setOnClickListener(this);
        btn_album.setOnClickListener(this);
        btn_cim.setOnClickListener(this);

    }

    private void intoView() {
        btn_hg = (Button) findViewById(R.id.id_btn_hg);
        btn_classification = (Button) findViewById(R.id.id_btn_classification);
        btn_dope = (Button) findViewById(R.id.id_btn_dope);
        btn_release = (Button) findViewById(R.id.id_btn_release);
        btn_album = (Button) findViewById(R.id.btn_album);
        btn_cim = (Button) findViewById(R.id.btn_cim);
        imgb_add_shop = (ImageButton) findViewById(R.id.add_this_shop);

        currentClickBtn = btn_hg;
        currentClickBtn.setTextColor(0xffff9c00);
        currentClickBtn.setBackgroundColor(0xfbfbfbf);


        layout_select_img_fg = findViewById(R.id.layout_select_img_fg);
        layout_select_img_bs = findViewById(R.id.layout_select_img_bs);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_hg:
                if (v != currentClickBtn) {
                    bottonBtnClickChangeStyle(v);
                    mFTransaction = mFManaage.beginTransaction();
                    fragmentShowToHide(fragment_hg);
                }
                break;
            case R.id.id_btn_classification:
                if (v != currentClickBtn) {
                    currentFragmentNumber = CASSFRAGMENTLOAD;
                    //判断如果按的按钮没有重复的话，那么就进行切换的操作
                    bottonBtnClickChangeStyle(v);
                    mFTransaction = mFManaage.beginTransaction();
                    if (cass_fragment_state == BEGING_LOADED) {
                        //判断如果现在fragment的状态属于属于正在加载的话
                        setingLoadFragmentIsShow();
                    } else if (cass_fragment_state == NO_LOAD) {
                        //判断当前页面如果还没有加载过的话就让其数据去进行加载，并且让加载页面显示出来
                        cass_fragment_state = BEGING_LOADED;
                        setingLoadFragmentIsShow();
                        shopClass2 = ShopClass.getShopClass2(this, mHandler);
//                        fragment_canssLoad();
                    } else if (cass_fragment_state == LOADED) {
                        //判断，如果加载成功的话，就显示当前界面
                        fragmentShowToHide(fragment_canss);
                    } else if (cass_fragment_state == FAILED_TO_LOAD) {
                        //最后一种状态，也就是加载失败的话，那么就显示网络异常界面
                        //这里写显示那个界面的代码....
                    }

                }
                break;
            case R.id.id_btn_dope:
                if (v != currentClickBtn) {
                    currentFragmentNumber = MSGFRAGMENTLOAD;
                    bottonBtnClickChangeStyle(v);
                    mFTransaction = mFManaage.beginTransaction();
                    if (fragment_message == null) {
                        fragment_message = new MessagesFragment();
                        mFTransaction.add(R.id.set_fragment, fragment_message);
                    }
                    fragmentShowToHide(fragment_message);
                    Login_util.isLogin(this);
                }
                break;
            case R.id.id_btn_release:
                if (v != currentClickBtn) {
                    bottonBtnClickChangeStyle(v);
                    currentFragmentNumber = MSGFRAGMENTLOAD;
                    mFTransaction = mFManaage.beginTransaction();
                    if (fragment_relese == null) {
                        fragment_relese = new ReleaseFragment();
                        mFTransaction.add(R.id.set_fragment, fragment_relese);
                    }
                    fragmentShowToHide(fragment_relese);
                    Login_util.isLogin(this);
                }
                break;
            case R.id.btn_album:
                getImage(1);
                break;
            case R.id.btn_cim:
                getImage(2);
                break;
            case R.id.add_this_shop:
                ejectOption();
                break;
        }
    }


    //设置fragment_hg页面的加载操作
    private void fragment_hgLoad() {
        mFTransaction = mFManaage.beginTransaction();
        fragment_hg = new HgFragment(new TuiJianFragment.TuiJianListItemClickListener() {
            @Override
            public void onItemClick(Commodity comm) {
                Intent intent = new Intent(com.lang.big.biglang.activity.homepage.this, com.lang.big.biglang.activity.CommShowActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("comm",comm);
                intent.putExtras(bundle);
                startActivityForResult(intent, COMMSHOWRESULTCODE);
            }
        });
        mFTransaction.add(R.id.set_fragment, fragment_hg);
        currentFragment = fragment_hg;
        fragmentShowToHide(fragment_hg);
    }

    //设置加载画面的fragment的显示
    private void setingLoadFragmentIsShow() {
        if (ingLoadFragment == null) {
            ingLoadFragment = new IngLoadFragment();
            mFTransaction.add(R.id.set_fragment, ingLoadFragment);
        }
        fragmentShowToHide(ingLoadFragment);
    }


    //设置fragment_canss的加载操作
    private void fragment_canssLoad() {
        mFTransaction = mFManaage.beginTransaction();
        fragment_canss = new ClassificationFragment(shopClass2);
        //这里将fragmeent的状态设置成加载完毕的状态
        mFTransaction.add(R.id.set_fragment, fragment_canss);
        if(currentFragmentNumber==CASSFRAGMENTLOAD) {
            fragmentShowToHide(fragment_canss);
        }else{
            mFTransaction.hide(fragment_canss);
            mFTransaction.commit();
        }
        cass_fragment_state = LOADED;
    }


    private void getImage(int i) {
        int code = 0;
        if (i == 1) {
            btn_album.setBackgroundResource(R.drawable.iamge_select_blue);
            btn_cim.setBackgroundResource(R.drawable.iamge_select_red);
            code = 2;
        } else {
            btn_cim.setBackgroundResource(R.drawable.iamge_select_blue);
            btn_album.setBackgroundResource(R.drawable.iamge_select_red);
            code = 1;
        }
        Intent intent = new Intent(this, MReleaseActivity.class);
        intent.putExtra("intent", code);
        startActivity(intent);
        hideOption();
    }

    private MyAnimation mAnimation;

    private void ejectOption() {
        isSelectImg = true;
        layout_select_img_fg.setVisibility(View.VISIBLE);
        //获得滑动对象
        mAnimation = MyAnimation.getMyAnimation();
        //设置获得图片界面透明度动画
        setLayoutAlpha();
        //设置滑动效果
        setViewTranslate();
    }

    private void setViewTranslate() {
        mAnimation.setViewTranslate(1000, true, 0, 0, 100, 0, layout_select_img_bs);
        mAnimation.setViewTranslate(1000, true, 0, 0, 10, 0, btn_album);
        mAnimation.setViewTranslate(1000, true, 0, 0, 10, 0, btn_cim);
    }

    private void setLayoutAlpha() {
        mAnimation.setLayoutAlpha(1000, true, 0f, 1f, layout_select_img_fg);
    }

    private void bottonBtnClickChangeStyle(View v) {
        Button btn = (Button) v;
        resetBtnTextColor();
        btn.setTextColor(0xffff9c00);
        btn.setBackgroundColor(0xfbfbfbf);
        currentClickBtn = btn;
    }

    private void fragmentShowToHide(Fragment mFragemnt) {

        mFTransaction.hide(currentFragment);
        mFTransaction.show(mFragemnt);
        currentFragment = mFragemnt;
        mFTransaction.commit();
    }

    private void resetBtnTextColor() {
        currentClickBtn.setTextColor(0xff000000);
        currentClickBtn.setBackgroundColor(0x00000000);
    }


    @Override
    public void onBackPressed() {
        if (isSelectImg) {
            hideOption();
        } else {
            super.onBackPressed();
        }
    }

    private void hideOption() {
        //            layout_select_img_bs.clearAnimation();
        layout_select_img_fg.clearAnimation();
//            btn_album.clearAnimation();
//            btn_cim.clearAnimation();
        layout_select_img_fg.setVisibility(View.GONE);
//            Toast.makeText(homepage.this,(layout_select_img_fg.getVisibility()==View.GONE)+"", Toast.LENGTH_SHORT).show();
        isSelectImg = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
