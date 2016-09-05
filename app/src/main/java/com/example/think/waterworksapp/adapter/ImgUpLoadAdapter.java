package com.example.think.waterworksapp.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.think.waterworksapp.R;
import com.example.think.waterworksapp.utils.ImageLoad;

import java.util.ArrayList;

/**
 * Created by Think on 2016/8/17.
 */
public class ImgUpLoadAdapter extends ArrayAdapter<String> implements View.OnClickListener{

    private final int LAYOUT_ID = R.layout.img_up_load_grid_item;
    private final int IMGVIEW_ID = R.id.up_load_imgView;

    private ArrayList<String> imgUrl;

    private LayoutInflater inflater;

    private AddImageViewBtnClickListener listener;

    public ImgUpLoadAdapter(Context context,ArrayList<String> imgUrls,AddImageViewBtnClickListener listener) {
        super(context, -1,imgUrls);
        inflater = LayoutInflater.from(context);
        this.imgUrl = imgUrls;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        int dataSize = imgUrl.size();
        if (dataSize<16){
            return dataSize+1;
        }
        return dataSize;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position<imgUrl.size()) {
            ViewHolder mHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(LAYOUT_ID, parent, false);
                mHolder = new ViewHolder();
                mHolder.imgView = (ImageView) convertView.findViewById(IMGVIEW_ID);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
                if (mHolder==null){
                    convertView = inflater.inflate(LAYOUT_ID, parent, false);
                    mHolder = new ViewHolder();
                    mHolder.imgView = (ImageView) convertView.findViewById(IMGVIEW_ID);
                    convertView.setTag(mHolder);
                }
            }
//        mHolder.mimg.setImageResource(R.drawable.yxy);
            ImageLoad.getImageLoad().loadImage(imgUrl.get(position), mHolder.imgView);
        }else{
            convertView  =  inflater.inflate(R.layout.up_load_img_btn_item,parent,false);
            ImageButton addImgBtn = (ImageButton) convertView.findViewById(R.id.addImg_btn);
            addImgBtn.setOnClickListener(this);
        }
        return convertView;
    }


    @Override
    public void onClick(View view) {
        listener.onAddImgClick();
    }

    class ViewHolder{
        ImageView imgView;
    }


    public interface AddImageViewBtnClickListener{
        void onAddImgClick();
    }
}
