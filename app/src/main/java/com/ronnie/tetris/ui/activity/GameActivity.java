package com.ronnie.tetris.ui.activity;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ronnie.tetris.R;
import com.ronnie.tetris.controller.GameCenter;
import com.ronnie.tetris.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {
    private ActivityGameBinding gameBinding;
    private boolean isPlaying = false;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //视图绑定
        gameBinding = ActivityGameBinding.inflate(getLayoutInflater());

        setContentView(gameBinding.getRoot());

        //配置GameCenter  绑定View配置
        GameCenter.defaultCenter.previewView=gameBinding.previewView;
        GameCenter.defaultCenter.gameView=gameBinding.gameView;

        //给按钮添加点击事件
        initUI();
    }

    //初始化UI控件
    private void initUI() {
        gameBinding.playBtn.setOnClickListener(v->{
            // 方法二:或者 给v设置tag值
            if(!isPlaying){
                GameCenter.defaultCenter.start();
                // 默认开始后设置暂停图标
                gameBinding.playBtn.setImageResource(R.drawable.stop);
            }else {
                GameCenter.defaultCenter.pause();
                gameBinding.playBtn.setImageResource(R.drawable.play);
            }
            isPlaying=!isPlaying;
        });
        //添加操作按钮点击事件
        gameBinding.ivRotate.setOnClickListener(v->{
            GameCenter.defaultCenter.rotate();
        });
        gameBinding.ivDown.setOnClickListener(v->{

            GameCenter.defaultCenter.moveDown();
        });
        gameBinding.ivDown.setOnLongClickListener(v->{
            GameCenter.defaultCenter.dropDown();
            return true;
        });
        gameBinding.ivLeft.setOnClickListener(v->{
            GameCenter.defaultCenter.moveLeft();
        });
        gameBinding.ivRight.setOnClickListener(v->{
            GameCenter.defaultCenter.moveRight();
        });
    }
}