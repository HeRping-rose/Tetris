package com.baidu.tetris.ui.activity;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.baidu.tetris.R;
import com.baidu.tetris.controller.GameCenter;
import com.baidu.tetris.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity implements GameCenter.OnGameOverListener, GameCenter.OnScoreChangeListener {
    private ActivityGameBinding mBinding;
    private boolean isStarted = false;
    private boolean isGameOver = true;
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
        mBinding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //配置GameCenter
        GameCenter.defaultCenter.previewView = mBinding.previewView;
        GameCenter.defaultCenter.gameView = mBinding.gameView;
        GameCenter.defaultCenter.mGameOverListener = this;
        GameCenter.defaultCenter.mScoreChangeListener = this;

        //给按钮添加点击事件
        initUI();

        //播放背景音乐
        loadBGM();

        //加载音效
        loadSound();
    }

    //加载音效
    private void loadSound(){
        AudioAttributes attr = new AudioAttributes.Builder()
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
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (player != null){
            player.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null && player.isPlaying()){
            player.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null){
            player.release();
        }
    }

    //播放背景音乐
    private void loadBGM(){
        player = MediaPlayer.create(this,R.raw.bgm1);
        player.setVolume(0.4f,0.4f);//设置音量
        player.setLooping(true); //循环播放
    }

    private void initUI() {
        mBinding.playBtn.setOnClickListener(v ->{
            //开始游戏
            if (isStarted) {//暂停
                GameCenter.defaultCenter.pause();
                mBinding.playBtn.setImageResource(R.drawable.play);
            }else{//开始
                if (isGameOver){ //游戏过程中点击暂停之后再开始
                    mBinding.tvGameOver.setVisibility(View.INVISIBLE);
                    isGameOver = false;
                }
                mBinding.tvScore.setText("0");
                GameCenter.defaultCenter.start();
                mBinding.playBtn.setImageResource(R.drawable.stop);
                player.start(); //开始播放
            }
            isStarted = !isStarted;
        });

        mBinding.left.setOnClickListener(v ->{
            soundPool.play(moveSound,0.6f,0.6f,1,0,1f);
            GameCenter.defaultCenter.moveLeft();
        });
        mBinding.right.setOnClickListener(v ->{
            soundPool.play(moveSound,0.6f,0.6f,1,0,1f);
            GameCenter.defaultCenter.moveRight();
        });
        mBinding.down.setOnClickListener(v ->{
            soundPool.play(moveSound,0.6f,0.6f,1,0,1f);
            GameCenter.defaultCenter.moveDown();
        });
        mBinding.down.setOnLongClickListener(v -> {
            soundPool.play(dropSound,0.6f,0.6f,1,0,1f);
            GameCenter.defaultCenter.dropDown();
            return true;
        });
        mBinding.rotate.setOnClickListener(v ->{
            soundPool.play(rotateSound,0.6f,0.6f,1,0,1f);
            GameCenter.defaultCenter.rotate();
        });
    }

    @Override
    public void gameOver() {
        isStarted = false;
        isGameOver = true;
        soundPool.play(gameOverSound,0.6f,0.6f,1,0,1f);
        player.pause(); //开始播放
        mBinding.playBtn.setImageResource(R.drawable.play);

        //弹窗 游戏结束
        mBinding.tvGameOver.setVisibility(View.VISIBLE);
        ScaleAnimation sa = new ScaleAnimation(0f,1f,0f,1f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        sa.setDuration(400);
        mBinding.tvGameOver.startAnimation(sa);
    }

    @Override
    public void scoreChanged(int score) {
        soundPool.play(clearSound,0.6f,0.6f,1,0,1f);
        mBinding.tvScore.setText(score+"");
    }
}