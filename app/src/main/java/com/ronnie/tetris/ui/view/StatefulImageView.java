package com.ronnie.tetris.ui.view;


import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

//有状态的图片控件
public class StatefulImageView extends AppCompatImageView {
    //定义变量记录状态值
    private State mState=State.NORMAL;
    //定义变量记录三种状态下的各种图片资源
    private int normalRes=0;
    private int selectedRes=0;
    private int errorRes=0;

    //    /提供给外部一个设置状态的方法
    public void changeStateTo(State state) {
        //保存外部传递过来的值
        this.mState=state;
        if (mState == State.NORMAL) {
            setImageResource(normalRes);
        } else if (mState == State.SELECTED) {
            setImageResource(selectedRes);
        }else {
            setImageResource(errorRes);
        }

    }
    //提供一个构造方法 外部构建的时候同时传递基本的图片资源
    //代码中使用
    public StatefulImageView(@NonNull Context context,int normalRes,int selectedRes,int errorRes) {
        super(context);//调用父类方法
        this.normalRes=normalRes; //对这个对象配置参数属性和方法
        this.selectedRes=selectedRes;
        this.errorRes=errorRes;
    }

    //xml中配置
    public StatefulImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //定义枚举 管理照片的三种状态
    public enum State{
        NORMAL,  //0
        SELECTED,  //1
        ERROR  //2
    }
}
