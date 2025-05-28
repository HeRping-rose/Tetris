package com.ronnie.tetris.ui.activity;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.provider.MediaStore;
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
    private MediaPlayer player;
    private SoundPool soundPool;
    private int moveSound;
    private int rotateSound;
    private int dropSound;
    private int clearSound;
    private int gameOverSound;
    private int leverUpSound;

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

    //     播放bgm
        loadSound();

        loadBGM();
    }

    private void loadBGM() {
        player = MediaPlayer.create(this,R.raw.bgm1);
        player.setVolume(0.4f,0.4f);//设置音量
        player.setLooping(true); //循环播放
    }

    // 加载音效
    private void loadSound() {

        AudioAttributes attr=new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(attr)
                .build();

        //记载音效
        //移动音效
        moveSound = soundPool.load(this,R.raw.move,1);
        //掉落音效
        dropSound = soundPool.load(this,R.raw.fast_down1,1);
        //旋转音效
        rotateSound = soundPool.load(this,R.raw.rotate1,1);
        //清行音效
        clearSound = soundPool.load(this,R.raw.clear1,1);
        //游戏结束音效
        gameOverSound = soundPool.load(this,R.raw.game_over,1);
        //登记提升音效
        leverUpSound = soundPool.load(this,R.raw.level_up,1);

    //     加载音效
    }



    // 从后台重新进去前台
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    // 前台进入后台
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                gameBinding.tvScore.setText("0");
                GameCenter.defaultCenter.start();
                // 默认开始后设置暂停图标
                gameBinding.playBtn.setImageResource(R.drawable.stop);
                player.start();//开始音效

            }else {//暂停
                GameCenter.defaultCenter.pause();
                gameBinding.playBtn.setImageResource(R.drawable.play);
            }

            isPlaying=!isPlaying;
        });
        //添加操作按钮点击事件
        gameBinding.ivRotate.setOnClickListener(v->{
            soundPool.play(rotateSound,0.5f,0.5f,1,0,1f);
            GameCenter.defaultCenter.rotate();
        });
        gameBinding.ivDown.setOnClickListener(v->{
            soundPool.play(moveSound,0.5f,0.5f,1,0,1f);


            GameCenter.defaultCenter.moveDown();
        });
        gameBinding.ivDown.setOnLongClickListener(v->{
            soundPool.play(dropSound,0.5f,0.5f,1,0,1f);

            GameCenter.defaultCenter.dropDown();
            return true;
        });
        gameBinding.ivLeft.setOnClickListener(v->{
            soundPool.play(moveSound,0.5f,0.5f,1,0,1f);

            GameCenter.defaultCenter.moveLeft();
        });
        gameBinding.ivRight.setOnClickListener(v->{
            soundPool.play(moveSound,0.5f,0.5f,1,0,1f);

            GameCenter.defaultCenter.moveRight();
        });
    }

    @Override
    public void gameover() {
        isPlaying = false;
        isGameOver = true;
        soundPool.play(gameOverSound,0.5f,0.5f,1,0,1f);
        player.pause();

        gameBinding.playBtn.setImageResource(R.drawable.play);

        //弹窗 游戏结束
        gameBinding.tvGameOver.setVisibility(View.VISIBLE);
        ScaleAnimation sa = new ScaleAnimation(0f,1f,0f,1f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        sa.setDuration(400);
        gameBinding.tvGameOver.startAnimation(sa);

    }

    @Override
    public void scoreChanged(int score) {
        soundPool.play(clearSound,0.5f,0.5f,1,0,1f);

        gameBinding.tvScore.setText(score+"");
    }
}