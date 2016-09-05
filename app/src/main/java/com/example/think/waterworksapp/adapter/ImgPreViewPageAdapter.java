package com.example.think.waterworksapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.think.waterworksapp.R;
import com.example.think.waterworksapp.utils.ImageLoad;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/20.
 */
public class ImgPreViewPageAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> dirPath = new ArrayList<>();

    public ImgPreViewPageAdapter(Context context, ArrayList<String> mDirPaths){
        this.context =context;
        dirPath = mDirPaths;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        ImageLoad.getImageLoad().loadImage(dirPath.get(position), imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //系统会直接帮我们将那个界面的对象(这里的话就是那个imageView对象)变成Object的形式传进来
        //然后我们的就直接强转一下类型，然后删除掉就好了。
        container.removeView((View) object);
//                mImageViews.remove(position);
    }

    @Override
    public int getCount() {
        return dirPath.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}
