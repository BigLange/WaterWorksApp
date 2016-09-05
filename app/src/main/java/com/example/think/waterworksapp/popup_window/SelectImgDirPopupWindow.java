package com.example.think.waterworksapp.popup_window;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.think.waterworksapp.R;
import com.example.think.waterworksapp.bean.FolderBean;
import com.example.think.waterworksapp.utils.ImageLoad;

import java.util.List;

/**
 * Created by Administrator on 2016/4/18.
 */
public class SelectImgDirPopupWindow extends PopupWindow {


    private  int mWidth;
    private int mHeight;
    private View mConvertView;
    private ListView mListView;

    private List<FolderBean> mDatas;



    public interface OnDirPopupWinowListener{
        void onSeleed(FolderBean folderBean);
    }

    public OnDirPopupWinowListener mListener;

    public SelectImgDirPopupWindow(Context context, List<FolderBean> datas){
        mDatas = datas;
        caiWidthAndHeight(context);
        mConvertView = LayoutInflater.from(context).inflate(R.layout.select_img_popup_mian,null);

        setContentView(mConvertView);
        setWidth(mWidth);
        setHeight(mHeight);

        //设置可触摸
        setFocusable(true);
        setTouchable(true);
        //设置外部可点击
        setOutsideTouchable(true);
        //设置PopupWindow背景，如果不设置这个,则他本身的dismiss无法执行
        setBackgroundDrawable(new BitmapDrawable());

        //设置点击监听，设置点击在外部，让他消失
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        initViews(context);
        initEvent();
    }

    public void setMListView(OnDirPopupWinowListener mListener){
        this.mListener = mListener;
    }

    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mListener!=null) {
                    FolderBean mBean = mDatas.get(position);
                    mListener.onSeleed(mBean);
                }
            }
        });


    }

    private void initViews(Context context) {
        mListView = (ListView)mConvertView.findViewById(R.id.select_img_popup_mian_list);
        mListView.setAdapter(new ListDirAdapter(context,mDatas));
    }

    //计算popupwindow的宽度和高度
    private void caiWidthAndHeight(Context context) {
        WindowManager mWindowManage = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        //这个对象用来获得屏幕宽高
        DisplayMetrics outMetrics = new DisplayMetrics();
        //这么传进去就能够获得屏幕宽高
        mWindowManage.getDefaultDisplay().getMetrics(outMetrics);
        //设置显示宽度
        mWidth = outMetrics.widthPixels;
        //设置显示在屏幕上的高度为屏幕高度的0.7倍
        mHeight = (int)(outMetrics.heightPixels*0.7);
    }

    private class ListDirAdapter extends ArrayAdapter<FolderBean>{
        private LayoutInflater mInflater;

        public ListDirAdapter(Context context,List<FolderBean> datas) {
            super(context, 0,datas);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder = null;
            FolderBean mBean = getItem(position);
            if(convertView==null){
                convertView = mInflater.inflate(R.layout.select_img_popup_item_moban,parent,false);
                mHolder = new ViewHolder();
                mHolder.mImg = (ImageView)convertView.findViewById(R.id.select_img_popup_item_img);
                mHolder.mDirName = (TextView)convertView.findViewById(R.id.select_img_item_dir_name_txt);
                mHolder.mDirCount = (TextView)convertView.findViewById(R.id.select_img_item_imt_count_txt);
                convertView.setTag(mHolder);
            }else{
                mHolder = (ViewHolder)convertView.getTag();
            }
            mHolder.mDirCount.setText(mBean.getCount()+"");
            mHolder.mDirName.setText(mBean.getName());
            mHolder.mImg.setImageResource(R.drawable.pictures_no);

            ImageLoad.getImageLoad().loadImage(mBean.getFirstImgPath(),mHolder.mImg);
            return convertView;
        }

        private class ViewHolder{
            ImageView mImg;
            TextView mDirName;
            TextView mDirCount;
        }
    }
}
