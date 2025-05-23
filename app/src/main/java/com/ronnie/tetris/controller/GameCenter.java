package com.ronnie.tetris.controller;

import com.ronnie.tetris.model.Block;
import com.ronnie.tetris.model.BlockManager;
import com.ronnie.tetris.ui.view.BlockPreviewView;
import com.ronnie.tetris.ui.view.GameView;

public class GameCenter {


    public static final GameCenter defaultCenter=new GameCenter();  //提供单例对象

    public BlockPreviewView previewView;
    public GameView gameView;
    public void start(){
        //  先生成预览方块
        //  TODO:给一个临时的数字,BlockView绘制的时候需要回调这个值过来
        Block previewBlock = BlockManager.defaultManager.getNextBlock(20);


        //  将这个方块在预览视图中显示
        previewView.showBlock(previewBlock);
    }

    private GameCenter(){}
}
