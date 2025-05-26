package com.ronnie.tetris.controller;

import com.ronnie.tetris.model.Block;
import com.ronnie.tetris.model.BlockManager;
import com.ronnie.tetris.ui.view.BlockPreviewView;
import com.ronnie.tetris.ui.view.GameView;

public class GameCenter {
    public static final GameCenter defaultCenter=new GameCenter();  //提供单例对象

    public BlockPreviewView previewView;
    public GameView gameView;
    private int mRow;//行
    private int mColumn;//列
    //

    public void start(){
        //  先生成预览方块
        //  TODO:给一个临时的数字,BlockView绘制的时候需要回调这个值过来
        Block previewBlock = BlockManager.defaultManager.getNextBlock();

        //  将这个方块在预览视图中显示
        previewView.showBlock(previewBlock);
    }

    //  初始化行和列数  提供一个方法  因为是单例设计模式   或者说使用数据回调
    public void initRowAndColumn(int row, int column){
        mRow=row;
        mColumn=column;
        BlockManager.defaultManager.resetColumn(column);
    }

    private GameCenter(){}
    // 旋转
    public void rotate() {
        Block block=BlockManager.defaultManager.getCurrentBlock();
        block.rotate();
        // 判断是否碰撞
        if (isCollision()) {
            for (int i = 0; i < 3; i++) {
                block.rotate();
            }
        } else {
            gameView.refresh();
        }

    }
    // 下移
    public void moveDown() {
        Block block=BlockManager.defaultManager.getCurrentBlock();
        block.moveDown();


        if (isCollision()) {
            // 还原上移
            block.moveUp();
        } else {
            gameView.refresh();
        }
        if(block.y+ block.x>=mRow){
            mergeShape();
        }


    }
    // 左移
    public void moveLeft() {
        Block block=BlockManager.defaultManager.getCurrentBlock();
        block.moveLeft();

        if (isCollision()) {
            // 还原右移
            block.moveRight();
        } else {
            gameView.refresh();
        }
    }
    // 右移
    public void moveRight() {
        Block block=BlockManager.defaultManager.getCurrentBlock();
        block.moveRight();

        if (isCollision()) {
            // 还原左移
            block.moveLeft();
        } else {
            gameView.refresh();
        }
    }

    // 判断碰撞条件
    private boolean isCollision() {
        // 获取当前操作的block对象
        Block block=BlockManager.defaultManager.getCurrentBlock();
        int[][] shape = block.getShape();//获取二维数组
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                if (shape[i][j] == 1) {
                    if(block.x+j<0) return true;//左边界
                    if (block.x+j>=mColumn) return true;// 右边界
                    if(block.y+i>=mRow) return true;// 下边界
                }
            }
        }
        return false;
    }
    private void mergeShape() {
        // 获取当前操作的block对象
        Block block=BlockManager.defaultManager.getCurrentBlock();
        Block block1=BlockManager.defaultManager.getNextBlock();
        int[][] shape = block.getShape();//获取二维数组
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    previewView.showBlock(block);
                }
            }
        }
    }

}
