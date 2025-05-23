package com.ronnie.tetris.ui.activity;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //视图绑定
        gameBinding = ActivityGameBinding.inflate(getLayoutInflater());

        setContentView(gameBinding.getRoot());

        //配置GameCenter
        GameCenter.defaultCenter.previewView=gameBinding.previewView;


        //给按钮添加点击事件
        initUI();
    }

    private void initUI() {
        gameBinding.playBtn.setOnClickListener(v->{
            GameCenter.defaultCenter.start();
        });
    }
}