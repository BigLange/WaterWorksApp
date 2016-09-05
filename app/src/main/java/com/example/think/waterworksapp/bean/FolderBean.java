package com.example.think.waterworksapp.bean;

/**
 * Created by Administrator on 2016/4/17.
 */
public class FolderBean {

    private String dirPath;//文件夹路径
    private String firstImgPath;//第一张图片的路径
    private String name;//文件夹名称
    private int count;//图片数量


    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
        int lastIndexOf = this.dirPath.lastIndexOf("/");
        this.name = this.dirPath.substring(lastIndexOf,dirPath.length());
    }

    public String getFirstImgPath() {
        return firstImgPath;
    }

    public void setFirstImgPath(String firstImgPath) {
        this.firstImgPath = firstImgPath;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }
}
