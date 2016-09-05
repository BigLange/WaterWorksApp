package com.example.think.waterworksapp.popup_window;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.think.waterworksapp.R;


/**
 * Created by Administrator on 2016/4/18.
 */
public class AddImagePopupWindow extends PopupWindow implements AdapterView.OnItemClickListener{


    private  int mWidth;
    private int mHeight;
    private View mConvertView;
    private ListView mListView;
    private Context context;

    private ArrayAdapter<String> adapter;

    private AddImageItemClickListener listener;

    private String[] addlistoptions = new String[]{"拍照","从手机相册获取","取消"};




    public AddImagePopupWindow(Context context,AddImageItemClickListener listener){
        caiWidthAndHeight(context);
        this.context = context;
        this.listener = listener;
        mConvertView = LayoutInflater.from(context).inflate(R.layout.add_image_popup_window_moban,null);

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
    }



    private void initViews(Context context) {
        mListView = (ListView)mConvertView.findViewById(R.id.popupWindow_list);
        adapter = new ArrayAdapter<String>(context,R.layout.addimg_options_item_moban,R.id.addImg_potions_item_txt,addlistoptions);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
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
        mHeight = (int)(outMetrics.heightPixels*0.3);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position){
            case 0:
                dismiss();
                listener.toPhotograph();
                break;
            case 1:
                dismiss();
                listener.toAlbum();
                break;
            case 2:
                dismiss();
                break;
        }
    }

    public interface AddImageItemClickListener{
        void toPhotograph();
        void toAlbum();
    }

}
