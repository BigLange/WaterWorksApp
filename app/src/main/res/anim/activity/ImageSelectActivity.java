package com.lang.big.biglang.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lang.big.biglang.Adapter.ImageSlectAdapter;
import com.lang.big.biglang.R;
import com.lang.big.biglang.bean.FolderBean;
import com.lang.big.biglang.popupwindow.SelectImgDirPopupWindow;
import com.lang.big.biglang.utils.MyFileNameFileter;
import com.lang.big.biglang.utils.MyThreadPool;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class ImageSelectActivity extends Activity implements Runnable,View.OnClickListener {

    private GridView mGridView;
    private ImageSlectAdapter imgAdapter;
    private List<String> mImgs;
    private RelativeLayout mButton;
    private TextView mDirName;
    private TextView mDirCount;
    private Button mConmmitBtn;


    private File currentDir;
    private int mMaxCount;

    private ProgressDialog mProgressDialog;

    private List<FolderBean> mFolderBeans = new ArrayList<>();

    private ExecutorService pool;

    private SelectImgDirPopupWindow sidPop;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //这里接受到信息后，的处理
            mProgressDialog.dismiss();
            //绑定数据到View中去
            data2View();
            initPopupWindow();

        }
    };

    private void initPopupWindow() {
        sidPop = new SelectImgDirPopupWindow(com.lang.big.biglang.activity.ImageSelectActivity.this,mFolderBeans);
        sidPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //当popupwindow消失的时候，设置屏幕蒙版消失
                lightOn();
            }
        });
        sidPop.setMListView(new SelectImgDirPopupWindow.OnDirPopupWinowListener() {
            @Override
            public void onSeleed(FolderBean folderBean) {
                currentDir = new File(folderBean.getDirPath());
                mImgs = Arrays.asList(currentDir.list(new MyFileNameFileter()));
                imgAdapter = new ImageSlectAdapter(com.lang.big.biglang.activity.ImageSelectActivity.this,R.layout.iamge_select_grid_item_moban,
                        mImgs,currentDir.getAbsolutePath());
                mGridView.setAdapter(imgAdapter);

                mDirCount.setText(mImgs.size()+"");
                mDirName.setText(folderBean.getName());
                sidPop.dismiss();
            }
        });
    }

    //下面的菜单去掉的时候，界面变亮
    private void lightOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    //下面的菜单弹出来的时候，界面变暗
    private void lightOff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    private void data2View() {
        if (currentDir == null) {
            Toast.makeText(com.lang.big.biglang.activity.ImageSelectActivity.this, "未扫描的任何图片", Toast.LENGTH_SHORT).show();
            return;
        }
//        System.out.println(currentDir);
        mImgs = Arrays.asList(currentDir.list(new MyFileNameFileter()));
        imgAdapter = new ImageSlectAdapter(com.lang.big.biglang.activity.ImageSelectActivity.this, R.layout.iamge_select_grid_item_moban
                , mImgs, currentDir.getAbsolutePath());

        mGridView.setAdapter(imgAdapter);

        mDirCount.setText(mMaxCount + "");
        mDirName.setText(currentDir.getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);

        initView();
        initDatas();
        initEvent();
    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.img_select_main_grid);
        mButton = (RelativeLayout) findViewById(R.id.img_select_bottom_layout);
        mDirName = (TextView) findViewById(R.id.img_select_wenjianjia);
        mDirCount = (TextView) findViewById(R.id.img_select_number);
        mConmmitBtn = (Button)findViewById(R.id.img_select_commit_btn);
    }

    private void initDatas() {
        //遍历所有的图片
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(com.lang.big.biglang.activity.ImageSelectActivity.this, "当前存储卡不可用", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pool == null) {
            pool = MyThreadPool.getMyThreadPool().getPool();
        }
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
        pool.submit(this);
    }

    private void initEvent() {
        mButton.setOnClickListener(this);
        mConmmitBtn.setOnClickListener(this);
    }


    @Override
    public void run() {

        //多线程来扫描
        //获取图片对应的Uri
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = this.getContentResolver();


        Cursor cursor = cr.query(uri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);

        Set<String> mDirPaths = new HashSet<>();

        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            //获得这张图片地址的父目录的file对象
            File parenFile = new File(path).getParentFile();

            if (parenFile == null) {
                continue;
            }
            String dirPath = parenFile.getAbsolutePath();
            FolderBean folderBean = null;

            if (mDirPaths.contains(dirPath)) {
                continue;
            } else {
                mDirPaths.add(dirPath);
                folderBean = new FolderBean();
                folderBean.setDirPath(dirPath);
                folderBean.setFirstImgPath(path);
            }

            if (parenFile.list() == null)
                continue;

            int picSize = parenFile.list(new MyFileNameFileter()).length;

            folderBean.setCount(picSize);
            mFolderBeans.add(folderBean);

            //最开始显示的那个文件夹里面相片。这里设置为最多的显示
            if (picSize > mMaxCount) {
                mMaxCount = picSize;
                currentDir = parenFile;
            }
        }
        cursor.close();

        //通知扫描结束
        mHandler.sendEmptyMessage(0x110);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_select_bottom_layout:
                //设置动画
                sidPop.setAnimationStyle(R.style.dir_popupwindow_anim);
                //设置SelectImgDirPopupWindow显示在mButton的正上方
                sidPop.showAsDropDown(mButton, 0, 0);
                //设置内容区域变黑
                lightOff();
                break;
            case R.id.img_select_commit_btn:
                HashSet<String> dirPaths = imgAdapter.getDriPaths();
                if(dirPaths.size()<17) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dirPaths", dirPaths);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    this.finish();
                }else{
                    Toast.makeText(com.lang.big.biglang.activity.ImageSelectActivity.this, "选取图片多于16张", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public void setmDirCountShowToHide(boolean show){
        if(show){
            mDirCount.setVisibility(View.VISIBLE);
        }else{
            mDirCount.setVisibility(View.GONE);
        }
    }

    public void setmConmmitBtnShowToHide(boolean show){
        if(show){
            mConmmitBtn.setVisibility(View.VISIBLE);
        }else{
            mConmmitBtn.setVisibility(View.GONE);
        }
    }

    public Button getmConmmitBtn(){
        return mConmmitBtn;
    }
}
