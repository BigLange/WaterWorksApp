package com.example.think.waterworksapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.example.think.waterworksapp.ImageSelectActivity;
import com.example.think.waterworksapp.R;
import com.example.think.waterworksapp.utils.ImageLoad;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Administrator on 2016/4/17.
 */
public class ImageSlectAdapter extends BaseAdapter {

    private int resouce;
    private List<String> mDatas;
    private String dirPath;
    private ImageSelectActivity mActivity;

    private  HashSet<String> positions = new HashSet<>();


    public ImageSlectAdapter(Context context, int resouce, List<String> mDatas, String dirPath){
        if (context instanceof ImageSelectActivity){
            mActivity = (ImageSelectActivity)context;
        }
        this.resouce = resouce;
        //图片名称
        this.mDatas = mDatas;
        //图片父文件夹地址
        this.dirPath = dirPath;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        MyClick mClick = null;
        String filePath = dirPath + "/" + getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(mActivity).inflate(resouce,parent,false);
            mHolder = new ViewHolder();
            mClick = new MyClick();
            mHolder.mImg = (ImageView)convertView.findViewById(R.id.img_select_grid_item_img);
            mHolder.mSelect = (ImageButton)convertView.findViewById(R.id.img_select_xuanze_btn);
            mHolder.mImg.setOnClickListener(mClick);
//            mHolder.mImg.setTag(mClick);
            mHolder.mSelect.setTag(mClick);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder)convertView.getTag();
            mClick = (MyClick)mHolder.mSelect.getTag();
        }
        //重置状态
        mHolder.mImg.setImageResource(R.drawable.pictures_no);
        mHolder.mSelect.setImageResource(R.drawable.picture_unselected);
        mHolder.mImg.setColorFilter(null);

        //这句话就让图片加载了
        ImageLoad.getImageLoad().loadImage(filePath, mHolder.mImg);
        mClick.setPosition(filePath,mHolder.mImg,mHolder.mSelect);
        if(positions.contains(filePath)){
            mHolder.mImg.setColorFilter(Color.parseColor("#90000000"));
            mHolder.mSelect.setImageResource(R.drawable.pictures_selected);
        }
        return convertView;
    }

    public HashSet<String> getDriPaths(){
        return positions;
    }


    private class ViewHolder{
        ImageView mImg;
        ImageButton mSelect;
    }
    class MyClick implements View.OnClickListener{
        String filePath;
        ImageView mImg;
        ImageButton mSelect;
        Button mConmmitBtn;

        public void setPosition(String filePath,ImageView mImg,ImageButton mSelect){
            this.filePath = filePath;
            this.mImg = mImg;
            this.mSelect = mSelect;
            mConmmitBtn = mActivity.getmConmmitBtn();
        }



        @Override
        public void onClick(View v) {
            int size = positions.size();
            if(positions.contains(filePath)){
                if(size==1){
                    mActivity.setmConmmitBtnShowToHide(false);
                    mActivity.setmDirCountShowToHide(true);
                }
                positions.remove(filePath);
                mImg.setColorFilter(null);
                mSelect.setImageResource(R.drawable.picture_unselected);
            }else{
                if(size<1){
                    mActivity.setmConmmitBtnShowToHide(true);
                    mActivity.setmDirCountShowToHide(false);
                }
                positions.add(filePath);
                mImg.setColorFilter(Color.parseColor("#90000000"));
                mSelect.setImageResource(R.drawable.pictures_selected);
            }
            size = positions.size();
            if(size>0){
                mConmmitBtn.setText(size+" 张图片被选取");
            }
        }
    }
}
