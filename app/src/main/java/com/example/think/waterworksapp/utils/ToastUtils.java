package com.example.think.waterworksapp.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Think on 2016/6/22.
 */
public class ToastUtils {


    public static void showToast(Context context,String showValue){
        Toast.makeText(context.getApplicationContext(), showValue, Toast.LENGTH_SHORT).show();
    }
}
