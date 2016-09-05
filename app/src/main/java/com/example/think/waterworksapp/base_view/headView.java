package com.example.think.waterworksapp.base_view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.think.waterworksapp.R;


/**
 * Created by Think on 2016/6/3.
 */
public class headView extends LinearLayout implements View.OnClickListener{
    private Context context;

    private ImageView backImgView;
    private TextView textView;
    private String textValue;

    public headView(Context context) {
        super(context);
        this.context  = context;
    }

    public headView(Context context, AttributeSet attrs){
        super(context,attrs);
        this.context  = context;
        TypedArray mTypeArray = context.obtainStyledAttributes(attrs, R.styleable.headView);
        textValue = mTypeArray.getString(R.styleable.headView_text);
        initView();
        initEvent();
    }

    private void initView(){
        View view = LayoutInflater.from(context).inflate(R.layout.head_view_moban,this);
        textView = (TextView)view.findViewById(R.id.id_head_text);
        textView.setText(textValue);
        backImgView = (ImageView)view.findViewById(R.id.id_head_back_img);
    }

    private void initEvent(){
        backImgView.setOnClickListener(this);
    }

    public void setBackImg(Bitmap bitmap){
        backImgView.setImageBitmap(bitmap);
    }

    public void setBackImg(int resID){
        backImgView.setImageResource(resID);
    }

    public void setTitleTextAttribute(int textColor,int textSize){
        textView.setTextColor(textColor);
        textView.setTextSize(textSize);
    }

    public void setBackIconHide(){
        backImgView.setVisibility(GONE);
    }

    @Override
    public void onClick(View view) {
        Activity activity = (Activity) context;
        activity.finish();
    }

    public void setText(String text){
        textView.setText(text);
    }

}
