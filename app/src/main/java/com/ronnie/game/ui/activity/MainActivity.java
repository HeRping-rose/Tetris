package com.ronnie.game.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ronnie.game.R;
import com.ronnie.game.controller.GameCenter;
import com.ronnie.game.databinding.ActivityMainBinding;
import com.ronnie.game.model.Block;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;//全局定义变量

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //加载layout布局并获取对应的绑定类实例
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());

        // 设置内容视图
        setContentView(mBinding.getRoot());


        GameCenter.defaultGameCenter.preViewBlockView = mBinding.previewBlock;
        GameCenter.defaultGameCenter.gameView= mBinding.gameView;

        GameCenter.defaultGameCenter.start();
        mBinding.tvScore.setText("123");

        mBinding.ivPlay.setOnClickListener(v->{
            mBinding.previewBlock.showBlock(new Block(3,0));

        });


    }
}