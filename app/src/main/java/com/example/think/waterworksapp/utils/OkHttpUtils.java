package com.example.think.waterworksapp.utils;


import android.graphics.Bitmap;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Think on 2016/6/21.
 */
public class OkHttpUtils {
    private final  String PERECETION_URL = "http://36.110.36.118:8081/api/";
    private final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");//这个表示上传的类型为图片类型
    private OkHttpClient mOkHttpClient;
    private static OkHttpUtils httpUtils;

    private OkHttpUtils(){
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
    }

    public static OkHttpUtils getOkHttpUtils(){
        if (httpUtils==null)
            httpUtils = new OkHttpUtils();
        return httpUtils;
    }


    public void doGetAsyn(String url, Callback callback){
        Call call = createCall(url);
        call.enqueue(callback);
    }

    public void doPostAsyn(String url, Map<String,Object> map, Callback callback){
        url = mosaicUrl(url);
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for(String key:map.keySet()) {
            builder.add(key,map.get(key).toString());
        }
        sendRequest(builder.build(),url,callback);
    }

    public void doPostAsyn(String url, String data,Callback callback){
        url = mosaicUrl(url);
        MediaType mediaType = MediaType.parse("raw");
        sendRequest(RequestBody.create(mediaType, data),url,callback);
    }

    public void doPostAsynValueToHeader(String url, Map<String,Object> headMap,Callback callback){
        url = mosaicUrl(url);
        FormEncodingBuilder builder = new FormEncodingBuilder();
        RequestBody requestBody = builder.build();
        Headers.Builder headerBuilder = new Headers.Builder();
        for(String key:headMap.keySet()) {
            Object obj = headMap.get(key);
            String data = "";
            if (obj!=null)
                data = obj.toString();
            headerBuilder.add(key,data);
        }
        Request request = new Request.Builder().url(url).headers(headerBuilder.build()).post(requestBody).build();
        sendRequest(request,callback);
    }

    public void doPostAsynValueToHeader(String url, Map<String,Object> headMap,Map<String,Object> bodyMap,Callback callback){
        url = mosaicUrl(url);
        FormEncodingBuilder builder = new FormEncodingBuilder();
        Headers.Builder headerBuilder = new Headers.Builder();
        for(String key:headMap.keySet()) {
            Object obj = headMap.get(key);
            String data = "";
            if (obj!=null)
                data = obj.toString();
            headerBuilder.add(key,data);
        }
        for (String key:bodyMap.keySet()){
            Object obj = headMap.get(key);
            String data = "";
            if (obj!=null)
                data = obj.toString();
            builder.add(key,data);
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).headers(headerBuilder.build()).post(requestBody).build();
        sendRequest(request,callback);
    }



    public void upLoadImg(String url, ArrayList<Bitmap> bitmaps,String upLoadImageKey
            , Map<String,Object> submitDta,Callback callback) {
        url=PERECETION_URL+url;
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for (String key:submitDta.keySet()) {
            Log.e("submit",key+":"+submitDta.get(key));
            builder.addFormDataPart(key, null, RequestBody.create(null, submitDta.get(key)+""));
        }
        for (Bitmap bitmap : bitmaps) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] b = bos.toByteArray();
            builder.addFormDataPart(upLoadImageKey, null, RequestBody.create(MEDIA_TYPE_PNG, b));
        }
        RequestBody requestBody = builder.build();
        sendRequest( requestBody, url,callback);
    }



    private Call createCall(String url){
        url = mosaicUrl(url);
        Request request = new Request.Builder().url(url).build();
        return mOkHttpClient.newCall(request);
    }

    private void sendRequest(RequestBody requestBody,String url,Callback callback){
        Request request = new Request.Builder().url(url).post(requestBody).build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    private void sendRequest(Request request,Callback callback){
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    private String mosaicUrl(String url){
        return PERECETION_URL+url;
    }

    public static void clearhttpUtils(){
        httpUtils  = null;
    }
}
