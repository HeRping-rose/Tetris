package com.ronnie.game.controller;

import com.ronnie.game.model.Block;
import com.ronnie.game.model.BlockManager;
import com.ronnie.game.ui.view.GameView;
import com.ronnie.game.ui.view.PreViewBlockView;

public class GameCenter {
    private int row;
    private int col;
    public  static final GameCenter defaultGameCenter=new GameCenter();//提供单例对象给外部使用
    public PreViewBlockView preViewBlockView;
    public GameView gameView;

    public void start(){
        Block previewBlock= BlockManager.defaultBlockManager.getNextBlock();
        preViewBlockView.showBlock(previewBlock);
    }
    private GameCenter(){}

    public void initRowAndCol(int row, int col) {
        this.row=row;
        this.col=col;
    }
}
