package com.example.think.waterworksapp.base_view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.think.waterworksapp.R;
import com.example.think.waterworksapp.adapter.PictureCarouselAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Think on 2016/8/11.
 */
public class PictureCarouselView extends RelativeLayout implements ViewPager.OnPageChangeListener{

    private ArrayList<String> imgUrls;
    private ArrayList<Integer> imgResourcesId;
    private int position = 0;
    private int dataSize;

    private Timer timer;
    private MyTimerTask timerTask;
    private final int timerObten = 5000;

    private PictureCarouselAdapter mAdaptaer;
    private ViewPager mViewPager;
    private TextView textView;
    private RelativeLayout layout;
    private Context context;

    private OnClickListener listener;

    private Handler handler = new Handler();

    public PictureCarouselView(Context context) {
        super(context);
        this.context = context;
        layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.picture_carouse_view,this);
        mViewPager = (ViewPager) layout.findViewById(R.id.image_show_viewPager);
//        textView = (TextView) layout.findViewById(R.id.show_current_number);
    }

    public PictureCarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.picture_carouse_view,this);
        mViewPager = (ViewPager) layout.findViewById(R.id.image_show_viewPager);
        textView = (TextView) layout.findViewById(R.id.show_current_number);
//        textView.setOnClickListener(this);
    }


    public void setImgUrls(ArrayList<String> imgUrls){
        this.imgUrls = imgUrls;
        initView();
        initEvent();
        startTimer();
    }

    public void setImgResourcesId(ArrayList<Integer> imgResourcesId){
        this.imgResourcesId = imgResourcesId;
        dataSize = imgResourcesId.size();
        initView();
        initEvent();
    }


    private void initView(){
        textView.setText(position+1+"/"+dataSize);
        mAdaptaer = new PictureCarouselAdapter(imgResourcesId,context,-1,listener);
        mViewPager.setAdapter(mAdaptaer);
    }

    private void initEvent() {
        mViewPager.setOnPageChangeListener(this);
    }

    public void startTimer(){
        if (timer!=null)
            stopTimer();
        timer = new Timer();
        timerTask = new MyTimerTask();
        timer.schedule(timerTask,timerObten,timerObten);
    }

    public void stopTimer(){
        if (timer!=null) {
            timer.cancel();
            timer = null;
            timerTask = null;
        }
    }

    public void setListener(OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchEventAction = event.getAction();
        switch (touchEventAction){
            case MotionEvent.ACTION_DOWN:
                stopTimer();
                break;
            case MotionEvent.ACTION_UP:
                startTimer();
                break;
        }
        return false;
    }





    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        textView.setText(position+1+"/"+dataSize);
        this.position = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public int getPosition() {
        return position;
    }

    class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    int number = (position+1)%dataSize;
                    mViewPager.setCurrentItem(number);
                }
            });
        }
    }

}
