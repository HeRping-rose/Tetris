package com.ronnie.game.utils;

import android.content.res.Resources;

public class Constants {
    public static final int SIZE = dp2px(20);
    public static final int ROW=20;
    public static final int COLUMN=10;



    private static int dp2px(int dp){
        return (int)(Resources.getSystem().getDisplayMetrics().density * dp);
    }
}