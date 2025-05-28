package com.baidu.tetris.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GridModel {
    private int flag = 0; //是否有数据
    private int blockRes = 0; //方格对应的图片资源

    public GridModel(int flag,int blockRes){
        this.flag = flag;
        this.blockRes = blockRes;
    }
    //提供给外部一个方法 快速判断当前这个方格是否有数据
    public boolean hasBlock(){
        return flag == 1;
    }

    //获取这个方格对应资源的Bitmap
    public Bitmap getBitmap(Context context){
        return BitmapFactory.decodeResource(context.getResources(),blockRes);
    }
}
