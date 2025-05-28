package com.ronnie.tetris.ui.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ronnie.tetris.R;
import com.ronnie.tetris.controller.GameCenter;
import com.ronnie.tetris.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity implements GameCenter.OnGameOverListener,GameCenter.OnScoreChangeListener{
    private ActivityGameBinding gameBinding;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    private boolean isGameOver=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //视图绑定
        gameBinding = ActivityGameBinding.inflate(getLayoutInflater());

        setContentView(gameBinding.getRoot());

        //配置GameCenter  绑定View配置
        GameCenter.defaultCenter.previewView=gameBinding.previewView;
        GameCenter.defaultCenter.gameView=gameBinding.gameView;
        GameCenter.defaultCenter.mGameOverListener=this;
        GameCenter.defaultCenter.mScoreChangeListener=this;

        //给按钮添加点击事件
        initUI();
    }

    //初始化UI控件
    private void initUI() {
        gameBinding.playBtn.setOnClickListener(v->{
            // 方法二:或者 给v设置tag值
            if(!isPlaying){//开始

                if (isGameOver) {
                //    游戏过程中点击暂停之后再开始
                    gameBinding.tvGameOver.setVisibility(View.INVISIBLE);
                    isGameOver=false;
                }
                GameCenter.defaultCenter.start();
                // 默认开始后设置暂停图标
                gameBinding.playBtn.setImageResource(R.drawable.stop);
            }else {//暂停
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

    @Override
    public void gameover() {
        isPlaying = false;
        isGameOver = true;

        gameBinding.playBtn.setImageResource(R.drawable.play);

        //弹窗 游戏结束
        gameBinding.tvGameOver.setVisibility(View.VISIBLE);
        ScaleAnimation sa = new ScaleAnimation(0f,1f,0f,1f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        sa.setDuration(400);
        gameBinding.tvGameOver.startAnimation(sa);

    }

    @Override
    public void scoreChanged(int score) {
        gameBinding.tvScore.setText(score+"");
    }
}