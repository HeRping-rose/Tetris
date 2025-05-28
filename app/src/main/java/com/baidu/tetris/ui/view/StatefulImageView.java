package com.baidu.tetris.ui.view;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 有状态的图片控件
 */
public class StatefulImageView extends AppCompatImageView {
    //定义变量记录三种状态对应的图片资源
    private int normalRes = 0;
    private int selectedRes = 0;
    private int errorRes = 0;
    //定义一个变量记录状态值
    private State mSate = State.NORMAL;

    //提供给外部一个设置状态的方法
    public void changeStateTo(State state){
        //保存好外部传递过来的状态值
        this.mSate = state;

        if (mSate == State.NORMAL){
            setImageResource(normalRes);
        } else if (mSate == State.SELECTED) {
            setImageResource(selectedRes);
        }else{
            setImageResource(errorRes);
        }
    }

    //提供一个构造方法，外部构建的同时传递基本的图片资源
    //代码中使用
    public StatefulImageView(Context context,int normalRes,int selectedRes,int errorRes){
        //先调用父类构建对象
        super(context);
        //再对这个对象配置属性或者方法
        this.normalRes = normalRes;
        this.selectedRes = selectedRes;
        this.errorRes = errorRes;
    }

    //定义枚举 管理图片的3种状态
    public enum State{
        NORMAL, //0
        SELECTED, //1
        ERROR //2
    }
}











