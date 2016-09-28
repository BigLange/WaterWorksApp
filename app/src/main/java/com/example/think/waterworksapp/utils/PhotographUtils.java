package com.example.think.waterworksapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Think on 2016/9/27.
 */

public class PhotographUtils {
    public static final int PHOTO_GRAPH_KEY = 10010;
    private Activity activity;
    private String currentImgPath = "";

    public PhotographUtils(Activity activity){
        this.activity = activity;
    }

    public void getBitmapByIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(activity.getPackageManager())!=null){
            activity.startActivityForResult(intent,PHOTO_GRAPH_KEY);
        }
    }


    public void saveBitmapByIntent() throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(activity.getPackageManager())!=null){
            File imageFile = createImageFile();
            if (imageFile!=null){
                //它使用了content：//Url 代替了 file:///Uri.. 可以让其他应用分享本应用下的  包名/files 目录
                Uri photoURI = FileProvider.getUriForFile(activity,
                        "com.huge.fileprovider",
                        imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(intent, PHOTO_GRAPH_KEY);
            }
        }
    }


    private File createImageFile() throws IOException{
        String currentTime = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_"+currentTime+"_";//图片文件的名字
        //获取应用下的  包名/files 目录 ，这个目录一般放一些长时间保存的数据
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName,".jpg",storageDir);
        currentImgPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    public String getCurrentImgPath(){
        String imgPath = currentImgPath;
        currentImgPath = "";
        return imgPath;
    }
}
