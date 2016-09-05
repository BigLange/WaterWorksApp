package com.example.think.waterworksapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.think.waterworksapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Think on 2016/8/11.
 */
public class PictureCarouselAdapter extends PagerAdapter {

    private ArrayList<String> imgUrls;
    private ArrayList<Integer> imgResourcesId;
    private Context context;
    private View.OnClickListener listener;

    public PictureCarouselAdapter(ArrayList<String> imgUrls,Context context){
        this.imgUrls = imgUrls;
        this.context = context;
    }

    public PictureCarouselAdapter(ArrayList<Integer> imgResourcesId, Context context, int number, View.OnClickListener listener){
        this.imgResourcesId = imgResourcesId;
        this.context = context;
        this.listener = listener;
    }




    @Override
    public int getCount() {
        if (imgUrls!=null)
            return imgUrls.size();
        else
            return imgResourcesId.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        if (imgUrls != null){
            Picasso.with(context).load(imgUrls.get(position)).error(R.drawable.logo).placeholder(R.drawable.pass_word_blue_click).into(imageView);
        }else {
            imageView.setImageResource(imgResourcesId.get(position));
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(imageView);
        imageView.setOnClickListener(listener);
        return imageView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }


}
