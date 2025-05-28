package com.baidu.tetris.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsUtils {
    private String fileName;
    private Context mContext;
    private SharedPreferences prefs;
    private final String PASSWORD_KEY = "password_key";

    //初始化文件的信息 名称
    public void init(Context context,String name){
        this.fileName = name;
        this.mContext = context;

        //创建或者打开文件
        prefs = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
    }

    //存密码
    public void savePassword(String pwd){
        if (prefs == null) return;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PASSWORD_KEY, pwd);
        editor.apply();
    }
    //取密码
    public String getPassword(){
        if (prefs == null) return null;

        return prefs.getString(PASSWORD_KEY, null);
    }

}
