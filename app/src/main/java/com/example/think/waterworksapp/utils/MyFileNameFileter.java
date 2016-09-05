package com.example.think.waterworksapp.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by Administrator on 2016/4/17.
 */
public class MyFileNameFileter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String filename) {
        if (filename.endsWith(".jpg")||filename.endsWith(".jpeg")||filename.endsWith(".png"))
            return true;
        return false;
    }
}
