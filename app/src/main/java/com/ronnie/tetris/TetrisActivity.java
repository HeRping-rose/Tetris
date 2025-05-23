package com.ronnie.tetris;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ronnie.tetris.databinding.ActivityTetrisBinding;

import java.util.HashMap;
import java.util.Map;


public class TetrisActivity extends AppCompatActivity {
    ActivityTetrisBinding tetrisBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tetrisBinding = ActivityTetrisBinding.inflate(getLayoutInflater());
        setContentView(tetrisBinding.getRoot());
//        setContentView(R.layout.activity_tetris);

    }

}