package com.example.think.waterworksapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Think on 2016/8/4.
 */
public class SharedPreferencesUtils {

    private static SharedPreferencesUtils utils;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferencesUtils(Context context,String TableName){
        sharedPreferences = context.getSharedPreferences(TableName,context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

//    public static SharedPreferencesUtils getUtils(Context context){
//        if (utils==null)
//            utils = new SharedPreferencesUtils(context);
//        return utils;
//    }

    public void clearUtils(){
        sharedPreferences = null;
        editor = null;
        utils = null;
    }

    public void preservationData(HashMap<String,Object> dataMap){
        Set<String> keySet = dataMap.keySet();
        for (String key:keySet) {
            editor.putString(key,dataMap.get(key).toString());
        }
        editor.commit();
    }

    public void preservationStringData(String key,String data){
        editor.putString(key,data);
        editor.commit();
    }

    public void preservatioLongData(String key,long data){
        editor.putLong(key,data);
        editor.commit();
    }

    public void preservationBooleanData(String key,boolean data){
        editor.putBoolean(key,data);
        editor.commit();
    }

    public Map<String,Object> takeOutAll(){
        HashMap<String,Object> dataMap = (HashMap<String, Object>) sharedPreferences.getAll();
        return dataMap;
    }

    public String takeOutString(String key){
        return sharedPreferences.getString(key,"");
    }

    public long takeOutLong(String key){
        return sharedPreferences.getLong(key,-1);
    }

    public boolean takeOutBoolean(String key){
        return sharedPreferences.getBoolean(key,true);
    }

    public void deleteString(){
        editor.clear();
        editor.commit();
    }

}
