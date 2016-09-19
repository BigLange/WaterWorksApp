package com.example.think.waterworksapp.utils;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Think on 2016/9/12.
 */

public class ActivityPreservationUtils {
    private static HashMap<String,Activity> activityMap = new HashMap<>();

    public static void setActivity(Activity activity){
        activityMap.put(activity.toString(),activity);
    }

    public static void cleanActivity(Activity activity){
        activityMap.remove(activity.toString().toString());
    }

    public static void finshAllActivity(){
        for (String key:activityMap.keySet()){
            activityMap.get(key).finish();
        }
        activityMap.clear();
    }
}
